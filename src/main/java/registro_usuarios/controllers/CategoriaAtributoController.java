package registro_usuarios.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.CategoriaAtributoDTO;
import registro_usuarios.services.CategoriaAtributoService;

import java.util.List;

@RestController
@RequestMapping("/api/categoria-atributos")
@CrossOrigin(origins = "*")
public class CategoriaAtributoController {

    private final CategoriaAtributoService categoriaAtributoService;

    public CategoriaAtributoController(
            CategoriaAtributoService categoriaAtributoService) {

        this.categoriaAtributoService = categoriaAtributoService;
    }

    @PostMapping
    public ResponseEntity<CategoriaAtributoDTO> asignarAtributo(
            @RequestBody CategoriaAtributoDTO dto) {

        return ResponseEntity.ok(
                categoriaAtributoService.asignarAtributo(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaAtributoDTO> actualizar(
            @PathVariable Long id,
            @RequestBody CategoriaAtributoDTO dto) {

        return ResponseEntity.ok(
                categoriaAtributoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        categoriaAtributoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaAtributoDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                categoriaAtributoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaAtributoDTO>> listar() {

        return ResponseEntity.ok(
                categoriaAtributoService.listar());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<CategoriaAtributoDTO>> listarPorCategoria(
            @PathVariable Long categoriaId) {

        return ResponseEntity.ok(
                categoriaAtributoService
                        .listarPorCategoria(categoriaId));
    }

}