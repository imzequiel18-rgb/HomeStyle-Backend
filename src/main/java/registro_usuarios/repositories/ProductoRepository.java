package registro_usuarios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import registro_usuarios.entities.Producto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByMarcaId(Long marcaId);

    List<Producto> findByProveedorId(Long proveedorId);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Query("""
    SELECT p
    FROM Producto p
    WHERE p.activo = true
    ORDER BY p.stock ASC
    """)

    List<Producto> obtenerProductosConMenorStock();
}
