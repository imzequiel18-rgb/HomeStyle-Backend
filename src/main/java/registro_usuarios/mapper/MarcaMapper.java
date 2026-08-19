package registro_usuarios.mapper;

import org.springframework.stereotype.Component;
import registro_usuarios.dto.MarcaDTO;
import registro_usuarios.entities.Marca;

import java.util.List;

@Component
public class MarcaMapper {

    public MarcaDTO toDTO(Marca marca) {

        if (marca == null) {
            return null;
        }

        return MarcaDTO.builder()
                .id(marca.getId())
                .nombre(marca.getNombre())
                .descripcion(marca.getDescripcion())
                .logo(marca.getLogo())
                .activo(marca.getActivo())
                .build();
    }

    public List<MarcaDTO> toDTOList(List<Marca> marcas) {

        return marcas.stream()
                .map(this::toDTO)
                .toList();

    }

    public Marca toEntity(MarcaDTO dto) {

        Marca marca = new Marca();

        marca.setNombre(dto.getNombre());
        marca.setDescripcion(dto.getDescripcion());
        marca.setLogo(dto.getLogo());
        marca.setActivo(dto.getActivo());

        return marca;
    }

}