package mongoose.activities.shared.book.cart.payment;

import mongoose.activities.shared.book.cart.CartRoutingRequest;
import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class PaymentRoutingRequest extends UiRoutingRequest {

    public PaymentRoutingRequest(Object cartUuid, History history) {
        super(MongooseRoutingUtil.interpolateCartUuidInPath(CartRoutingRequest.getCartUuid(cartUuid), PaymentRouting.PATH), history);
    }

}
