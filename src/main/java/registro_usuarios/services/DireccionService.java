package registro_usuarios.services;

import registro_usuarios.dto.DireccionDTO;
import registro_usuarios.dto.DireccionRequestDTO;

import java.util.List;

public interface DireccionService {

    List<DireccionDTO> obtenerMisDirecciones();

    DireccionDTO obtenerDireccion(Long id);

    DireccionDTO crearDireccion(DireccionRequestDTO request);

    DireccionDTO actualizarDireccion(Long id,
                                     DireccionRequestDTO request);

    void eliminarDireccion(Long id);

    void establecerPredeterminada(Long id);

}