package mongoose.frontend.operations.cart;

import mongoose.frontend.activities.cart.routing.CartRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.platform.shared.services.json.Json;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public final class RouteToCartRequest extends RoutePushRequest {

    public RouteToCartRequest(Object cartUuidOrDocument, BrowsingHistory history) {
        super(CartRouting.getCartPath(cartUuidOrDocument), history, Json.createObject().set("refresh", Instant.now()));
    }

}
