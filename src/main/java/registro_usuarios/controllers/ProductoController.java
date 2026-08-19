package registro_usuarios.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.ProductoAdminDTO;
import registro_usuarios.dto.ProductoClienteDTO;
import registro_usuarios.services.ProductoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoAdminDTO> guardar(

            @RequestPart("producto") ProductoAdminDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        return ResponseEntity.ok(
                productoService.guardar(dto, imagen)
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoAdminDTO> actualizar(

            @PathVariable Long id,

            @RequestPart("producto") ProductoAdminDTO dto,

            @RequestPart(value = "imagen", required = false) MultipartFile imagen

    ) {

        return ResponseEntity.ok(
                productoService.actualizar(id, dto, imagen)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        productoService.eliminar(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoAdminDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productoService.buscarPorId(id));

    }

    @GetMapping
    public ResponseEntity<List<ProductoAdminDTO>> listar() {

        return ResponseEntity.ok(
                productoService.listar());

    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductoAdminDTO> buscarPorSku(
            @PathVariable String sku) {

        return ResponseEntity.ok(
                productoService.buscarPorSku(sku));

    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoAdminDTO>> buscarPorCategoria(
            @PathVariable Long categoriaId) {

        return ResponseEntity.ok(
                productoService.buscarPorCategoria(categoriaId));

    }

    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<ProductoAdminDTO>> buscarPorMarca(
            @PathVariable Long marcaId) {

        return ResponseEntity.ok(
                productoService.buscarPorMarca(marcaId));

    }

    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<ProductoAdminDTO>> buscarPorProveedor(
            @PathVariable Long proveedorId) {

        return ResponseEntity.ok(
                productoService.buscarPorProveedor(proveedorId));

    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoClienteDTO>> buscarPorNombre(
            @RequestParam String nombre) {

        return ResponseEntity.ok(
                productoService.buscarPorNombre(nombre));

    }

}