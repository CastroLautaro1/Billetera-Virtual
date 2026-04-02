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

    // 1. RECUPERACIÓN ASÍNCRONA AL ARRANCAR LA APP
    @EventListener(ApplicationReadyEvent.class)
    public void checkMissedJobsOnStartup() {
        System.out.println("🔍 Verificando estado de intereses en la base de datos...");

        JobInstance lastInstance = jobExplorer.getLastJobInstance("payInterestJob");

        if (lastInstance != null) {
            JobExecution lastExecution = jobExplorer.getLastJobExecution(lastInstance);

            if (lastExecution != null && lastExecution.getEndTime() != null) {
                LocalDate lastRunDate = lastExecution.getEndTime().toLocalDate();
                LocalDate today = LocalDate.now();

                if (lastRunDate.isBefore(today)) {
                    System.out.println("⚠️ Pendiente: No se pagaron los intereses ayer. Iniciando recuperación asíncrona...");
                    // Se ejecuta en segundo plano para no bloquear la API
                    CompletableFuture.runAsync(this::executeJob);
                } else {
                    System.out.println("✅ Intereses al día. No hay tareas pendientes.");
                }
            }
        } else {
            System.out.println("🚀 Primera ejecución detectada. Iniciando Job de intereses en segundo plano...");
            CompletableFuture.runAsync(this::executeJob);
        }
    }

    // 2. EJECUCIÓN NORMAL PROGRAMADA (Todos los días a las 00:00)
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledMidnightJob() {
        System.out.println("⏰ Medianoche detectada. Iniciando pago de intereses...");
        executeJob();
    }

    // 3. LÓGICA CENTRAL PARA LANZAR EL TRABAJO
    private void executeJob() {
        try {
            // Le pasamos el tiempo actual en milisegundos como parámetro.
            // Spring Batch requiere parámetros únicos para ejecutar el mismo Job varias veces.
            JobParameters parameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(payInterestJob, parameters);
            System.out.println("✅ Job de intereses finalizado con éxito.");

        } catch (Exception e) {
            System.err.println("❌ Error al ejecutar el Job de intereses: " + e.getMessage());
        }
    }
}
