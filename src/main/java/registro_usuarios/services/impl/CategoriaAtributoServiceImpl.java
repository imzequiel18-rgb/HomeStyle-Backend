package registro_usuarios.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import registro_usuarios.dto.CategoriaAtributoDTO;
import registro_usuarios.entities.Atributo;
import registro_usuarios.entities.Categoria;
import registro_usuarios.entities.CategoriaAtributo;
import registro_usuarios.exceptions.RecursoDuplicadoException;
import registro_usuarios.exceptions.RecursoNoEncontradoException;
import registro_usuarios.mapper.CategoriaAtributoMapper;
import registro_usuarios.repositories.AtributoRepository;
import registro_usuarios.repositories.CategoriaAtributoRepository;
import registro_usuarios.repositories.CategoriaRepository;
import registro_usuarios.services.CategoriaAtributoService;

import java.util.List;

@Service
@Transactional
public class CategoriaAtributoServiceImpl implements CategoriaAtributoService {

    private final CategoriaAtributoRepository categoriaAtributoRepository;

    private final CategoriaRepository categoriaRepository;

    private final AtributoRepository atributoRepository;

    private final CategoriaAtributoMapper categoriaAtributoMapper;

    public CategoriaAtributoServiceImpl(
            CategoriaAtributoRepository categoriaAtributoRepository,
            CategoriaRepository categoriaRepository,
            AtributoRepository atributoRepository,
            CategoriaAtributoMapper categoriaAtributoMapper) {

        this.categoriaAtributoRepository = categoriaAtributoRepository;
        this.categoriaRepository = categoriaRepository;
        this.atributoRepository = atributoRepository;
        this.categoriaAtributoMapper = categoriaAtributoMapper;
    }

    private Categoria obtenerCategoria(Long id) {

        return categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Categoría no encontrada con ID: " + id));

    }

    private Atributo obtenerAtributo(Long id) {

        return atributoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Atributo no encontrado con ID: " + id));

    }

    private CategoriaAtributo obtenerRelacion(Long id) {

        return categoriaAtributoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Relación no encontrada con ID: " + id));

    }

    private void validarRelacion(Long categoriaId,
                                 Long atributoId) {

        if (categoriaAtributoRepository
                .existsByCategoriaIdAndAtributoId(
                        categoriaId,
                        atributoId)) {

            throw new RecursoDuplicadoException(
                    "El atributo ya está asignado a esta categoría.");

        }

    }

    @Override
    public CategoriaAtributoDTO asignarAtributo(CategoriaAtributoDTO dto) {

        validarRelacion(
                dto.getCategoriaId(),
                dto.getAtributoId());

        Categoria categoria = obtenerCategoria(
                dto.getCategoriaId());

        Atributo atributo = obtenerAtributo(
                dto.getAtributoId());

        CategoriaAtributo relacion =
                categoriaAtributoMapper.toEntity(dto);

        relacion.setCategoria(categoria);

        relacion.setAtributo(atributo);

        CategoriaAtributo guardada =
                categoriaAtributoRepository.save(relacion);

        return categoriaAtributoMapper.toDTO(guardada);

    }

    @Override
    public CategoriaAtributoDTO actualizar(Long id,
                                           CategoriaAtributoDTO dto) {

        CategoriaAtributo relacion = obtenerRelacion(id);

        if (!relacion.getCategoria().getId().equals(dto.getCategoriaId())
                || !relacion.getAtributo().getId().equals(dto.getAtributoId())) {

            validarRelacion(
                    dto.getCategoriaId(),
                    dto.getAtributoId());
        }

        Categoria categoria = obtenerCategoria(dto.getCategoriaId());

        Atributo atributo = obtenerAtributo(dto.getAtributoId());

        relacion.setCategoria(categoria);
        relacion.setAtributo(atributo);
        relacion.setObligatorio(dto.getObligatorio());
        relacion.setOrden(dto.getOrden());

        CategoriaAtributo actualizada =
                categoriaAtributoRepository.save(relacion);

        return categoriaAtributoMapper.toDTO(actualizada);

    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaAtributoDTO> listarPorCategoria(Long categoriaId) {

        obtenerCategoria(categoriaId);

        return categoriaAtributoMapper.toDTOList(
                categoriaAtributoRepository
                        .findByCategoriaIdOrderByOrdenAsc(categoriaId));

    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaAtributoDTO buscarPorId(Long id) {

        return categoriaAtributoMapper.toDTO(
                obtenerRelacion(id));

    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaAtributoDTO> listar() {

        return categoriaAtributoMapper.toDTOList(
                categoriaAtributoRepository.findAll());

    }

    @Override
    public void eliminar(Long id) {

        CategoriaAtributo relacion = obtenerRelacion(id);

        categoriaAtributoRepository.delete(relacion);

    }



}