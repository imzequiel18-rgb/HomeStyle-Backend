package registro_usuarios.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import registro_usuarios.entities.Pedido;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    List<Pedido> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    Optional<Pedido> findFirstByUsuarioIdAndEstadoAndMetodoPagoOrderByFechaDesc(
            Long usuarioId,
            Pedido.EstadoPedido estado,
            Pedido.MetodoPago metodoPago
    );

    @EntityGraph(attributePaths = "detalles")
    Optional<Pedido> findWithDetallesById(Long id);

    List<Pedido> findAllByOrderByFechaDesc();



    @Query("""
    SELECT COALESCE(SUM(p.total),0)
    FROM Pedido p
    WHERE p.estado IN ('PAGADO','ENVIADO','ENTREGADO')
    """)
    java.math.BigDecimal obtenerVentasTotales();


    @Query("""
    SELECT d.producto.nombre, SUM(d.cantidad)
    FROM DetallePedido d
    GROUP BY d.producto.id, d.producto.nombre
    ORDER BY SUM(d.cantidad) DESC
    """)
    List<Object[]> obtenerProductosMasVendidos();

    @Query("""
    SELECT COUNT(p)
    FROM Pedido p
    """)
    Long obtenerTotalPedidos();


    @Query("""
    SELECT p
    FROM Pedido p
    ORDER BY p.fecha DESC
    """)
    List<Pedido> obtenerUltimosPedidos();
}