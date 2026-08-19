package registro_usuarios.mapper;

import org.springframework.stereotype.Component;
import registro_usuarios.dto.CategoriaAtributoDTO;
import registro_usuarios.entities.CategoriaAtributo;

import java.util.List;

@Component
public class CategoriaAtributoMapper {

    public CategoriaAtributoDTO toDTO(CategoriaAtributo relacion) {

        if (relacion == null) {
            return null;
        }

        return CategoriaAtributoDTO.builder()
                .id(relacion.getId())
                .categoriaId(relacion.getCategoria().getId())
                .categoriaNombre(relacion.getCategoria().getNombre())
                .atributoId(relacion.getAtributo().getId())
                .atributoNombre(relacion.getAtributo().getNombre())
                .tipoDato(relacion.getAtributo().getTipoDato().name())
                .unidad(relacion.getAtributo().getUnidad())
                .obligatorio(relacion.getObligatorio())
                .orden(relacion.getOrden())
                .build();

    }

    public List<CategoriaAtributoDTO> toDTOList(List<CategoriaAtributo> relaciones) {

        return relaciones.stream()
                .map(this::toDTO)
                .toList();

    }

    public CategoriaAtributo toEntity(CategoriaAtributoDTO dto) {

        CategoriaAtributo relacion = new CategoriaAtributo();

        relacion.setObligatorio(dto.getObligatorio());
        relacion.setOrden(dto.getOrden());

        return relacion;

    }

}