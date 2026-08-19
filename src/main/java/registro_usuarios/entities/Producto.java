package registro_usuarios.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 50, unique = true)
    private String sku;

    @Column(nullable = false , length = 100)
    private String nombre;

    @Column(nullable = false , length = 100)
    private String descripcion;

    @Column(name = "precio_venta", nullable = false)
    private BigDecimal precioVenta;     // se cambbio a bigdecimal

    @Column(nullable = false)
    private Integer stock;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imagen;

    @Column(nullable=false)
    private Boolean activo=true;

    //datos sensibles para DTO
    @Column(name = "precio_costo", nullable = false)
    private BigDecimal precioCosto;


    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false) // Nombre de la FK en la tabla
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false) // Nombre de la FK en la tabla
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false) // Nombre de la FK en la tabla
    private Marca marca;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoAtributo> atributos = new ArrayList<>();

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<ProductoAtributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<ProductoAtributo> atributos) {
        this.atributos = atributos;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Producto(){}

    public Producto( BigDecimal precioCosto,
                    String imagen, Integer stock, String sku,
                    BigDecimal precioVenta, String descripcion,
                    String nombre) {


        this.precioCosto = precioCosto;
        this.imagen = imagen;
        this.stock = stock;
        this.sku = sku;
        this.precioVenta = precioVenta;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }



    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }


    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
