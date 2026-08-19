package registro_usuarios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import registro_usuarios.entities.CarritoItem;

import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    Optional<CarritoItem> findByCarritoIdAndProductoId(
            Long carritoId,
            Long productoId
    );

}