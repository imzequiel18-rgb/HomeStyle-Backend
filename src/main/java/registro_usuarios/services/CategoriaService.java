package registro_usuarios.services;

import registro_usuarios.dto.CategoriaDTO;

import java.util.List;

public interface CategoriaService {

    CategoriaDTO guardar(CategoriaDTO dto);

    CategoriaDTO actualizar(Long id, CategoriaDTO dto);

    void eliminar(Long id);

    CategoriaDTO buscarPorId(Long id);

    CategoriaDTO buscarPorNombre(String nombre);

    List<CategoriaDTO> listar();

}