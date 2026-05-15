package com.billetera_virtual.account.infra.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class InterestSchedulerService {

    private final JobLauncher jobLauncher;
    private final Job payInterestJob;
    private final JobExplorer jobExplorer;


    // 1. LA RECUPERACIÓN INTELIGENTE (El bucle Catch-up)
    @EventListener(ApplicationReadyEvent.class)
    public void checkMissedJobsOnStartup() {
        System.out.println("--- Verificando estado de intereses...");

        JobInstance lastInstance = jobExplorer.getLastJobInstance("payInterestJob");

        if (lastInstance != null) {
            JobExecution lastExecution = jobExplorer.getLastJobExecution(lastInstance);

            if (lastExecution != null && lastExecution.getEndTime() != null) {
                String lastTargetDateStr = lastExecution.getJobParameters().getString("targetDate");
                LocalDate lastRunDate;

                if (lastTargetDateStr != null) {
                    lastRunDate = LocalDate.parse(lastTargetDateStr); // Ultimo dia en que se pagó
                } else {
                    // Fallback de seguridad por si hay ejecuciones viejas sin el parámetro
                    lastRunDate = lastExecution.getEndTime() != null ?
                            lastExecution.getEndTime().toLocalDate() : LocalDate.now();
                }

                LocalDate today = LocalDate.now();

                if (lastRunDate.isBefore(today)) {
                    System.out.println("--- PENDIENTE: Hay días sin pagar. Iniciando recuperación...");

                    // Hilo asíncrono para no trabar el arranque
                    CompletableFuture.runAsync(() -> {
                        // Arrancamos desde el día siguiente al último pago
                        LocalDate dateToProcess = lastRunDate.plusDays(1);

                        // Mientras la fecha a procesar sea anterior o igual a hoy...
                        while (!dateToProcess.isAfter(today)) {
                            System.out.println("--- Recuperando intereses del día: " + dateToProcess);
                            executeJob(dateToProcess);
                            dateToProcess = dateToProcess.plusDays(1); // Pasamos al siguiente día
                        }
                    });
                } else {
                    System.out.println("--- Intereses al día.");
                }
            }
        } else {
            System.out.println("--- Primera ejecución detectada...");
            CompletableFuture.runAsync(() -> executeJob(LocalDate.now()));
        }
    }

    // 2. EJECUCIÓN NORMAL PROGRAMADA (Todos los días a las 00:00)
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledMidnightJob() {
        System.out.println("--- Medianoche detectada. Iniciando pago de intereses...");
        executeJob(LocalDate.now());
    }

    // 3. LÓGICA CENTRAL PARA LANZAR EL TRABAJO
    private void executeJob(LocalDate targetDate) {
        try {
            // Spring Batch requiere parámetros únicos para ejecutar el mismo Job varias veces.
            JobParameters parameters = new JobParametersBuilder()
                    .addString("targetDate", targetDate.toString()) // Guarda la fecha exacta en el historial
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(payInterestJob, parameters);
            System.out.println("--- Job de intereses finalizado con éxito.");

        } catch (Exception e) {
            System.err.println("--- Error al ejecutar el Job de intereses: " + e.getMessage());
        }
    }
}
