package registro_usuarios.services;

import com.mercadopago.resources.preference.Preference;
import registro_usuarios.entities.Pedido;

public interface MercadoPagoService {

    Preference crearPreferencia(Pedido pedido);

    /**
     * Intenta invalidar una preferencia de Checkout Pro para evitar que
     * un enlace viejo siga siendo utilizable después de cancelar un pedido.
     */
    void invalidarPreferencia(String preferenceId);
}
