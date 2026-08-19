package registro_usuarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoAtributoDTO {
    private Long id;

    private Long atributoId;

    private String atributoNombre;

    private String valor;

    private String unidad;

    private Long productoId;
}
