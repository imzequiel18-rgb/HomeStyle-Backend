package registro_usuarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarcaDTO {
    private Long id;

    private String nombre;

    private String descripcion;

    private String logo;

    private Boolean activo;
}
