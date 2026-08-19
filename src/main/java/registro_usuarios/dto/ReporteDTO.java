package registro_usuarios.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ReporteDTO {

    private BigDecimal ventasTotales;

    private Long totalPedidos;

    private Long totalClientes;

    private Long totalProductos;

    private List<ProductoStockDTO> productosStockBajo;

    private List<ProductoVendidoDTO> productosMasVendidos;

    private List<PedidoRecienteDTO> ultimosPedidos;

}