package mongoose.operations.bothends.route;

import mongoose.activities.bothends.book.cart.CartRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;
import webfx.platform.services.json.Json;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public final class RouteToCartRequest extends RoutePushRequest {

    public RouteToCartRequest(Object cartUuidOrDocument, History history) {
        super(CartRouting.getCartPath(cartUuidOrDocument), history, Json.createObject().set("refresh", Instant.now()));
    }

}
