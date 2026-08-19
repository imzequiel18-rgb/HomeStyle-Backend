package registro_usuarios.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "categoria_atributos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"categoria_id", "atributo_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaAtributo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atributo_id", nullable = false)
    private Atributo atributo;

    @Column(nullable = false)
    @Builder.Default
    private Boolean obligatorio = false;

    @Column(name = "orden_visualizacion", nullable = false)
    @Builder.Default
    private Integer orden = 1;

}