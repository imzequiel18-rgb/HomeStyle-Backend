package registro_usuarios.mapper;

import org.springframework.stereotype.Component;
import registro_usuarios.dto.AtributoDTO;
import registro_usuarios.entities.Atributo;

import java.util.List;

@Component
public class AtributoMapper {
    public AtributoDTO toDTO(Atributo atributo){

        if(atributo == null){
            return null;
        }

        return AtributoDTO.builder()
                .id(atributo.getId())
                .nombre(atributo.getNombre())
                .tipoDato(atributo.getTipoDato().name())
                .unidad(atributo.getUnidad())
                .activo(atributo.getActivo())
                .build();

    }

    public List<AtributoDTO> toDTOList(List<Atributo> atributos){

        return atributos.stream()
                .map(this::toDTO)
                .toList();

    }

    public Atributo toEntity(AtributoDTO dto){

        Atributo atributo = new Atributo();

        atributo.setNombre(dto.getNombre());
        atributo.setTipoDato(
                Atributo.TipoDato.valueOf(dto.getTipoDato()));
        atributo.setUnidad(dto.getUnidad());
        atributo.setActivo(dto.getActivo());

        return atributo;

    }

}
