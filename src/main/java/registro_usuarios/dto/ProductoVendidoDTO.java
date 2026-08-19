package registro_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoVendidoDTO {

    private String nombre;

    private Long vendidos;

}