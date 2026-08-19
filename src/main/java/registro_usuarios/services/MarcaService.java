package registro_usuarios.services;

import registro_usuarios.dto.MarcaDTO;

import java.util.List;

public interface MarcaService {
    MarcaDTO guardar(MarcaDTO dto);

    MarcaDTO actualizar(Long id, MarcaDTO dto);

    void eliminar(Long id);

    MarcaDTO buscarPorId(Long id);

    MarcaDTO buscarPorNombre(String nombre);

    List<MarcaDTO> listar();
}
