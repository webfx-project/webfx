package mongoose.operations.bothends.route;

import mongoose.activities.bothends.book.payment.PaymentRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToPaymentRequest extends RoutePushRequest {

    public RouteToPaymentRequest(Object cartUuidOrDocument, History history) {
        super(PaymentRouting.getPaymentPath(cartUuidOrDocument), history);
    }

}
