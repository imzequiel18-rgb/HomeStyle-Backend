package registro_usuarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoDTO  {
    private Long id;

    private String nombre;

    private String tipoDato;

    private String unidad;

    private Boolean activo;

}
