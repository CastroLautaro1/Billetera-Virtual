package com.billetera_virtual.deposit.infra.data.adapter;

import com.billetera_virtual.deposit.domain.PaymentInfoDTO;
import com.billetera_virtual.deposit.domain.ports.PaymentGatewayPort;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MercadoPagoAdapter implements PaymentGatewayPort {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    // Inicializamos la llave maestra cuando arranca el proyecto
    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    @Override
    public PaymentInfoDTO getPaymentInfo(String paymentId) {
        try {
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.valueOf(paymentId));

            boolean isApproved = "approved".equals(payment.getStatus());

            return new PaymentInfoDTO(
                    isApproved,
                    payment.getTransactionAmount(),
                    payment.getExternalReference()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar MP", e);
        }
    }

    @Override
    public String createDepositPreference(BigDecimal amount, String userEmail) {
        try {
            // 1. Configuramos el ítem (La recarga)
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title("Recarga de saldo en Oran")
                    .quantity(1)
                    .unitPrice(amount)
                    .currencyId("ARS")
                    .build();

            // 2. Rutas de retorno hacia Angular
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:4200/payment-success")
                    .pending("http://localhost:4200/payment-pending")
                    .failure("http://localhost:4200/payment-error")
                    .build();

            // 3. Armamos el paquete completo
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(Collections.singletonList(itemRequest))
                    .backUrls(backUrls)
                    //.autoReturn("approved")
                    .externalReference(userEmail) // Etiquetamos el pago con el email del usuario
                    .notificationUrl("https://dig-coaster-trapping.ngrok-free.dev/webhook/mercadopago") // Tunel de Ngrok
                    .build();

            // 4. Se lo mandamos a MP y obtenemos la respuesta
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return preference.getInitPoint(); // String con la URL de pago

        } catch (MPApiException apiException) {
            throw new RuntimeException("❌ Mercado Pago rechazó la petición", apiException);
        }
        catch (MPException mpException) {
            throw new RuntimeException("❌Error del SDK de MP", mpException);
        }
        catch (Exception e) {
            throw new RuntimeException("❌ Error general al generar el link", e);
        }
    }

}
