package registro_usuarios.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.AtributoDTO;
import registro_usuarios.services.AtributoService;

import java.util.List;

@RestController
@RequestMapping("/api/atributos")
@CrossOrigin(origins = "*")
public class AtributoController {

    private final AtributoService atributoService;

    public AtributoController(AtributoService atributoService) {
        this.atributoService = atributoService;
    }

    @PostMapping
    public ResponseEntity<AtributoDTO> guardar(
            @RequestBody AtributoDTO dto) {

        return ResponseEntity.ok(
                atributoService.guardar(dto));

    }

    @PutMapping("/{id}")
    public ResponseEntity<AtributoDTO> actualizar(
            @PathVariable Long id,
            @RequestBody AtributoDTO dto) {

        return ResponseEntity.ok(
                atributoService.actualizar(id, dto));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        atributoService.eliminar(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<AtributoDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                atributoService.buscarPorId(id));

    }

    @GetMapping
    public ResponseEntity<List<AtributoDTO>> listar() {

        return ResponseEntity.ok(
                atributoService.listar());

    }

    @GetMapping("/activos")
    public ResponseEntity<List<AtributoDTO>> listarActivos() {

        return ResponseEntity.ok(
                atributoService.listarActivos());

    }

}