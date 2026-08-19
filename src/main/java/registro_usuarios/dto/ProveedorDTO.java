package registro_usuarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorDTO {

    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;
    private String personaContacto;
    private Boolean activo;

}
