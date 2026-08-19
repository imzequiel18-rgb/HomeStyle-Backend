package registro_usuarios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registro_usuarios.entities.CategoriaAtributo;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaAtributoRepository  extends JpaRepository<CategoriaAtributo, Long> {
    List<CategoriaAtributo> findByCategoriaIdOrderByOrdenAsc(Long categoriaId);

    List<CategoriaAtributo> findByAtributoId(Long atributoId);

    Optional<CategoriaAtributo> findByCategoriaIdAndAtributoId(
            Long categoriaId,
            Long atributoId);

    boolean existsByCategoriaIdAndAtributoId(
            Long categoriaId,
            Long atributoId);

    List<CategoriaAtributo> findByCategoriaId(Long categoriaId);
}
