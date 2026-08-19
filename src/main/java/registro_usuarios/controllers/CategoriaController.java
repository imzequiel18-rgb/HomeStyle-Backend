package registro_usuarios.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.CategoriaDTO;
import registro_usuarios.services.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> guardar(
            @RequestBody CategoriaDTO dto) {

        return ResponseEntity.ok(
                categoriaService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizar(
            @PathVariable Long id,
            @RequestBody CategoriaDTO dto) {

        return ResponseEntity.ok(
                categoriaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        categoriaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                categoriaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listar() {

        return ResponseEntity.ok(
                categoriaService.listar());
    }

}