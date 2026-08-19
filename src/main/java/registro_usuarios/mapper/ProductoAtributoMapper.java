package registro_usuarios.mapper;

import org.springframework.stereotype.Component;
import registro_usuarios.dto.ProductoAtributoDTO;
import registro_usuarios.entities.ProductoAtributo;

import java.util.List;

@Component
public class ProductoAtributoMapper {


    public ProductoAtributoDTO toDTO(ProductoAtributo productoAtributo) {

        if (productoAtributo == null) {
            return null;
        }

        return ProductoAtributoDTO.builder()
                .id(productoAtributo.getId())
                .atributoId(
                        productoAtributo.getAtributo() != null
                                ? productoAtributo.getAtributo().getId()
                                : null
                )
                .atributoNombre(
                        productoAtributo.getAtributo() != null
                                ? productoAtributo.getAtributo().getNombre()
                                : null
                )
                .valor(productoAtributo.getValor())
                .unidad(
                        productoAtributo.getAtributo() != null
                                ? productoAtributo.getAtributo().getUnidad()
                                : null
                )
                .productoId(
                        productoAtributo.getProducto() != null
                                ? productoAtributo.getProducto().getId()
                                : null
                )
                .build();
    }

    public List<ProductoAtributoDTO> toDTOList(List<ProductoAtributo> lista) {

        return lista.stream()
                .map(this::toDTO)
                .toList();

    }

    public ProductoAtributo toEntity(ProductoAtributoDTO dto) {

        ProductoAtributo productoAtributo = new ProductoAtributo();

        productoAtributo.setValor(dto.getValor());

        return productoAtributo;

    }

}