package registro_usuarios.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoAdminDTO {

    private Long id;
    private String sku;
    private String nombre;
    private String descripcion;
    private BigDecimal precioVenta;
    private Integer stock;
    private String imagen;
    private BigDecimal precioCosto;
    private Boolean activo;

    private Long categoriaId;
    private String categoriaNombre;

    private Long proveedorId;
    private String proveedorNombre;

    private Long marcaId;
    private String marcaNombre;

    private List<ProductoAtributoDTO> atributos;

}
