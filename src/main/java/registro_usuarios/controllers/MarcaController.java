package registro_usuarios.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.MarcaDTO;
import registro_usuarios.services.MarcaService;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "*")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @PostMapping
    public ResponseEntity<MarcaDTO> guardar(
            @RequestBody MarcaDTO dto) {

        return ResponseEntity.ok(
                marcaService.guardar(dto));

    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcaDTO> actualizar(
            @PathVariable Long id,
            @RequestBody MarcaDTO dto) {

        return ResponseEntity.ok(
                marcaService.actualizar(id, dto));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        marcaService.eliminar(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                marcaService.buscarPorId(id));

    }

    @GetMapping
    public ResponseEntity<List<MarcaDTO>> listar() {

        return ResponseEntity.ok(
                marcaService.listar());

    }

}