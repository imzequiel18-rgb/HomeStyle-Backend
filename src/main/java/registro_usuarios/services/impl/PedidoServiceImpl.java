package registro_usuarios.services.impl;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentRefundClient;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import registro_usuarios.dto.*;
import registro_usuarios.entities.*;
import registro_usuarios.exceptions.BadRequestException;
import registro_usuarios.mapper.PedidoMapper;
import registro_usuarios.repositories.*;
import registro_usuarios.services.MercadoPagoService;
import registro_usuarios.services.PedidoService;

import java.util.List;
import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    @Value("${mercadopago.mode:test}")
    private String mercadoPagoMode;


    private final CarritoRepository carritoRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;
    private final DireccionRepository direccionRepository;
    private final PedidoMapper pedidoMapper;
    private final MercadoPagoService mercadoPagoService;




    @Override
    @Transactional
    public PedidoDTO finalizarCompra() {

        Usuario usuario = obtenerUsuarioAutenticado();

        Carrito carrito = obtenerCarrito(usuario.getId());

        validarCarrito(carrito);

        validarStock(carrito);

        Direccion direccionEnvio = obtenerDireccionEnvio(carrito.getUsuario());

        Pedido pedido = crearPedido(carrito, direccionEnvio);

        crearDetalles(pedido, carrito);

        actualizarInventario(carrito);

        registrarMovimientos(carrito);

        vaciarCarrito(carrito);

        return pedidoMapper.toDTO(pedido);
    }

    private Carrito obtenerCarrito(Long usuarioId) {

        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("El usuario no tiene un carrito."));
    }


    private void validarCarrito(Carrito carrito) {

        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío.");
        }

    }

    private void validarStock(Carrito carrito) {

        for (CarritoItem item : carrito.getItems()) {

            Producto producto = item.getProducto();

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException(
                        "Stock insuficiente para el producto: "
                                + producto.getNombre()
                );
            }

        }

    }


    private Pedido crearPedido(Carrito carrito, Direccion direccion) {

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CarritoItem item : carrito.getItems()) {

            BigDecimal precio = item.getProducto().getPrecioVenta();

            BigDecimal cantidad = BigDecimal.valueOf(item.getCantidad());

            subtotal = subtotal.add(
                    precio.multiply(cantidad)
            );

        }

        Pedido pedido = Pedido.builder()
                .usuario(carrito.getUsuario())
                .numeroPedido(generarNumeroPedido())
                .subtotal(subtotal)
                .descuento(BigDecimal.ZERO)
                .iva(BigDecimal.ZERO)
                .total(subtotal)
                .direccionEnvio(formatearDireccion(direccion))
                .metodoPago(Pedido.MetodoPago.EFECTIVO)
                .estado(Pedido.EstadoPedido.PENDIENTE)
                .build();

        return pedidoRepository.save(pedido);

    }

    private void crearDetalles(Pedido pedido, Carrito carrito) {

        for (CarritoItem item : carrito.getItems()) {

            Producto producto = item.getProducto();

            BigDecimal subtotal = producto.getPrecioVenta()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));

            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .nombreProducto(producto.getNombre())
                    .skuProducto(producto.getSku())
                    .imagenProducto(producto.getImagen())
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecioVenta())
                    .subtotal(subtotal)
                    .build();

            // Guardar en la base de datos
            detallePedidoRepository.save(detalle);

            // IMPORTANTE: agregarlo también a la lista del pedido
            pedido.getDetalles().add(detalle);
        }
    }

    private void actualizarInventario(Carrito carrito) {

        for (CarritoItem item : carrito.getItems()) {

            Producto producto = item.getProducto();

            producto.setStock(
                    producto.getStock() - item.getCantidad()
            );

            productoRepository.save(producto);

        }

    }

    private String generarNumeroPedido() {

        return "HS-"
                + System.currentTimeMillis();

    }

    private void registrarMovimientos(Carrito carrito) {

        Usuario usuario = carrito.getUsuario();

        for (CarritoItem item : carrito.getItems()) {

            Producto producto = item.getProducto();

            MovimientoInventario movimiento = MovimientoInventario.builder()
                    .producto(producto)
                    .usuario(usuario)
                    .tipo(MovimientoInventario.TipoMovimiento.SALIDA)
                    .origen(MovimientoInventario.OrigenMovimiento.VENTA)
                    .cantidad(item.getCantidad())
                    .stockAnterior(producto.getStock() + item.getCantidad())
                    .stockNuevo(producto.getStock())
                    .motivo("Venta realizada")
                    .build();

            movimientoInventarioRepository.save(movimiento);

        }

    }

    private void vaciarCarrito(Carrito carrito) {

        carrito.getItems().clear();

        carritoRepository.save(carrito);

    }

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

    //cliente
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumenDTO> obtenerMisPedidos() {

        Usuario usuario = obtenerUsuarioAutenticado();

        List<Pedido> pedidos =
                pedidoRepository.findByUsuarioIdOrderByFechaDesc(usuario.getId());

        return pedidos.stream()
                .map(pedidoMapper::toResumenDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoDetalleDTO obtenerDetallePedido(Long pedidoId) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Pedido pedido = pedidoRepository.findWithDetallesById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para consultar este pedido.");
        }

        return pedidoMapper.toDetalleDTO(pedido);
    }


    //administrados
    @Override
    @Transactional(readOnly = true)
    public List<PedidoAdminDTO> obtenerTodosLosPedidos() {

        return pedidoRepository
                .findAllByOrderByFechaDesc()
                .stream()
                .map(pedidoMapper::toAdminDTO)
                .toList();

    }


    @Override
    @Transactional
    public void actualizarEstadoPedido(Long pedidoId,
                                       Pedido.EstadoPedido nuevoEstado) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new RuntimeException("Pedido no encontrado"));

        Pedido.EstadoPedido estadoActual = pedido.getEstado();

        if (!esTransicionValida(estadoActual, nuevoEstado)) {
            throw new RuntimeException(
                    "No se puede cambiar de " +
                            estadoActual +
                            " a " +
                            nuevoEstado
            );
        }

        pedido.setEstado(nuevoEstado);

        pedidoRepository.save(pedido);
    }

    private boolean esTransicionValida(
            Pedido.EstadoPedido actual,
            Pedido.EstadoPedido nuevo) {

        return switch (actual) {

            case PENDIENTE ->
                    nuevo == Pedido.EstadoPedido.PAGADO
                            || nuevo == Pedido.EstadoPedido.CANCELADO;

            case PAGADO ->
                    nuevo == Pedido.EstadoPedido.ENVIADO;

            case ENVIADO ->
                    nuevo == Pedido.EstadoPedido.ENTREGADO;

            case ENTREGADO,
                 CANCELADO -> false;
        };
    }


    @Transactional(readOnly = true)
    public PedidoDetalleDTO obtenerDetallePedidoAdmin(Long pedidoId) {

        Pedido pedido = pedidoRepository.findWithDetallesById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        return pedidoMapper.toDetalleDTO(pedido);
    }

    @Override
    @Transactional
    public MercadoPagoPreferenceDTO iniciarPagoMercadoPago() {

        Usuario usuario = obtenerUsuarioAutenticado();

        Carrito carrito = obtenerCarrito(usuario.getId());

        validarCarrito(carrito);

        validarStock(carrito);

        Direccion direccionEnvio = obtenerDireccionEnvio(usuario);

        /*
         * Buscamos si el usuario ya tiene un pedido pendiente
         * creado mediante Mercado Pago.
         *
         * Esto evita crear un pedido nuevo cada vez que el
         * usuario vuelve a presionar "Pagar con Mercado Pago".
         */
        Pedido pedido = pedidoRepository
                .findFirstByUsuarioIdAndEstadoAndMetodoPagoOrderByFechaDesc(
                        usuario.getId(),
                        Pedido.EstadoPedido.PENDIENTE,
                        Pedido.MetodoPago.MERCADO_PAGO
                )
                .orElse(null);

        /*
         * Si no existe un pedido pendiente, creamos uno nuevo
         * y copiamos los productos del carrito.
         */
        if (pedido == null) {

            pedido = crearPedidoMercadoPago(carrito, direccionEnvio);

            crearDetalles(pedido, carrito);

        } else {

            /*
             * Si ya existe un pedido pendiente, verificamos que
             * corresponda al carrito actual.
             */
            if (!pedidoCoincideConCarrito(pedido, carrito)) {

                /*
                 * El usuario cambió el carrito después de crear
                 * el pedido anterior.
                 *
                 * Cancelamos el pedido pendiente anterior y
                 * creamos uno nuevo con el carrito actual.
                 */
                pedido.setEstado(Pedido.EstadoPedido.CANCELADO);

                pedidoRepository.save(pedido);
                mercadoPagoService.invalidarPreferencia(
                        pedido.getMercadoPagoPreferenceId()
                );

                pedido = crearPedidoMercadoPago(carrito, direccionEnvio);

                crearDetalles(pedido, carrito);
            }
        }

        /*
         * Creamos una nueva preferencia para el pedido.
         *
         * Aunque se vuelva a intentar el pago, NO se crea otro
         * pedido en nuestra base de datos.
         */
        var preference =
                mercadoPagoService.crearPreferencia(pedido);

        pedido.setMercadoPagoPreferenceId(preference.getId());

        pedidoRepository.save(pedido);

        return MercadoPagoPreferenceDTO.builder()
                .pedidoId(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .preferenceId(preference.getId())
                .checkoutUrl("production".equalsIgnoreCase(mercadoPagoMode)
                        ? preference.getInitPoint()
                        : preference.getSandboxInitPoint())
                .build();
    }

    @Override
    @Transactional
    public void procesarPagoMercadoPago(Long paymentId) {

        try {

            PaymentClient paymentClient = new PaymentClient();

            Payment payment = paymentClient.get(paymentId);

            System.out.println();
            System.out.println("==============================================");
            System.out.println("       NOTIFICACIÓN DE MERCADO PAGO");
            System.out.println("==============================================");
            System.out.println("Payment ID: " + payment.getId());
            System.out.println("Status: " + payment.getStatus());
            System.out.println("Status detail: " + payment.getStatusDetail());
            System.out.println("External reference: " + payment.getExternalReference());
            System.out.println("==============================================");
            System.out.println();

            String numeroPedidoNotificacion = payment.getExternalReference();
            if (numeroPedidoNotificacion != null && !numeroPedidoNotificacion.isBlank()) {
                pedidoRepository.findByNumeroPedido(numeroPedidoNotificacion).ifPresent(p -> {
                    p.setMercadoPagoPaymentId(String.valueOf(payment.getId()));
                    p.setMercadoPagoStatus(payment.getStatus());
                    p.setMercadoPagoStatusDetail(payment.getStatusDetail());
                    pedidoRepository.save(p);
                });
            }

            /*
             * Solamente procesamos pagos aprobados.
             */
            if (!"approved".equalsIgnoreCase(payment.getStatus())) {

                System.out.println(
                        "El pago todavía no está aprobado. Status: "
                                + payment.getStatus()
                );

                return;
            }

            /*
             * Mercado Pago devuelve como external_reference
             * nuestro numeroPedido.
             */
            String numeroPedido = payment.getExternalReference();

            if (numeroPedido == null || numeroPedido.isBlank()) {

                throw new RuntimeException(
                        "El pago de Mercado Pago no contiene external_reference."
                );
            }

            /*
             * Buscamos el pedido que creó HomeStyle.
             */
            Pedido pedido = pedidoRepository
                    .findByNumeroPedido(numeroPedido)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "No se encontró el pedido: "
                                            + numeroPedido
                            )
                    );

            /*
             * IMPORTANTE:
             *
             * Si Mercado Pago manda nuevamente la misma
             * notificación, no volvemos a procesar el pedido.
             */
            if (pedido.getEstado() == Pedido.EstadoPedido.PAGADO) {

                System.out.println(
                        "El pedido " + numeroPedido
                                + " ya fue procesado anteriormente."
                );

                return;
            }

            /*
             * Si el cliente canceló el pedido pero alcanzó a pagar usando
             * una pestaña o enlace viejo de Mercado Pago, reembolsamos el
             * pago aprobado de inmediato y conservamos el pedido CANCELADO.
             */
            if (pedido.getEstado() == Pedido.EstadoPedido.CANCELADO) {
                System.out.println(
                        "Se recibió un pago aprobado para el pedido cancelado "
                                + numeroPedido + ". Se intentará reembolsar."
                );

                PaymentRefundClient refundClient = new PaymentRefundClient();
                refundClient.refund(payment.getId());

                System.out.println(
                        "Pago " + payment.getId()
                                + " reembolsado porque el pedido estaba cancelado."
                );
                return;
            }

            /*
             * Solamente permitimos procesar pedidos pendientes.
             */
            if (pedido.getEstado() != Pedido.EstadoPedido.PENDIENTE) {

                System.out.println(
                        "El pedido " + numeroPedido
                                + " no está pendiente. Estado actual: "
                                + pedido.getEstado()
                );

                return;
            }

            /*
             * Verificamos que el total recibido por Mercado Pago
             * coincida con el total del pedido.
             */
            if (payment.getTransactionAmount() == null ||
                    payment.getTransactionAmount()
                            .compareTo(pedido.getTotal()) != 0) {

                throw new RuntimeException(
                        "El monto del pago no coincide con el pedido."
                );
            }

            /*
             * Buscamos el carrito del usuario.
             */
            Carrito carrito = obtenerCarrito(
                    pedido.getUsuario().getId()
            );

            /*
             * Verificamos que todavía haya productos.
             */
            if (carrito.getItems().isEmpty()) {

                /*
                 * Esto puede ocurrir si otra operación ya vació
                 * el carrito.
                 *
                 * Marcamos el pedido como pagado solamente si
                 * el pedido todavía está pendiente.
                 */
                pedido.setEstado(Pedido.EstadoPedido.PAGADO);

                pedido.setMetodoPago(
                        Pedido.MetodoPago.MERCADO_PAGO
                );

                pedidoRepository.save(pedido);

                return;
            }

            /*
             * Verificamos nuevamente el stock antes de descontarlo.
             */
            validarStock(carrito);

            /*
             * Marcamos el pedido como PAGADO.
             */
            pedido.setEstado(Pedido.EstadoPedido.PAGADO);

            pedido.setMetodoPago(
                    Pedido.MetodoPago.MERCADO_PAGO
            );

            pedidoRepository.save(pedido);

            /*
             * Ahora sí realizamos las operaciones definitivas:
             *
             * 1. Descontar inventario.
             * 2. Registrar movimientos.
             * 3. Vaciar carrito.
             */
            actualizarInventario(carrito);

            registrarMovimientos(carrito);

            vaciarCarrito(carrito);

            System.out.println(
                    "Pedido " + numeroPedido
                            + " procesado correctamente."
            );

        } catch (MPApiException e) {

            System.out.println(
                    "Error de API de Mercado Pago: "
                            + e.getApiResponse().getContent()
            );

            throw new RuntimeException(
                    "No fue posible consultar el pago de Mercado Pago.",
                    e
            );

        } catch (MPException e) {

            System.out.println(
                    "Error del SDK de Mercado Pago: "
                            + e.getMessage()
            );

            throw new RuntimeException(
                    "No fue posible consultar el pago de Mercado Pago.",
                    e
            );
        }
    }

    private Pedido crearPedidoMercadoPago(Carrito carrito, Direccion direccion) {

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CarritoItem item : carrito.getItems()) {

            BigDecimal precio =
                    item.getProducto().getPrecioVenta();

            BigDecimal cantidad =
                    BigDecimal.valueOf(item.getCantidad());

            subtotal = subtotal.add(
                    precio.multiply(cantidad)
            );
        }

        Pedido pedido = Pedido.builder()
                .usuario(carrito.getUsuario())
                .numeroPedido(generarNumeroPedido())
                .subtotal(subtotal)
                .descuento(BigDecimal.ZERO)
                .iva(BigDecimal.ZERO)
                .total(subtotal)
                .direccionEnvio(formatearDireccion(direccion))
                .metodoPago(Pedido.MetodoPago.MERCADO_PAGO)
                .estado(Pedido.EstadoPedido.PENDIENTE)
                .build();

        return pedidoRepository.save(pedido);
    }



    /**
     * Obtiene la dirección que se copiará al pedido.
     * Se prioriza la dirección predeterminada y, si por algún motivo
     * no existe una marcada, se usa la primera dirección activa.
     */
    private Direccion obtenerDireccionEnvio(Usuario usuario) {

        return direccionRepository
                .findByUsuarioAndPredeterminadaTrueAndActivoTrue(usuario)
                .orElseGet(() -> direccionRepository
                        .findByUsuarioAndActivoTrue(usuario)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new BadRequestException(
                                "Debes registrar una dirección de envío antes de realizar tu compra."
                        ))
                );
    }

    /**
     * Guarda una copia legible de la dirección dentro del pedido.
     * Así, si el cliente modifica su dirección después, el pedido
     * conserva exactamente la dirección utilizada al comprar.
     */
    private String formatearDireccion(Direccion direccion) {

        String interior = direccion.getNumeroInterior() != null
                && !direccion.getNumeroInterior().isBlank()
                ? ", Int. " + direccion.getNumeroInterior()
                : "";

        String referencias = direccion.getReferencias() != null
                && !direccion.getReferencias().isBlank()
                ? ". Referencias: " + direccion.getReferencias()
                : "";

        return direccion.getNombreDestinatario()
                + " | " + direccion.getCalle()
                + " " + direccion.getNumeroExterior()
                + interior
                + ", Col. " + direccion.getColonia()
                + ", " + direccion.getCiudad()
                + ", " + direccion.getEstado()
                + ", C.P. " + direccion.getCodigoPostal()
                + " | Tel. " + direccion.getTelefonoContacto()
                + referencias;
    }

    private boolean pedidoCoincideConCarrito(
            Pedido pedido,
            Carrito carrito
    ) {

        if (pedido.getDetalles().size() != carrito.getItems().size()) {
            return false;
        }

        for (CarritoItem item : carrito.getItems()) {

            boolean encontrado = pedido.getDetalles()
                    .stream()
                    .anyMatch(detalle ->
                            detalle.getProducto().getId()
                                    .equals(item.getProducto().getId())
                                    &&
                                    detalle.getCantidad() == item.getCantidad()
                    );

            if (!encontrado) {
                return false;
            }
        }

        return true;
    }

    @Override
    @Transactional
    public void cancelarPedido(Long pedidoId) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new RuntimeException("Pedido no encontrado"));

        // Verificar que el pedido pertenezca al usuario
        if (!pedido.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException(
                    "No tienes permiso para cancelar este pedido."
            );
        }

        // Solo se pueden cancelar pedidos pendientes
        if (pedido.getEstado() != Pedido.EstadoPedido.PENDIENTE) {
            throw new RuntimeException(
                    "Solo se pueden cancelar pedidos pendientes."
            );
        }

        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);

        pedidoRepository.save(pedido);

        /*
         * Si este pedido tenía una preferencia de Checkout Pro, intentamos
         * invalidarla para que el enlace de pago anterior deje de ser útil.
         * Si Mercado Pago rechaza la actualización, la cancelación local se
         * conserva y el webhook reembolsará cualquier pago tardío aprobado.
         */
        mercadoPagoService.invalidarPreferencia(
                pedido.getMercadoPagoPreferenceId()
        );
    }

}