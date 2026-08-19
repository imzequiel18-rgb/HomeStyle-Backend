package registro_usuarios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import registro_usuarios.entities.Carrito;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioId(Long usuarioId);

}