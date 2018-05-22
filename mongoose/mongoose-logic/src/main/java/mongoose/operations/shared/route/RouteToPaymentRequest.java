package mongoose.operations.shared.route;

import mongoose.activities.shared.book.payment.PaymentRouting;
import naga.framework.operations.route.RoutePushRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToPaymentRequest extends RoutePushRequest {

    public RouteToPaymentRequest(Object cartUuidOrDocument, History history) {
        super(PaymentRouting.getPaymentPath(cartUuidOrDocument), history);
    }

}
