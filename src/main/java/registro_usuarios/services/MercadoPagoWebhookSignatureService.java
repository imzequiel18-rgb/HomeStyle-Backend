package registro_usuarios.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Service
public class MercadoPagoWebhookSignatureService {

    @Value("${mercadopago.webhook-secret:}")
    private String webhookSecret;

    @Value("${mercadopago.mode:test}")
    private String mercadoPagoMode;

    public boolean validar(String xSignature, String xRequestId, String dataId) {
        if (webhookSecret == null || webhookSecret.isBlank()) {
            // En desarrollo permitimos probar sin firma. En producción, nunca.
            return !"production".equalsIgnoreCase(mercadoPagoMode);
        }

        if (xSignature == null || xSignature.isBlank()) {
            return false;
        }

        try {
            Map<String, String> partes = new HashMap<>();
            for (String parte : xSignature.split(",")) {
                String[] kv = parte.trim().split("=", 2);
                if (kv.length == 2) {
                    partes.put(kv[0], kv[1]);
                }
            }

            String ts = partes.get("ts");
            String v1 = partes.get("v1");
            if (ts == null || v1 == null) {
                return false;
            }

            StringBuilder manifest = new StringBuilder();
            if (dataId != null && !dataId.isBlank()) {
                manifest.append("id:").append(dataId.toLowerCase()).append(';');
            }
            if (xRequestId != null && !xRequestId.isBlank()) {
                manifest.append("request-id:").append(xRequestId).append(';');
            }
            manifest.append("ts:").append(ts).append(';');

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] digest = mac.doFinal(manifest.toString().getBytes(StandardCharsets.UTF_8));
            String calculada = toHex(digest);

            return MessageDigest.isEqual(
                    calculada.getBytes(StandardCharsets.UTF_8),
                    v1.toLowerCase().getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
