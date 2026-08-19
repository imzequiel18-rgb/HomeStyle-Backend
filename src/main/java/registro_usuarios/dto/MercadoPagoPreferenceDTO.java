package registro_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MercadoPagoPreferenceDTO {

    private Long pedidoId;

    private String numeroPedido;

    private String preferenceId;

    private String checkoutUrl;
}