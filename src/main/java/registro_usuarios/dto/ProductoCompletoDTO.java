package registro_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCompletoDTO {

    private ProductoAdminDTO producto;

    private List<ProductoAtributoDTO> atributos;

}