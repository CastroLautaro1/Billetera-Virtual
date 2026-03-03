package com.billetera_virtual.transaction.infra.data.external;

import com.billetera_virtual.account.domain.dto.AccountPublicDataResponse;
import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.domain.dto.TransactionAccountInfo;
import com.billetera_virtual.transaction.domain.port.external.AccountExternalPort;
import com.billetera_virtual.transaction.domain.port.external.ReceiptGeneratorPort;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReceiptGeneratorAdapter implements ReceiptGeneratorPort {

    private final TemplateEngine templateEngine;;

    public ReceiptGeneratorAdapter(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public byte[] generateReceipt(Transaction tx, TransactionAccountInfo origin, TransactionAccountInfo destination) {
        try {
            ZonedDateTime horaLocal = tx.getTimestamp().atZoneSameInstant(ZoneId.of("America/Argentina/Buenos_Aires"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy 'a las' HH:mm 'hs'", new Locale("es", "AR"));
            String formattedDate = horaLocal.format(formatter);

            formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);

            Context context = new Context();
            context.setVariable("date", formattedDate);
            context.setVariable("amount", tx.getAmount());
            context.setVariable("details", tx.getDetails());
            context.setVariable("originName", origin.fullname().toUpperCase());
            context.setVariable("originCvu", origin.cvu());
            context.setVariable("destinationName", destination.fullname().toUpperCase());
            context.setVariable("destinationCvu", destination.cvu());
            // ... setear las demás variables

            String htmlContent = templateEngine.process("receipt", context);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando el comprobante PDF", e);
        }
    }

}
