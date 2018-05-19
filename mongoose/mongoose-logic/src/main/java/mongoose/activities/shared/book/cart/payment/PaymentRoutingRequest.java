package mongoose.activities.shared.book.cart.payment;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class PaymentRoutingRequest extends PushRoutingRequest {

    public PaymentRoutingRequest(Object cartUuidOrDocument, History history) {
        super(PaymentRouting.getPaymentPath(cartUuidOrDocument), history);
    }

}
