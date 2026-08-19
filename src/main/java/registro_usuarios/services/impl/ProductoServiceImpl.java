package registro_usuarios.services.impl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import registro_usuarios.dto.ProductoAdminDTO;
import registro_usuarios.dto.ProductoClienteDTO;
import registro_usuarios.entities.Categoria;
import registro_usuarios.entities.Marca;
import registro_usuarios.entities.Producto;
import registro_usuarios.entities.Proveedor;
import registro_usuarios.exceptions.RecursoDuplicadoException;
import registro_usuarios.exceptions.RecursoNoEncontradoException;
import registro_usuarios.mapper.ProductoMapper;
import registro_usuarios.repositories.CategoriaRepository;
import registro_usuarios.repositories.MarcaRepository;
import registro_usuarios.repositories.ProductoRepository;
import registro_usuarios.repositories.ProveedorRepository;
import registro_usuarios.services.ProductoService;
import org.springframework.web.multipart.MultipartFile;
import registro_usuarios.services.ArchivoService;

import java.util.List;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoMapper productoMapper;
    private final ArchivoService archivoService;

    public ProductoServiceImpl(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            MarcaRepository marcaRepository,
            ProveedorRepository proveedorRepository,
            ProductoMapper productoMapper,
            ArchivoService archivoService) {

        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoMapper = productoMapper;
        this.archivoService = archivoService;
    }

    // Métodos...
    private Categoria obtenerCategoria(Long categoriaId) {

        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Categoría no encontrada con ID: " + categoriaId));

    }

    private Marca obtenerMarca(Long marcaId) {

        return marcaRepository.findById(marcaId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Marca no encontrada con ID: " + marcaId));

    }

    private Proveedor obtenerProveedor(Long proveedorId) {

        return proveedorRepository.findById(proveedorId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Proveedor no encontrado con ID: " + proveedorId));

    }

    private void validarSkuUnico(String sku) {

        if (productoRepository.existsBySku(sku)) {
            throw new RecursoDuplicadoException(
                    "Ya existe un producto con el SKU: " + sku);
        }

    }

    @Override
    public ProductoAdminDTO guardar(ProductoAdminDTO dto, MultipartFile imagen) {

        validarSkuUnico(dto.getSku());

        Categoria categoria = obtenerCategoria(dto.getCategoriaId());

        Marca marca = obtenerMarca(dto.getMarcaId());

        Proveedor proveedor = obtenerProveedor(dto.getProveedorId());

        Producto producto = productoMapper.toEntity(dto);

        if (imagen != null && !imagen.isEmpty()) {

            String nombreArchivo = archivoService.guardarImagen(imagen);

            producto.setImagen(nombreArchivo);

        }

        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setProveedor(proveedor);

        Producto guardado = productoRepository.save(producto);

        return productoMapper.toAdminDTO(guardado);

    }

    @Override
    @Transactional(readOnly = true)
    public ProductoAdminDTO buscarPorId(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Producto no encontrado con ID: " + id));

        return productoMapper.toAdminDTO(producto);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoAdminDTO> listar() {

        return productoMapper.toAdminDTOList(
                productoRepository.findAll());

    }

    @Override
    @Transactional(readOnly = true)
    public ProductoAdminDTO buscarPorSku(String sku) {

        Producto producto = productoRepository.findBySku(sku)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Producto no encontrado con SKU: " + sku));

        return productoMapper.toAdminDTO(producto);

    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductoAdminDTO> buscarPorCategoria(Long categoriaId) {

        return productoMapper.toAdminDTOList(
                productoRepository.findByCategoriaId(categoriaId));

    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductoAdminDTO> buscarPorMarca(Long marcaId) {

        return productoMapper.toAdminDTOList(
                productoRepository.findByMarcaId(marcaId));

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoAdminDTO> buscarPorProveedor(Long proveedorId) {

        return productoMapper.toAdminDTOList(
                productoRepository.findByProveedorId(proveedorId));

    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductoClienteDTO> buscarPorNombre(String nombre) {

        return productoMapper.toClienteDTOList(
                productoRepository.findByNombreContainingIgnoreCase(nombre));

    }

    @Override
    public ProductoAdminDTO actualizar(Long id, ProductoAdminDTO dto, MultipartFile imagen) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Producto no encontrado con ID: " + id));

        if (!producto.getSku().equals(dto.getSku())) {
            validarSkuUnico(dto.getSku());
        }

        Categoria categoria = obtenerCategoria(dto.getCategoriaId());
        Marca marca = obtenerMarca(dto.getMarcaId());
        Proveedor proveedor = obtenerProveedor(dto.getProveedorId());

        producto.setSku(dto.getSku());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioCosto(dto.getPrecioCosto());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo());

        if (imagen != null && !imagen.isEmpty()) {

            if (producto.getImagen() != null) {
                archivoService.eliminarImagen(producto.getImagen());
            }

            String nombreArchivo = archivoService.guardarImagen(imagen);

            producto.setImagen(nombreArchivo);
        }

        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setProveedor(proveedor);

        Producto actualizado = productoRepository.save(producto);

        return productoMapper.toAdminDTO(actualizado);
    }


    @Override
    public void eliminar(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Producto no encontrado con ID: " + id));

        producto.setActivo(false);

        productoRepository.save(producto);
    }





}
