package registro_usuarios.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import registro_usuarios.dto.ProductoAtributoDTO;
import registro_usuarios.entities.Atributo;
import registro_usuarios.entities.Producto;
import registro_usuarios.entities.ProductoAtributo;
import registro_usuarios.exceptions.RecursoDuplicadoException;
import registro_usuarios.exceptions.RecursoNoEncontradoException;
import registro_usuarios.mapper.ProductoAtributoMapper;
import registro_usuarios.repositories.*;
import registro_usuarios.services.ProductoAtributoService;
import registro_usuarios.entities.CategoriaAtributo;
import registro_usuarios.repositories.CategoriaAtributoRepository;

import java.util.List;

@Service
@Transactional
public class ProductoAtributoServiceImpl implements ProductoAtributoService {
    private final ProductoAtributoRepository productoAtributoRepository;

    private final ProductoRepository productoRepository;

    private final AtributoRepository atributoRepository;

    private final ProductoAtributoMapper productoAtributoMapper;

    private final CategoriaAtributoRepository categoriaAtributoRepository;

    public ProductoAtributoServiceImpl(
            ProductoAtributoRepository productoAtributoRepository,
            ProductoRepository productoRepository,
            AtributoRepository atributoRepository,
            ProductoAtributoMapper productoAtributoMapper,
            CategoriaAtributoRepository categoriaAtributoRepository) {

        this.productoAtributoRepository = productoAtributoRepository;
        this.productoRepository = productoRepository;
        this.atributoRepository = atributoRepository;
        this.productoAtributoMapper = productoAtributoMapper;
        this.categoriaAtributoRepository = categoriaAtributoRepository;
    }

    private Producto obtenerProducto(Long id) {

        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Producto no encontrado con ID: " + id));

    }

    private Atributo obtenerAtributo(Long id) {

        return atributoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Atributo no encontrado con ID: " + id));

    }


    private ProductoAtributo obtenerValor(Long id) {

        return productoAtributoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Valor de atributo no encontrado con ID: " + id));

    }

    private void validarRelacion(Long productoId,
                                 Long atributoId) {

        if (productoAtributoRepository
                .existsByProductoIdAndAtributoId(
                        productoId,
                        atributoId)) {

            throw new RecursoDuplicadoException(
                    "El producto ya tiene asignado este atributo.");

        }

    }

    @Override
    public ProductoAtributoDTO guardarValor(ProductoAtributoDTO dto) {

        validarRelacion(
                dto.getProductoId(),
                dto.getAtributoId());

        Producto producto = obtenerProducto(
                dto.getProductoId());

        Atributo atributo = obtenerAtributo(
                dto.getAtributoId());

        ProductoAtributo productoAtributo =
                productoAtributoMapper.toEntity(dto);

        productoAtributo.setProducto(producto);

        productoAtributo.setAtributo(atributo);

        ProductoAtributo guardado =
                productoAtributoRepository.save(productoAtributo);

        return productoAtributoMapper.toDTO(guardado);

    }

    @Override
    public ProductoAtributoDTO actualizarValor(Long id,
                                               ProductoAtributoDTO dto) {

        ProductoAtributo valor = obtenerValor(id);

        if (!valor.getProducto().getId().equals(dto.getProductoId())
                || !valor.getAtributo().getId().equals(dto.getAtributoId())) {

            validarRelacion(
                    dto.getProductoId(),
                    dto.getAtributoId());
        }

        Producto producto = obtenerProducto(dto.getProductoId());

        Atributo atributo = obtenerAtributo(dto.getAtributoId());

        valor.setProducto(producto);
        valor.setAtributo(atributo);
        valor.setValor(dto.getValor());

        ProductoAtributo actualizado =
                productoAtributoRepository.save(valor);

        return productoAtributoMapper.toDTO(actualizado);

    }

    @Override
    @Transactional(readOnly = true)
    public ProductoAtributoDTO buscarPorId(Long id) {

        return productoAtributoMapper.toDTO(
                obtenerValor(id));

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoAtributoDTO> listarPorProducto(Long productoId) {

        obtenerProducto(productoId);

        return productoAtributoMapper.toDTOList(
                productoAtributoRepository.findByProductoId(productoId));

    }

    @Override
    public List<ProductoAtributoDTO> reemplazarPorProducto(
            Long productoId,
            List<ProductoAtributoDTO> atributos) {

        Producto producto = obtenerProducto(productoId);

        if (atributos == null) {
            atributos = List.of();
        }

        List<CategoriaAtributo> atributosCategoria =
                categoriaAtributoRepository
                        .findByCategoriaIdOrderByOrdenAsc(
                                producto.getCategoria().getId());

        java.util.Map<Long, CategoriaAtributo> permitidos =
                atributosCategoria.stream()
                        .collect(java.util.stream.Collectors.toMap(
                                ca -> ca.getAtributo().getId(),
                                ca -> ca
                        ));

        java.util.Set<Long> atributosRecibidos =
                new java.util.HashSet<>();

        for (ProductoAtributoDTO dto : atributos) {

            if (dto.getAtributoId() == null) {
                throw new IllegalArgumentException(
                        "Todos los atributos deben tener un atributoId.");
            }

            if (!atributosRecibidos.add(dto.getAtributoId())) {
                throw new IllegalArgumentException(
                        "No se puede repetir el mismo atributo.");
            }

            CategoriaAtributo categoriaAtributo =
                    permitidos.get(dto.getAtributoId());

            if (categoriaAtributo == null) {
                throw new IllegalArgumentException(
                        "El atributo " + dto.getAtributoId()
                                + " no pertenece a la categoría del producto.");
            }


            if (Boolean.TRUE.equals(categoriaAtributo.getObligatorio())
                    && (dto.getValor() == null
                    || dto.getValor().isBlank())) {

                throw new IllegalArgumentException(
                        "El atributo "
                                + categoriaAtributo.getAtributo().getNombre()
                                + " es obligatorio.");
            }
        }

        for (CategoriaAtributo categoriaAtributo : atributosCategoria) {

            if (Boolean.TRUE.equals(categoriaAtributo.getObligatorio())
                    && !atributosRecibidos.contains(
                    categoriaAtributo.getAtributo().getId())) {

                throw new IllegalArgumentException(
                        "Falta el atributo obligatorio: "
                                + categoriaAtributo.getAtributo().getNombre());
            }
        }

        productoAtributoRepository.deleteByProductoId(productoId);
        productoAtributoRepository.flush();

        for (ProductoAtributoDTO dto : atributos) {

            ProductoAtributo productoAtributo =
                    new ProductoAtributo();

            productoAtributo.setProducto(producto);
            productoAtributo.setAtributo(
                    obtenerAtributo(dto.getAtributoId()));
            productoAtributo.setValor(dto.getValor());

            productoAtributoRepository.save(productoAtributo);
        }

        return listarPorProducto(productoId);
    }

    @Override
    public void eliminar(Long id) {

        ProductoAtributo valor = obtenerValor(id);

        productoAtributoRepository.delete(valor);

    }
}
