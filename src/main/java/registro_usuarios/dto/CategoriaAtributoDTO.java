package registro_usuarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaAtributoDTO {

    private Long id;

    private Long categoriaId;

    private String categoriaNombre;

    private Long atributoId;

    private String atributoNombre;

    private Boolean obligatorio;

    private Integer orden;

    private String tipoDato;

    private String unidad;
}
