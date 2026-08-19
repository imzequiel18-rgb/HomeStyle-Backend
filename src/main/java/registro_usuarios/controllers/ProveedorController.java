package registro_usuarios.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import registro_usuarios.dto.ProveedorDTO;
import registro_usuarios.services.ProveedorService;

import java.util.List;


@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {


    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @PostMapping
    public ResponseEntity<ProveedorDTO> guardar(
            @RequestBody ProveedorDTO dto) {

        return ResponseEntity.ok(
                proveedorService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ProveedorDTO dto) {

        return ResponseEntity.ok(
                proveedorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        proveedorService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                proveedorService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> listar() {

        return ResponseEntity.ok(
                proveedorService.listar());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProveedorDTO>> listarActivos() {

        return ResponseEntity.ok(
                proveedorService.listarActivos());
    }

}
