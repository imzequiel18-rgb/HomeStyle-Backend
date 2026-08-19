package registro_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PedidoRecienteDTO {

    private String numeroPedido;

    private String cliente;

    private LocalDateTime fecha;

    private String estado;

    private BigDecimal total;

}