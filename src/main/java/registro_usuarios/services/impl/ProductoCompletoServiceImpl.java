package registro_usuarios.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import registro_usuarios.dto.ProductoAdminDTO;
import registro_usuarios.dto.ProductoAtributoDTO;
import registro_usuarios.dto.ProductoCompletoDTO;
import registro_usuarios.services.ProductoAtributoService;
import registro_usuarios.services.ProductoCompletoService;
import registro_usuarios.services.ProductoService;

@Service
@Transactional
public class ProductoCompletoServiceImpl implements ProductoCompletoService {

    private final ProductoService productoService;

    private final ProductoAtributoService productoAtributoService;

    public ProductoCompletoServiceImpl(
            ProductoService productoService,
            ProductoAtributoService productoAtributoService) {

        this.productoService = productoService;
        this.productoAtributoService = productoAtributoService;

    }


    @Override
    public ProductoCompletoDTO guardarProductoCompleto(
            ProductoCompletoDTO dto,
            MultipartFile imagen) {

        ProductoAdminDTO productoGuardado =
                productoService.guardar(dto.getProducto(),
                        imagen);

        if (dto.getAtributos() != null) {

            for (ProductoAtributoDTO atributo : dto.getAtributos()) {

                atributo.setProductoId(productoGuardado.getId());

                productoAtributoService.guardarValor(atributo);

            }

        }

        dto.setProducto(productoGuardado);

        return dto;

    }


}