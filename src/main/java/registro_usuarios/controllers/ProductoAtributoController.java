package registro_usuarios.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.ProductoAtributoDTO;
import registro_usuarios.services.ProductoAtributoService;

import java.util.List;

@RestController
@RequestMapping("/api/producto-atributos")
@CrossOrigin(origins = "*")
public class ProductoAtributoController {

    private final ProductoAtributoService productoAtributoService;

    public ProductoAtributoController(
            ProductoAtributoService productoAtributoService) {

        this.productoAtributoService = productoAtributoService;
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<ProductoAtributoDTO>> listarPorProducto(
            @PathVariable Long productoId) {

        return ResponseEntity.ok(
                productoAtributoService.listarPorProducto(productoId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoAtributoDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productoAtributoService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoAtributoDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ProductoAtributoDTO dto) {

        return ResponseEntity.ok(
                productoAtributoService.actualizarValor(id, dto)
        );
    }

    @PostMapping
    public ResponseEntity<ProductoAtributoDTO> guardar(
            @RequestBody ProductoAtributoDTO dto) {


        return ResponseEntity.ok(
                productoAtributoService.guardarValor(dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        productoAtributoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/producto/{productoId}")
    public ResponseEntity<List<ProductoAtributoDTO>> reemplazarPorProducto(
            @PathVariable Long productoId,
            @RequestBody List<ProductoAtributoDTO> atributos) {

        return ResponseEntity.ok(
                productoAtributoService
                        .reemplazarPorProducto(productoId, atributos)
        );
    }

}