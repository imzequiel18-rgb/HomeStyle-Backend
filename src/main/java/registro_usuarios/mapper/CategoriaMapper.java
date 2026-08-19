package registro_usuarios.mapper;

import org.springframework.stereotype.Component;
import registro_usuarios.dto.CategoriaDTO;
import registro_usuarios.entities.Categoria;

import java.util.List;

@Component
public class CategoriaMapper {

    public CategoriaDTO toDTO(Categoria categoria) {

        if (categoria == null) {
            return null;
        }

        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .imagen(categoria.getImagen())
                .activo(categoria.getActivo())
                .build();
    }

    public List<CategoriaDTO> toDTOList(List<Categoria> categorias) {

        return categorias.stream()
                .map(this::toDTO)
                .toList();

    }

    public Categoria toEntity(CategoriaDTO dto) {

        Categoria categoria = new Categoria();

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setImagen(dto.getImagen());
        categoria.setActivo(dto.getActivo());

        return categoria;
    }
}