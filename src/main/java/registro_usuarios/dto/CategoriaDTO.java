package registro_usuarios.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private Boolean activo;
}
