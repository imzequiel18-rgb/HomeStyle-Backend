package registro_usuarios.services;

import registro_usuarios.dto.CategoriaAtributoDTO;
import registro_usuarios.dto.ProductoAdminDTO;
import registro_usuarios.dto.ProductoClienteDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductoService {

    ProductoAdminDTO guardar(
            ProductoAdminDTO dto,
            MultipartFile imagen
    );

    ProductoAdminDTO actualizar(
            Long id,
            ProductoAdminDTO dto,
            MultipartFile imagen
    );

    void eliminar(Long id);

    ProductoAdminDTO buscarPorId(Long id);

    ProductoAdminDTO buscarPorSku(String sku);

    List<ProductoAdminDTO> listar();

    List<ProductoAdminDTO> buscarPorCategoria(Long categoriaId);

    List<ProductoAdminDTO> buscarPorMarca(Long marcaId);

    List<ProductoAdminDTO> buscarPorProveedor(Long proveedorId);

    List<ProductoClienteDTO> buscarPorNombre(String nombre);

}