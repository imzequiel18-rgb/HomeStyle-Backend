package registro_usuarios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registro_usuarios.entities.Proveedor;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository <Proveedor, Long> {
    Optional<Proveedor> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Proveedor> findByActivoTrue();
}
