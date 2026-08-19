package registro_usuarios.services;

import registro_usuarios.dto.ProductoAtributoDTO;

import java.util.List;

public interface ProductoAtributoService {

    ProductoAtributoDTO guardarValor(ProductoAtributoDTO dto);

    ProductoAtributoDTO actualizarValor(Long id,
                                        ProductoAtributoDTO dto);

    ProductoAtributoDTO buscarPorId(Long id);

    List<ProductoAtributoDTO> listarPorProducto(Long productoId);

    List<ProductoAtributoDTO> reemplazarPorProducto(
            Long productoId,
            List<ProductoAtributoDTO> atributos);

    void eliminar(Long id);
}