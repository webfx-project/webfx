package mongooses.core.frontend.operations.payment;

import mongooses.core.frontend.activities.payment.PaymentRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToPaymentRequest extends RoutePushRequest {

    public RouteToPaymentRequest(Object cartUuidOrDocument, BrowsingHistory history) {
        super(PaymentRouting.getPaymentPath(cartUuidOrDocument), history);
    }

}
