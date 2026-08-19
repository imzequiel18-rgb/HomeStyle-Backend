package registro_usuarios.services;

import registro_usuarios.dto.ProveedorDTO;


import java.util.List;


public interface ProveedorService {

    ProveedorDTO guardar(ProveedorDTO dto);

    ProveedorDTO actualizar(Long id, ProveedorDTO dto);

    void eliminar(Long id);

    ProveedorDTO buscarPorId(Long id);

    ProveedorDTO buscarPorNombre(String nombre);

    List<ProveedorDTO> listar();

    List<ProveedorDTO> listarActivos();


}
