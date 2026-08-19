package registro_usuarios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import registro_usuarios.entities.Usuario;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long>  {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    @Query("""
    SELECT COUNT(u)
    FROM Usuario u
    WHERE u.rol = 'CLIENTE'
    """)
    Long contarClientes();


}
