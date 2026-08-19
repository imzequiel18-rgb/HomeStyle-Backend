package registro_usuarios.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import registro_usuarios.dto.ProveedorDTO;
import registro_usuarios.entities.Proveedor;
import registro_usuarios.exceptions.RecursoDuplicadoException;
import registro_usuarios.exceptions.RecursoNoEncontradoException;
import registro_usuarios.mapper.ProveedorMapper;
import registro_usuarios.repositories.ProveedorRepository;
import registro_usuarios.services.ProveedorService;

import java.util.List;

@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {
    private final ProveedorRepository proveedorRepository;

    private final ProveedorMapper proveedorMapper;

    public ProveedorServiceImpl(
            ProveedorRepository proveedorRepository,
            ProveedorMapper proveedorMapper) {

        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    // Métodos públicos
    @Override
    public ProveedorDTO guardar(ProveedorDTO dto){

        validarNombre(dto.getNombre());

        Proveedor proveedor = proveedorMapper.toEntity(dto);

        Proveedor guardado = proveedorRepository.save(proveedor);

        return proveedorMapper.toDTO(guardado);

    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDTO buscarPorId(Long id){

        return proveedorMapper.toDTO(
                obtenerProveedor(id));

    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDTO buscarPorNombre(String nombre){

        Proveedor proveedor = proveedorRepository.findByNombre(nombre)
                .orElseThrow(()->
                        new RecursoNoEncontradoException(
                                "Proveedor no encontrado con nombre: "+nombre));

        return proveedorMapper.toDTO(proveedor);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> listar(){

        return proveedorMapper.toDTOList(
                proveedorRepository.findAll());

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarActivos(){

        return proveedorMapper.toDTOList(
                proveedorRepository.findByActivoTrue());

    }

    @Override
    public ProveedorDTO actualizar(Long id, ProveedorDTO dto){

        Proveedor proveedor = obtenerProveedor(id);

        if(!proveedor.getNombre().equals(dto.getNombre())){

            validarNombre(dto.getNombre());

        }

        proveedor.setNombre(dto.getNombre());
        proveedor.setCorreo(dto.getCorreo());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setPersonaContacto(dto.getPersonaContacto());
        proveedor.setActivo(dto.getActivo());

        Proveedor actualizado = proveedorRepository.save(proveedor);

        return proveedorMapper.toDTO(actualizado);

    }

    @Override
    public void eliminar(Long id){

        Proveedor proveedor = obtenerProveedor(id);

        proveedor.setActivo(false);

        proveedorRepository.save(proveedor);

    }


    // Métodos privados
    private Proveedor obtenerProveedor(Long id){

        return proveedorRepository.findById(id)
                .orElseThrow(()->
                        new RecursoNoEncontradoException(
                                "Proveedor no encontrado con ID: "+id));

    }

    private void validarNombre(String nombre){

        if(proveedorRepository.existsByNombre(nombre)){

            throw new RecursoDuplicadoException(
                    "Ya existe un proveedor con el nombre: "+nombre);

        }

    }


}
