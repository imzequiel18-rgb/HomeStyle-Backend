package registro_usuarios.mapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import registro_usuarios.dto.DireccionDTO;
import registro_usuarios.entities.Direccion;
import registro_usuarios.entities.Usuario;

@Component
public class DireccionMapper {

    public DireccionDTO toDTO(Direccion direccion){

        if(direccion == null){

            return null;

        }

        return DireccionDTO.builder()

                .id(direccion.getId())

                .nombreDestinatario(direccion.getNombreDestinatario())

                .telefonoContacto(direccion.getTelefonoContacto())

                .calle(direccion.getCalle())

                .numeroExterior(direccion.getNumeroExterior())

                .numeroInterior(direccion.getNumeroInterior())

                .colonia(direccion.getColonia())

                .ciudad(direccion.getCiudad())

                .estado(direccion.getEstado())

                .codigoPostal(direccion.getCodigoPostal())

                .referencias(direccion.getReferencias())

                .latitud(direccion.getLatitud())

                .longitud(direccion.getLongitud())

                .predeterminada(direccion.getPredeterminada())

                .build();

    }

}