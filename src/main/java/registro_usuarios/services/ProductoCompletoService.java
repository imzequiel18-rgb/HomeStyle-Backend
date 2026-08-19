package registro_usuarios.services;

import org.springframework.web.multipart.MultipartFile;
import registro_usuarios.dto.ProductoCompletoDTO;

public interface ProductoCompletoService {

    ProductoCompletoDTO guardarProductoCompleto(
            ProductoCompletoDTO dto,
            MultipartFile imagen);

}