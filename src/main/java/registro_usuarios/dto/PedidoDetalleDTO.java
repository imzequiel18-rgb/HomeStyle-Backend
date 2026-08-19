package registro_usuarios.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDetalleDTO {

    private Long id;

    private String numeroPedido;

    private LocalDateTime fecha;

    private String estado;

    private String metodoPago;

    private String direccionEnvio;

    private String mercadoPagoPaymentId;

    private String mercadoPagoStatus;

    private String mercadoPagoStatusDetail;

    private BigDecimal total;

    private List<DetallePedidoDTO> productos;

}