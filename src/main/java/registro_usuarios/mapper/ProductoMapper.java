package registro_usuarios.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import registro_usuarios.dto.ProductoAdminDTO;
import registro_usuarios.dto.ProductoClienteDTO;
import registro_usuarios.dto.ProductoAtributoDTO;
import registro_usuarios.entities.Producto;
import registro_usuarios.entities.ProductoAtributo;

import java.util.List;

@Component
public class ProductoMapper {

    @Value("${app.backend-url:http://localhost:8080}")
    private String backendUrl;

    public ProductoAdminDTO toAdminDTO(Producto producto) {

        if (producto == null) {
            return null;
        }

        ProductoAdminDTO dto = ProductoAdminDTO.builder()
                .id(producto.getId())
                .sku(producto.getSku())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precioCosto(producto.getPrecioCosto())
                .precioVenta(producto.getPrecioVenta())
                .stock(producto.getStock())
                .activo(producto.getActivo())
                .imagen(construirUrlImagen(producto.getImagen()))
                .categoriaId(producto.getCategoria().getId())
                .categoriaNombre(producto.getCategoria().getNombre())
                .marcaId(producto.getMarca().getId())
                .marcaNombre(producto.getMarca().getNombre())
                .proveedorId(producto.getProveedor().getId())
                .proveedorNombre(producto.getProveedor().getNombre())
                .build();

        if (producto.getAtributos() != null) {

            dto.setAtributos(

                    producto.getAtributos()

                            .stream()

                            .map(this::toProductoAtributoDTO)

                            .toList()
            );
        }

        return dto;
    }

    public ProductoClienteDTO toClienteDTO(Producto producto) {

        if (producto == null) {
            return null;
        }

        ProductoClienteDTO dto = ProductoClienteDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precioVenta(producto.getPrecioVenta())
                .imagen(construirUrlImagen(producto.getImagen()))
                .categoriaId(producto.getCategoria().getId())
                .categoriaNombre(producto.getCategoria().getNombre())
                .marcaNombre(producto.getMarca().getNombre())
                .build();

        if (producto.getAtributos() != null) {

            dto.setAtributos(

                    producto.getAtributos()

                            .stream()

                            .map(this::toProductoAtributoDTO)

                            .toList()
            );
        }

        return dto;
    }

    private ProductoAtributoDTO toProductoAtributoDTO(ProductoAtributo atributo) {

        return ProductoAtributoDTO.builder()

                .atributoId(atributo.getAtributo().getId())

                .atributoNombre(atributo.getAtributo().getNombre())

                .unidad(atributo.getAtributo().getUnidad())

                .valor(atributo.getValor())

                .build();

    }

    public List<ProductoAdminDTO> toAdminDTOList(List<Producto> productos) {

        return productos.stream()
                .map(this::toAdminDTO)
                .toList();

    }

    public List<ProductoClienteDTO> toClienteDTOList(List<Producto> productos) {

        return productos.stream()
                .map(this::toClienteDTO)
                .toList();

    }

    public Producto toEntity(ProductoAdminDTO dto) {

        Producto producto = new Producto();

        producto.setSku(dto.getSku());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioCosto(dto.getPrecioCosto());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo());
        producto.setImagen(dto.getImagen());

        return producto;

    }

    private String construirUrlImagen(String nombreImagen) {

        if (nombreImagen == null || nombreImagen.isBlank()) {
            return null;
        }

        return backendUrl + "/uploads/" + nombreImagen;
    }

}
