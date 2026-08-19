package registro_usuarios.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoAdminDTO {

    private Long id;

    private String numeroPedido;

    private String cliente;

    private LocalDateTime fecha;

    private String estado;

    private BigDecimal total;

    private String metodoPago;

    private String direccionEnvio;

}
