package registro_usuarios.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DireccionDTO {

    private Long id;

    private String nombreDestinatario;

    private String telefonoContacto;

    private String calle;

    private String numeroExterior;

    private String numeroInterior;

    private String colonia;

    private String ciudad;

    private String estado;

    private String codigoPostal;

    private String referencias;

    private Double latitud;

    private Double longitud;

    private Boolean predeterminada;

}