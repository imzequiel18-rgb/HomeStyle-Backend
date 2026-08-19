package registro_usuarios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registro_usuarios.entities.ProductoAtributo;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoAtributoRepository extends JpaRepository<ProductoAtributo, Long> {
    List<ProductoAtributo> findByProductoId(Long productoId);


    Optional<ProductoAtributo> findByProductoIdAndAtributoId(
            Long productoId,
            Long atributoId);

    boolean existsByProductoIdAndAtributoId(
            Long productoId,
            Long atributoId);


    void deleteByProductoId(Long productoId);
}
