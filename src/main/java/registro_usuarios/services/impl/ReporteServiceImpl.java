package registro_usuarios.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import registro_usuarios.dto.*;
import registro_usuarios.entities.Pedido;
import registro_usuarios.entities.Producto;
import registro_usuarios.repositories.PedidoRepository;
import registro_usuarios.repositories.ProductoRepository;
import registro_usuarios.repositories.UsuarioRepository;
import registro_usuarios.services.ReporteService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ReporteDTO obtenerReporteGeneral() {

        ReporteDTO reporte = new ReporteDTO();

        reporte.setVentasTotales(
                pedidoRepository.obtenerVentasTotales()
        );

        reporte.setTotalPedidos(
                pedidoRepository.obtenerTotalPedidos()
        );

        reporte.setTotalClientes(
                usuarioRepository.contarClientes()
        );

        reporte.setTotalProductos(
                productoRepository.count()
        );

        reporte.setProductosStockBajo(
                obtenerStockBajo()
        );

        reporte.setProductosMasVendidos(
                obtenerMasVendidos()
        );

        reporte.setUltimosPedidos(
                obtenerUltimosPedidos()
        );

        return reporte;
    }

    private List<ProductoStockDTO> obtenerStockBajo() {

        return productoRepository
                .obtenerProductosConMenorStock()
                .stream()
                .limit(5)
                .map(p -> new ProductoStockDTO(
                        p.getNombre(),
                        p.getStock()
                ))
                .collect(Collectors.toList());

    }

    private List<ProductoVendidoDTO> obtenerMasVendidos() {

        return pedidoRepository
                .obtenerProductosMasVendidos()
                .stream()
                .limit(5)
                .map(obj -> new ProductoVendidoDTO(
                        (String) obj[0],
                        ((Number) obj[1]).longValue()
                ))
                .collect(Collectors.toList());

    }

    private List<PedidoRecienteDTO> obtenerUltimosPedidos() {

        return pedidoRepository
                .obtenerUltimosPedidos()
                .stream()
                .limit(5)
                .map(this::mapPedido)
                .collect(Collectors.toList());

    }

    private PedidoRecienteDTO mapPedido(Pedido pedido) {

        return new PedidoRecienteDTO(
                pedido.getNumeroPedido(),
                pedido.getUsuario().getUserName(),
                pedido.getFecha(),
                pedido.getEstado().name(),
                pedido.getTotal()
        );

    }

}