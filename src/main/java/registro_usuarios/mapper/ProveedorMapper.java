package registro_usuarios.mapper;

import org.springframework.stereotype.Component;
import registro_usuarios.dto.ProveedorDTO;
import registro_usuarios.entities.Proveedor;

import java.util.List;

@Component
public class ProveedorMapper {
    public ProveedorDTO toDTO(Proveedor proveedor){

        if(proveedor == null){
            return null;
        }

        return ProveedorDTO.builder()
                .id(proveedor.getId())
                .nombre(proveedor.getNombre())
                .telefono(proveedor.getTelefono())
                .correo(proveedor.getCorreo())
                .direccion(proveedor.getDireccion())
                .personaContacto(proveedor.getPersonaContacto())
                .activo(proveedor.getActivo())
                .build();

    }

    public List<ProveedorDTO> toDTOList(List<Proveedor> proveedores){

        return proveedores.stream()
                .map(this::toDTO)
                .toList();

    }

    public Proveedor toEntity(ProveedorDTO dto){

        Proveedor proveedor = new Proveedor();

        proveedor.setNombre(dto.getNombre());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCorreo(dto.getCorreo());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setPersonaContacto(dto.getPersonaContacto());
        proveedor.setActivo(dto.getActivo());

        return proveedor;

    }
}
