package registro_usuarios.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.DireccionDTO;
import registro_usuarios.dto.DireccionRequestDTO;
import registro_usuarios.services.DireccionService;

import java.util.List;

@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService direccionService;

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerMisDirecciones() {

        return ResponseEntity.ok(
                direccionService.obtenerMisDirecciones()
        );

    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> obtenerDireccion(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                direccionService.obtenerDireccion(id)
        );

    }

    @PostMapping
    public ResponseEntity<DireccionDTO> crearDireccion(
            @Valid @RequestBody DireccionRequestDTO request) {


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(direccionService.crearDireccion(request));

    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionDTO> actualizarDireccion(
            @PathVariable Long id,
            @Valid @RequestBody DireccionRequestDTO request) {

        return ResponseEntity.ok(
                direccionService.actualizarDireccion(id, request)
        );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDireccion(
            @PathVariable Long id) {

        direccionService.eliminarDireccion(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}/predeterminada")
    public ResponseEntity<Void> establecerPredeterminada(
            @PathVariable Long id) {

        direccionService.establecerPredeterminada(id);

        return ResponseEntity.ok().build();

    }

}