package registro_usuarios.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.PedidoAdminDTO;
import registro_usuarios.dto.PedidoDetalleDTO;
import registro_usuarios.entities.Pedido;
import registro_usuarios.services.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pedidos")
@RequiredArgsConstructor
public class AdminPedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoAdminDTO>> obtenerTodosLosPedidos() {

        return ResponseEntity.ok(
                pedidoService.obtenerTodosLosPedidos()
        );

    }


    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestParam Pedido.EstadoPedido estado) {

        try {
            pedidoService.actualizarEstadoPedido(id, estado);
            return ResponseEntity.ok("Estado actualizado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<PedidoDetalleDTO> obtenerDetallePedido(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pedidoService.obtenerDetallePedidoAdmin(id)
        );

    }

}