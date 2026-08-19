package registro_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoStockDTO {

    private String nombre;

    private Integer stock;

}