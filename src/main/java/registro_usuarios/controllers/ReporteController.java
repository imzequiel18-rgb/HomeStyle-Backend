package registro_usuarios.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.ReporteDTO;
import registro_usuarios.services.ReporteService;

@RestController
@RequestMapping("/api/admin/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<ReporteDTO> obtenerReporteGeneral() {

        return ResponseEntity.ok(
                reporteService.obtenerReporteGeneral()
        );

    }

}