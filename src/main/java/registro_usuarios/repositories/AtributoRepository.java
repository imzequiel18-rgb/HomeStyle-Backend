package registro_usuarios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registro_usuarios.entities.Atributo;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtributoRepository extends JpaRepository<Atributo, Long> {
    Optional<Atributo> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Atributo> findByActivoTrue();
}
