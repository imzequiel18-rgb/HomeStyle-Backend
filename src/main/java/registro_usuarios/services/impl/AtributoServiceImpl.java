package registro_usuarios.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import registro_usuarios.dto.AtributoDTO;
import registro_usuarios.entities.Atributo;
import registro_usuarios.exceptions.RecursoDuplicadoException;
import registro_usuarios.exceptions.RecursoNoEncontradoException;
import registro_usuarios.mapper.AtributoMapper;
import registro_usuarios.repositories.AtributoRepository;
import registro_usuarios.services.AtributoService;

import java.util.List;

@Service
@Transactional
public class AtributoServiceImpl implements AtributoService {

    private final AtributoRepository atributoRepository;
    private final AtributoMapper atributoMapper;

    public AtributoServiceImpl(
            AtributoRepository atributoRepository,
            AtributoMapper atributoMapper) {

        this.atributoRepository = atributoRepository;
        this.atributoMapper = atributoMapper;
    }

    // Métodos públicos
    @Override
    public AtributoDTO guardar(AtributoDTO dto) {

        validarNombre(dto.getNombre());

        Atributo atributo = atributoMapper.toEntity(dto);

        Atributo guardado = atributoRepository.save(atributo);

        return atributoMapper.toDTO(guardado);

    }

    @Override
    public AtributoDTO actualizar(Long id, AtributoDTO dto) {

        Atributo atributo = obtenerAtributo(id);

        if (!atributo.getNombre().equals(dto.getNombre())) {
            validarNombre(dto.getNombre());
        }

        atributo.setNombre(dto.getNombre());
        atributo.setTipoDato(
                Atributo.TipoDato.valueOf(dto.getTipoDato()));
        atributo.setUnidad(dto.getUnidad());
        atributo.setActivo(dto.getActivo());

        Atributo actualizado = atributoRepository.save(atributo);

        return atributoMapper.toDTO(actualizado);

    }

    @Override
    public void eliminar(Long id) {

        Atributo atributo = obtenerAtributo(id);

        atributo.setActivo(false);

        atributoRepository.save(atributo);

    }

    @Override
    @Transactional(readOnly = true)
    public AtributoDTO buscarPorId(Long id) {

        return atributoMapper.toDTO(
                obtenerAtributo(id));

    }

    @Override
    @Transactional(readOnly = true)
    public AtributoDTO buscarPorNombre(String nombre) {

        Atributo atributo = atributoRepository.findByNombre(nombre)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Atributo no encontrado con nombre: " + nombre));

        return atributoMapper.toDTO(atributo);

    }

    @Override
    @Transactional(readOnly = true)
    public List<AtributoDTO> listar() {

        return atributoMapper.toDTOList(
                atributoRepository.findAll());

    }

    @Override
    @Transactional(readOnly = true)
    public List<AtributoDTO> listarActivos() {

        return atributoMapper.toDTOList(
                atributoRepository.findByActivoTrue());

    }


    // Métodos privados
    private Atributo obtenerAtributo(Long id) {

        return atributoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Atributo no encontrado con ID: " + id));

    }

    private void validarNombre(String nombre) {

        if (atributoRepository.existsByNombre(nombre)) {

            throw new RecursoDuplicadoException(
                    "Ya existe un atributo con el nombre: " + nombre);

        }

    }


}

