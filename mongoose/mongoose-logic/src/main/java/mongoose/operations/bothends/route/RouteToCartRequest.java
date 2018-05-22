package mongoose.operations.bothends.route;

import mongoose.activities.bothends.book.cart.CartRouting;
import naga.framework.operations.route.RoutePushRequest;
import naga.platform.client.url.history.History;
import naga.platform.json.Json;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public class RouteToCartRequest extends RoutePushRequest {

    public RouteToCartRequest(Object cartUuidOrDocument, History history) {
        super(CartRouting.getCartPath(cartUuidOrDocument), history, Json.createObject().set("refresh", Instant.now()));
    }

}
