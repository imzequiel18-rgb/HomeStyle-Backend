package registro_usuarios.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import registro_usuarios.dto.CarritoDTO;
import registro_usuarios.dto.CarritoItemRequestDTO;
import registro_usuarios.services.CarritoService;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    /**
     * Obtener carrito del usuario autenticado
     */
    @GetMapping
    public ResponseEntity<CarritoDTO> obtenerCarrito() {

        return ResponseEntity.ok(
                carritoService.obtenerCarrito()
        );
    }

    /**
     * Agregar producto al carrito
     */
    @PostMapping("/items")
    public ResponseEntity<CarritoDTO> agregarProducto(
            @RequestBody CarritoItemRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(carritoService.agregarProducto(request));
    }

    /**
     * Actualizar cantidad
     */
    @PutMapping("/items/{productoId}")
    public ResponseEntity<CarritoDTO> actualizarCantidad(
            @PathVariable Long productoId,
            @RequestBody CarritoItemRequestDTO request) {

        return ResponseEntity.ok(
                carritoService.actualizarCantidad(
                        productoId,
                        request
                )
        );
    }

    /**
     * Eliminar producto
     */
    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<CarritoDTO> eliminarProducto(
            @PathVariable Long productoId) {

        return ResponseEntity.ok(
                carritoService.eliminarProducto(productoId)
        );
    }

    /**
     * Vaciar carrito
     */
    @DeleteMapping
    public ResponseEntity<CarritoDTO> vaciarCarrito() {

        return ResponseEntity.ok(
                carritoService.vaciarCarrito()
        );
    }

}