package registro_usuarios.services;

import registro_usuarios.dto.CategoriaAtributoDTO;

import java.util.List;

public interface CategoriaAtributoService {
    CategoriaAtributoDTO asignarAtributo(CategoriaAtributoDTO dto);

    CategoriaAtributoDTO actualizar(Long id,
                                    CategoriaAtributoDTO dto);

    void eliminar(Long id);

    CategoriaAtributoDTO buscarPorId(Long id);

    List<CategoriaAtributoDTO> listar();

    List<CategoriaAtributoDTO> listarPorCategoria(Long categoriaId);


}
