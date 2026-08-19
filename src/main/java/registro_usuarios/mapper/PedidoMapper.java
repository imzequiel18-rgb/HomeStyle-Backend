package registro_usuarios.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import registro_usuarios.dto.*;
import registro_usuarios.entities.Pedido;

@Component
public class PedidoMapper {

    @Value("${app.backend-url:http://localhost:8080}")
    private String backendUrl;

    //checkout
    public PedidoDTO toDTO(Pedido pedido) {

        PedidoDTO dto = new PedidoDTO();

        dto.setId(pedido.getId());
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado().name());
        dto.setTotal(pedido.getTotal());
        dto.setUsuario(pedido.getUsuario().getUserName());

        return dto;
    }

    //Historial
    public PedidoResumenDTO toResumenDTO(Pedido pedido) {

        return PedidoResumenDTO.builder()
                .id(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .fecha(pedido.getFecha())
                .estado(pedido.getEstado().name())
                .total(pedido.getTotal())
                .metodoPago(pedido.getMetodoPago().name())
                .direccionEnvio(pedido.getDireccionEnvio())
                .build();
    }


    public PedidoDetalleDTO toDetalleDTO(Pedido pedido) {

        return PedidoDetalleDTO.builder()
                .id(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .fecha(pedido.getFecha())
                .estado(pedido.getEstado().name())
                .metodoPago(pedido.getMetodoPago().name())
                .direccionEnvio(pedido.getDireccionEnvio())
                .mercadoPagoPaymentId(pedido.getMercadoPagoPaymentId())
                .mercadoPagoStatus(pedido.getMercadoPagoStatus())
                .mercadoPagoStatusDetail(pedido.getMercadoPagoStatusDetail())
                .total(pedido.getTotal())
                .productos(
                        pedido.getDetalles()
                                .stream()
                                .map(detalle -> DetallePedidoDTO.builder()
                                        .productoId(detalle.getProducto().getId())
                                        .nombreProducto(detalle.getNombreProducto())
                                        .imagen(construirUrlImagen(detalle.getImagenProducto()))
                                        .cantidad(detalle.getCantidad())
                                        .precioUnitario(detalle.getPrecioUnitario())
                                        .subtotal(detalle.getSubtotal())
                                        .build())
                                .toList()
                )
                .build();

    }

    private String construirUrlImagen(String nombreImagen) {

        if (nombreImagen == null || nombreImagen.isBlank()) {
            return null;
        }

        return backendUrl + "/uploads/" + nombreImagen;
    }

    // Administrador
    public PedidoAdminDTO toAdminDTO(Pedido pedido) {

        return PedidoAdminDTO.builder()
                .id(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .cliente(pedido.getUsuario().getUserName())
                .fecha(pedido.getFecha())
                .estado(pedido.getEstado().name())
                .total(pedido.getTotal())
                .metodoPago(pedido.getMetodoPago().name())
                .direccionEnvio(pedido.getDireccionEnvio())
                .build();

    }


}
