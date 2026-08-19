package registro_usuarios.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import registro_usuarios.dto.MarcaDTO;
import registro_usuarios.entities.Marca;
import registro_usuarios.exceptions.RecursoDuplicadoException;
import registro_usuarios.exceptions.RecursoNoEncontradoException;
import registro_usuarios.mapper.MarcaMapper;
import registro_usuarios.repositories.MarcaRepository;
import registro_usuarios.services.MarcaService;

import java.util.List;

@Service
@Transactional
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public MarcaServiceImpl(MarcaRepository marcaRepository,
                            MarcaMapper marcaMapper) {

        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
    }

    // Métodos públicos
    @Override
    public MarcaDTO guardar(MarcaDTO dto) {

        validarNombre(dto.getNombre());

        Marca marca = marcaMapper.toEntity(dto);

        Marca guardada = marcaRepository.save(marca);

        return marcaMapper.toDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public MarcaDTO buscarPorId(Long id) {

        return marcaMapper.toDTO(obtenerMarca(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaDTO> listar() {

        return marcaMapper.toDTOList(
                marcaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public MarcaDTO buscarPorNombre(String nombre) {

        Marca marca = marcaRepository.findByNombre(nombre)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Marca no encontrada con nombre: " + nombre));

        return marcaMapper.toDTO(marca);
    }

    @Override
    public MarcaDTO actualizar(Long id, MarcaDTO dto) {

        Marca marca = obtenerMarca(id);

        if (!marca.getNombre().equals(dto.getNombre())) {
            validarNombre(dto.getNombre());
        }

        marca.setNombre(dto.getNombre());
        marca.setDescripcion(dto.getDescripcion());
        marca.setLogo(dto.getLogo());
        marca.setActivo(dto.getActivo());

        Marca actualizada = marcaRepository.save(marca);

        return marcaMapper.toDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {

        Marca marca = obtenerMarca(id);

        marca.setActivo(false);

        marcaRepository.save(marca);
    }

    public List<MarcaDTO> listarActivas() {

        return marcaMapper.toDTOList(
                marcaRepository.findByActivoTrue());
    }

    // Métodos privados
    private Marca obtenerMarca(Long id) {

        return marcaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Marca no encontrada con ID: " + id));
    }

    private void validarNombre(String nombre) {

        if (marcaRepository.existsByNombre(nombre)) {

            throw new RecursoDuplicadoException(
                    "Ya existe una marca con el nombre: " + nombre);
        }
    }



}