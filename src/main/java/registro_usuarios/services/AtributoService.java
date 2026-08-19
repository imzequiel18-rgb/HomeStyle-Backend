package registro_usuarios.services;

import registro_usuarios.dto.AtributoDTO;

import java.util.List;

public interface AtributoService {

    AtributoDTO guardar(AtributoDTO dto);

    AtributoDTO actualizar(Long id, AtributoDTO dto);

    void eliminar(Long id);

    AtributoDTO buscarPorId(Long id);

    AtributoDTO buscarPorNombre(String nombre);

    List<AtributoDTO> listar();

    List<AtributoDTO> listarActivos();
}
