package registro_usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DireccionRequestDTO {

    @NotBlank
    private String nombreDestinatario;

    @NotBlank
    private String telefonoContacto;

    @NotBlank
    private String calle;

    @NotBlank
    private String numeroExterior;

    private String numeroInterior;

    @NotBlank
    private String colonia;

    @NotBlank
    private String ciudad;

    @NotBlank
    private String estado;

    @NotBlank
    private String codigoPostal;

    private String referencias;

    private Double latitud;

    private Double longitud;

}