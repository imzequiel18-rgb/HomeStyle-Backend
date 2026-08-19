package registro_usuarios.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "direcciones")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private String nombreDestinatario;

    @Column(nullable = false)
    private String telefonoContacto;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String numeroExterior;

    private String numeroInterior;

    @Column(nullable = false)
    private String colonia;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String codigoPostal;

    private String referencias;

    private Double latitud;

    private Double longitud;

    @Builder.Default
    private Boolean predeterminada = false;

    @Builder.Default
    private Boolean activo = true;

}