package registro_usuarios.services;

import registro_usuarios.dto.*;
import registro_usuarios.entities.Pedido;

import java.util.List;

public interface PedidoService {

    PedidoDTO finalizarCompra();

    MercadoPagoPreferenceDTO iniciarPagoMercadoPago();

    List<PedidoResumenDTO> obtenerMisPedidos();

    PedidoDetalleDTO obtenerDetallePedido(Long pedidoId);

    PedidoDetalleDTO obtenerDetallePedidoAdmin(Long pedidoId);

    List<PedidoAdminDTO> obtenerTodosLosPedidos();

    void actualizarEstadoPedido(Long pedidoId, Pedido.EstadoPedido estado);

    void procesarPagoMercadoPago(Long paymentId);

    void cancelarPedido(Long pedidoId);
}