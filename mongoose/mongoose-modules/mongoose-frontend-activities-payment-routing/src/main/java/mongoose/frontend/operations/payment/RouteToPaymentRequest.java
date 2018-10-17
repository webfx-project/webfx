package mongoose.frontend.operations.payment;

import mongoose.frontend.activities.payment.routing.PaymentRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToPaymentRequest extends RoutePushRequest {

    public RouteToPaymentRequest(Object cartUuidOrDocument, BrowsingHistory history) {
        super(PaymentRouting.getPaymentPath(cartUuidOrDocument), history);
    }

}
