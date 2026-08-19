package registro_usuarios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import registro_usuarios.entities.Direccion;
import registro_usuarios.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface DireccionRepository
        extends JpaRepository<Direccion,Long> {

    List<Direccion> findByUsuarioAndActivoTrue(Usuario usuario);

    Optional<Direccion> findByIdAndActivoTrue(Long id);

    Optional<Direccion> findByUsuarioAndPredeterminadaTrueAndActivoTrue(
            Usuario usuario
    );

}