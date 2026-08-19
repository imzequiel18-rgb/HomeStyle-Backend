package registro_usuarios.services.impl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import registro_usuarios.dto.CategoriaDTO;
import registro_usuarios.entities.Categoria;
import registro_usuarios.exceptions.RecursoDuplicadoException;
import registro_usuarios.exceptions.RecursoNoEncontradoException;
import registro_usuarios.mapper.CategoriaMapper;
import registro_usuarios.repositories.CategoriaRepository;
import registro_usuarios.services.CategoriaService;

import java.util.List;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepository;

    private final CategoriaMapper categoriaMapper;

    public CategoriaServiceImpl(
            CategoriaRepository categoriaRepository,
            CategoriaMapper categoriaMapper) {

        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    private Categoria obtenerCategoria(Long id){

        return categoriaRepository.findById(id)
                .orElseThrow(()->
                        new RecursoNoEncontradoException(
                                "Categoría no encontrada con ID: "+id));

    }

    private void validarNombre(String nombre){

        if(categoriaRepository.existsByNombre(nombre)){

            throw new RecursoDuplicadoException(
                    "Ya existe una categoría con el nombre: "+nombre);

        }

    }

    @Override
    public CategoriaDTO guardar(CategoriaDTO dto){

        validarNombre(dto.getNombre());

        Categoria categoria = categoriaMapper.toEntity(dto);

        Categoria guardada = categoriaRepository.save(categoria);

        return categoriaMapper.toDTO(guardada);

    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO buscarPorId(Long id){

        return categoriaMapper.toDTO(
                obtenerCategoria(id));

    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> listar(){

        return categoriaMapper.toDTOList(
                categoriaRepository.findAll());

    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO buscarPorNombre(String nombre){

        Categoria categoria = categoriaRepository.findByNombre(nombre)
                .orElseThrow(()->
                        new RecursoNoEncontradoException(
                                "Categoría no encontrada."));

        return categoriaMapper.toDTO(categoria);

    }

    @Override
    public CategoriaDTO actualizar(Long id, CategoriaDTO dto){

        Categoria categoria = obtenerCategoria(id);

        if(!categoria.getNombre().equals(dto.getNombre())){

            validarNombre(dto.getNombre());

        }

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setImagen(dto.getImagen());
        categoria.setActivo(dto.getActivo());

        Categoria actualizada = categoriaRepository.save(categoria);

        return categoriaMapper.toDTO(actualizada);

    }

    @Override
    public void eliminar(Long id){

        Categoria categoria = obtenerCategoria(id);

        categoria.setActivo(false);

        categoriaRepository.save(categoria);

    }



}
