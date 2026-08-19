package registro_usuarios.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable =false)
    private Producto producto;

    // Fotografía del producto
    @NotBlank
    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name = "sku_producto", nullable = false)
    private String skuProducto;

    @Column(name = "imagen_producto")
    private String imagenProducto;

    @Min(1)
    @Column(nullable = false)
    private Integer cantidad;

    @DecimalMin(value = "0.01")
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @DecimalMin(value = "0.01")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

}