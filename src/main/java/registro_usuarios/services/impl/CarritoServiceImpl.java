package registro_usuarios.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import registro_usuarios.dto.CarritoDTO;
import registro_usuarios.dto.CarritoItemDTO;
import registro_usuarios.dto.CarritoItemRequestDTO;
import registro_usuarios.entities.Carrito;
import registro_usuarios.entities.CarritoItem;
import registro_usuarios.entities.Producto;
import registro_usuarios.entities.Usuario;
import registro_usuarios.repositories.CarritoItemRepository;
import registro_usuarios.repositories.CarritoRepository;
import registro_usuarios.repositories.ProductoRepository;
import registro_usuarios.repositories.UsuarioRepository;
import registro_usuarios.services.CarritoService;

@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;

    private final CarritoItemRepository carritoItemRepository;

    private final ProductoRepository productoRepository;

    private final UsuarioRepository usuarioRepository;

    public CarritoServiceImpl(
            CarritoRepository carritoRepository,
            CarritoItemRepository carritoItemRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository) {

        this.carritoRepository = carritoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene el usuario autenticado desde el JWT.
     */
    private Usuario obtenerUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));
    }

    /**
     * Obtiene el carrito del usuario o lo crea si aún no existe.
     */
    private Carrito obtenerOCrearCarrito(Usuario usuario) {

        return carritoRepository.findByUsuarioId(usuario.getId())
                .orElseGet(() -> {

                    Carrito carrito = new Carrito();

                    carrito.setUsuario(usuario);

                    return carritoRepository.save(carrito);
                });
    }


    @Override
    public CarritoDTO obtenerCarrito() {

        Usuario usuario = obtenerUsuarioAutenticado();

        Carrito carrito = obtenerOCrearCarrito(usuario);

        return convertirDTO(carrito);
    }

    @Override
    public CarritoDTO agregarProducto(CarritoItemRequestDTO request) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Carrito carrito = obtenerOCrearCarrito(usuario);

        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor que cero.");
        }

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado"));

        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new RuntimeException("El producto no está disponible.");
        }

        CarritoItem item = carritoItemRepository
                .findByCarritoIdAndProductoId(
                        carrito.getId(),
                        producto.getId())
                .orElse(null);


        if (item == null) {

            item = new CarritoItem();

            item.setCarrito(carrito);

            item.setProducto(producto);

            item.setCantidad(request.getCantidad());

            carrito.getItems().add(item);

        } else {

            item.setCantidad(
                    item.getCantidad() + request.getCantidad()
            );

        }

        if (item.getCantidad() > producto.getStock()) {

            throw new RuntimeException(
                    "Stock insuficiente."
            );

        }

        carritoItemRepository.save(item);

        carritoRepository.save(carrito);

        return convertirDTO(carrito);
    }

    @Override
    public CarritoDTO actualizarCantidad(Long productoId,
                                         CarritoItemRequestDTO request) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Carrito carrito = obtenerOCrearCarrito(usuario);

        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor que cero.");
        }

        CarritoItem item = carritoItemRepository
                .findByCarritoIdAndProductoId(
                        carrito.getId(),
                        productoId)
                .orElseThrow(() ->
                        new RuntimeException("El producto no está en el carrito."));

        Producto producto = item.getProducto();

        if (request.getCantidad() > producto.getStock()) {
            throw new RuntimeException("Stock insuficiente.");
        }

        item.setCantidad(request.getCantidad());

        carritoItemRepository.save(item);

        return convertirDTO(carrito);
    }

    @Override
    public CarritoDTO eliminarProducto(Long productoId) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Carrito carrito = obtenerOCrearCarrito(usuario);

        CarritoItem item = carritoItemRepository
                .findByCarritoIdAndProductoId(
                        carrito.getId(),
                        productoId)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado en el carrito."));

        carrito.getItems().remove(item);

        carritoItemRepository.delete(item);

        return convertirDTO(carrito);
    }

    @Override
    public CarritoDTO vaciarCarrito() {

        Usuario usuario = obtenerUsuarioAutenticado();

        Carrito carrito = obtenerOCrearCarrito(usuario);

        carrito.getItems().clear();

        carritoRepository.save(carrito);

        return convertirDTO(carrito);
    }

    /**
     * Convierte la entidad Carrito a CarritoDTO.
     */
    private CarritoDTO convertirDTO(Carrito carrito) {

        if (carrito == null) {
            return new CarritoDTO();
        }

        CarritoDTO carritoDTO = new CarritoDTO();

        carritoDTO.setId(carrito.getId());

        List<CarritoItemDTO> itemsDTO = new ArrayList<>();

        BigDecimal totalCompra = BigDecimal.ZERO;
        int totalProductos = 0;

        for (CarritoItem item : carrito.getItems()) {

            Producto producto = item.getProducto();

            CarritoItemDTO itemDTO = new CarritoItemDTO();

            itemDTO.setProductoId(producto.getId());
            itemDTO.setNombre(producto.getNombre());
            itemDTO.setDescripcion(producto.getDescripcion());
            itemDTO.setPrecioVenta(producto.getPrecioVenta());
            itemDTO.setImagen(producto.getImagen());
            itemDTO.setStock(producto.getStock());
            itemDTO.setCantidad(item.getCantidad());

            itemsDTO.add(itemDTO);

            totalCompra = totalCompra.add(
                    producto.getPrecioVenta()
                            .multiply(BigDecimal.valueOf(item.getCantidad()))
            );


            totalProductos += item.getCantidad();
        }

        carritoDTO.setItems(itemsDTO);
        carritoDTO.setTotalCompra(totalCompra.doubleValue());
        carritoDTO.setTotalProductos(totalProductos);

        return carritoDTO;
    }



}