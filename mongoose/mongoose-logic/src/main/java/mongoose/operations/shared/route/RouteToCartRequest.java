package mongoose.operations.shared.route;

import mongoose.activities.shared.book.cart.CartRouting;
import naga.framework.operations.route.PushRouteRequest;
import naga.platform.client.url.history.History;
import naga.platform.json.Json;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public class RouteToCartRequest extends PushRouteRequest {

    public RouteToCartRequest(Object cartUuidOrDocument, History history) {
        super(CartRouting.getCartPath(cartUuidOrDocument), history, Json.createObject().set("refresh", Instant.now()));
    }

}
