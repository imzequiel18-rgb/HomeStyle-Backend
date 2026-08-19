package registro_usuarios.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "producto_atributos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"producto_id", "atributo_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoAtributo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atributo_id", nullable = false)
    private Atributo atributo;

    @NotBlank(message = "El valor del atributo es obligatorio")
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String valor;

}