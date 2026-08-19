package registro_usuarios.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoDTO {

    private Long productoId;

    private String nombreProducto;

    private String imagen;

    private Integer cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal subtotal;
}