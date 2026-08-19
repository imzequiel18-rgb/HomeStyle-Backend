package registro_usuarios.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.MercadoPagoPreferenceDTO;
import registro_usuarios.dto.PedidoDTO;
import registro_usuarios.dto.PedidoDetalleDTO;
import registro_usuarios.dto.PedidoResumenDTO;
import registro_usuarios.services.PedidoService;
import registro_usuarios.services.MercadoPagoWebhookSignatureService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final MercadoPagoWebhookSignatureService webhookSignatureService;

    @PostMapping("/finalizar")
    public ResponseEntity<PedidoDTO> finalizarCompra() {

        return ResponseEntity.ok(
                pedidoService.finalizarCompra()
        );
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResumenDTO>> obtenerMisPedidos() {

        return ResponseEntity.ok(
                pedidoService.obtenerMisPedidos()
        );
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoDetalleDTO> obtenerDetallePedido(
            @PathVariable Long pedidoId) {

        return ResponseEntity.ok(
                pedidoService.obtenerDetallePedido(pedidoId)
        );
    }

    @PostMapping("/{pedidoId}/cancelar")
    public ResponseEntity<Void> cancelarPedido(
            @PathVariable Long pedidoId) {

        pedidoService.cancelarPedido(pedidoId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/pagar/mercadopago")
    public ResponseEntity<MercadoPagoPreferenceDTO> iniciarPagoMercadoPago() {

        return ResponseEntity.ok(
                pedidoService.iniciarPagoMercadoPago()
        );
    }

    @PostMapping("/mercadopago/confirmar/{paymentId}")
    public ResponseEntity<Void> confirmarPagoMercadoPago(@PathVariable Long paymentId) {
        // La fuente de verdad sigue siendo la API de Mercado Pago;
        // este endpoint solo acelera la sincronización al volver del checkout.
        pedidoService.procesarPagoMercadoPago(paymentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mercadopago/webhook")
    public ResponseEntity<Void> webhookMercadoPago(
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam Map<String, String> queryParams,
            @RequestHeader(value = "x-signature", required = false) String xSignature,
            @RequestHeader(value = "x-request-id", required = false) String xRequestId) {

        System.out.println("==============================================");
        System.out.println("        WEBHOOK MERCADO PAGO");
        System.out.println("==============================================");
        System.out.println("Body recibido: " + body);

        if (body == null) {
            body = Map.of();
        }

        try {

            Long paymentId = null;
            String dataIdFirma = queryParams.get("data.id");

            // Mercado Pago puede enviar data.id también en el body.
            if (dataIdFirma == null || dataIdFirma.isBlank()) {
                Object dataObjFirma = body.get("data");
                if (dataObjFirma instanceof Map<?, ?> dataMapFirma) {
                    Object idObjFirma = dataMapFirma.get("id");
                    if (idObjFirma != null) {
                        dataIdFirma = idObjFirma.toString();
                    }
                }
            }

            if (!webhookSignatureService.validar(xSignature, xRequestId, dataIdFirma)) {
                System.out.println("Firma de webhook inválida o ausente.");
                return ResponseEntity.status(401).build();
            }

            if (dataIdFirma != null && dataIdFirma.matches("\\d+")) {
                paymentId = Long.valueOf(dataIdFirma);
            }

            /*
             * FORMATO WEBHOOK ACTUAL
             *
             * {
             *   "type": "payment",
             *   "data": {
             *      "id": "123456"
             *   }
             * }
             */
            Object typeObj = body.get("type");

            if (typeObj != null
                    && "payment".equalsIgnoreCase(typeObj.toString())) {

                Object dataObj = body.get("data");

                if (dataObj instanceof Map<?, ?> dataMap) {

                    Object idObj = dataMap.get("id");

                    if (idObj != null) {
                        paymentId = Long.valueOf(idObj.toString());
                    }
                }
            }

            /*
             * FORMATO IPN
             *
             * {
             *   "topic": "payment",
             *   "resource": "123456"
             * }
             */
            if (paymentId == null) {

                Object topicObj = body.get("topic");
                Object resourceObj = body.get("resource");

                if (topicObj != null
                        && "payment".equalsIgnoreCase(topicObj.toString())
                        && resourceObj != null) {

                    String resource = resourceObj.toString();

                    /*
                     * A veces resource puede venir como ID,
                     * y otras veces como una URL.
                     */
                    if (resource.matches("\\d+")) {

                        paymentId = Long.valueOf(resource);

                    } else {

                        String[] partes = resource.split("/");

                        String ultimo = partes[partes.length - 1];

                        if (ultimo.matches("\\d+")) {
                            paymentId = Long.valueOf(ultimo);
                        }
                    }
                }
            }

            if (paymentId != null) {

                System.out.println(
                        "Payment ID detectado: " + paymentId
                );

                pedidoService.procesarPagoMercadoPago(paymentId);

            } else {

                System.out.println(
                        "La notificación no corresponde "
                                + "a un pago procesable."
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Error procesando webhook de Mercado Pago: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        System.out.println("==============================================");

        return ResponseEntity.ok().build();
    }



}