package registro_usuarios.services;

import registro_usuarios.dto.CarritoDTO;
import registro_usuarios.dto.CarritoItemRequestDTO;

public interface CarritoService {

    CarritoDTO obtenerCarrito();

    CarritoDTO agregarProducto(CarritoItemRequestDTO request);

    CarritoDTO actualizarCantidad(Long productoId,
                                  CarritoItemRequestDTO request);

    CarritoDTO eliminarProducto(Long productoId);

    CarritoDTO vaciarCarrito();

}