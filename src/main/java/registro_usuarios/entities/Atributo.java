package registro_usuarios.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "atributos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Atributo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del atributo es obligatorio")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dato", nullable = false)
    private TipoDato tipoDato;

    @Column(length = 30)
    private String unidad;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "atributo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoriaAtributo> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "atributo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoAtributo> productos = new ArrayList<>();

    /**
     * Tipos de datos permitidos para un atributo.
     */
    public enum TipoDato {
        TEXTO,
        NUMERO,
        DECIMAL,
        BOOLEANO,
        FECHA,
        SELECCION
    }
}