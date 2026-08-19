package registro_usuarios.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoClienteDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioVenta;
    private Integer stock;
    private String imagen;

    private String marcaNombre;

    private List<ProductoAtributoDTO> atributos;

    private Long categoriaId;
    private String categoriaNombre;


}
