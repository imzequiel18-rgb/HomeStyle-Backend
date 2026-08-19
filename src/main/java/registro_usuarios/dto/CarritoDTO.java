package registro_usuarios.dto;

import java.util.ArrayList;
import java.util.List;

public class CarritoDTO {

    private Long id;

    private Integer totalProductos;

    private Double totalCompra;

    private List<CarritoItemDTO> items = new ArrayList<>();

    public CarritoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(Integer totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(Double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public List<CarritoItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CarritoItemDTO> items) {
        this.items = items;
    }
}