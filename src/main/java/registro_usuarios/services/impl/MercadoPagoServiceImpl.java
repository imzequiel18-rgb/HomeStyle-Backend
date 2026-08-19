package registro_usuarios.services.impl;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import registro_usuarios.entities.DetallePedido;
import registro_usuarios.entities.Pedido;
import registro_usuarios.services.MercadoPagoService;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoServiceImpl implements MercadoPagoService {

    @Value("${mercadopago.notification-url}")
    private String notificationUrl;

    @Value("${mercadopago.frontend-url}")
    private String frontendUrl;

    @Value("${mercadopago.preference-expiration-minutes:30}")
    private long preferenceExpirationMinutes;

    @Override
    public Preference crearPreferencia(Pedido pedido) {

        List<PreferenceItemRequest> items = new ArrayList<>();

        for (DetallePedido detalle : pedido.getDetalles()) {

            PreferenceItemRequest item =
                    PreferenceItemRequest.builder()
                            .id(detalle.getSkuProducto())
                            .title(detalle.getNombreProducto())
                            .quantity(detalle.getCantidad())
                            .currencyId("MXN")
                            .unitPrice(detalle.getPrecioUnitario())
                            .build();

            items.add(item);
        }

        OffsetDateTime ahora = OffsetDateTime.now();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(frontendUrl + "/pago/resultado")
                .pending(frontendUrl + "/pago/resultado")
                .failure(frontendUrl + "/pago/resultado")
                .build();

        PreferenceRequest preferenceRequest =
                PreferenceRequest.builder()
                        .items(items)
                        .externalReference(pedido.getNumeroPedido())
                        .backUrls(backUrls)
                        .autoReturn("approved")
                        .notificationUrl(notificationUrl)
                        .expires(true)
                        .expirationDateFrom(ahora)
                        .expirationDateTo(ahora.plusMinutes(preferenceExpirationMinutes))
                        .build();

        PreferenceClient client = new PreferenceClient();

        try {
            return client.create(preferenceRequest);

        } catch (MPApiException e) {
            imprimirErrorApi(e, "crear la preferencia");
            throw new RuntimeException(
                    "Error de la API de Mercado Pago: " + e.getApiResponse().getContent(),
                    e
            );

        } catch (MPException e) {
            imprimirErrorSdk(e, "crear la preferencia");
            throw new RuntimeException(
                    "Error de Mercado Pago: " + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public void invalidarPreferencia(String preferenceId) {
        if (preferenceId == null || preferenceId.isBlank()) {
            return;
        }

        PreferenceClient client = new PreferenceClient();
        OffsetDateTime ahora = OffsetDateTime.now();

        PreferenceRequest request = PreferenceRequest.builder()
                .expires(true)
                .expirationDateFrom(ahora.minusMinutes(1))
                .expirationDateTo(ahora)
                .build();

        try {
            client.update(preferenceId, request);
            System.out.println("Preferencia de Mercado Pago invalidada: " + preferenceId);
        } catch (MPApiException e) {
            // La cancelación local no debe fallar si Mercado Pago no permite
            // actualizar una preferencia que ya expiró o cambió de estado.
            imprimirErrorApi(e, "invalidar la preferencia " + preferenceId);
        } catch (MPException e) {
            imprimirErrorSdk(e, "invalidar la preferencia " + preferenceId);
        }
    }

    private void imprimirErrorApi(MPApiException e, String operacion) {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("       ERROR DE LA API DE MERCADO PAGO");
        System.out.println("==============================================");
        System.out.println("Operación: " + operacion);
        System.out.println("Mensaje: " + e.getMessage());
        if (e.getApiResponse() != null) {
            System.out.println("Código HTTP: " + e.getApiResponse().getStatusCode());
            System.out.println("Respuesta: " + e.getApiResponse().getContent());
        }
        System.out.println("==============================================");
        System.out.println();
    }

    private void imprimirErrorSdk(MPException e, String operacion) {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("          ERROR DE MERCADO PAGO");
        System.out.println("==============================================");
        System.out.println("Operación: " + operacion);
        System.out.println("Mensaje: " + e.getMessage());
        System.out.println("==============================================");
        System.out.println();
    }
}
