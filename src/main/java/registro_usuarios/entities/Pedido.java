package registro_usuarios.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cliente que realizó el pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Número visible para el cliente
    @Column(name = "numero_pedido", nullable = false, unique = true, length = 20)
    private String numeroPedido;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Builder.Default
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Builder.Default
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal iva = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "direccion_envio", length = 255)
    private String direccionEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "mercado_pago_preference_id", length = 100)
    private String mercadoPagoPreferenceId;

    @Column(name = "mercado_pago_payment_id", length = 100)
    private String mercadoPagoPaymentId;

    @Column(name = "mercado_pago_status", length = 40)
    private String mercadoPagoStatus;

    @Column(name = "mercado_pago_status_detail", length = 100)
    private String mercadoPagoStatusDetail;


    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DetallePedido> detalles = new ArrayList<>();

    public enum EstadoPedido {
        PENDIENTE,
        PAGADO,
        ENVIADO,
        ENTREGADO,
        CANCELADO
    }

    public enum MetodoPago {
        EFECTIVO,
        TARJETA,
        TRANSFERENCIA,
        MERCADO_PAGO
    }
}