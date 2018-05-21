package mongoose.activities.shared.book.payment;

import naga.framework.ui.router.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToPaymentRequest extends PushRouteRequest {

    public RouteToPaymentRequest(Object cartUuidOrDocument, History history) {
        super(PaymentRouting.getPaymentPath(cartUuidOrDocument), history);
    }

}
