package mongooses.core.operations.bothends.route;

import mongooses.core.activities.sharedends.book.cart.CartRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;
import webfx.platforms.core.services.json.Json;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public final class RouteToCartRequest extends RoutePushRequest {

    public RouteToCartRequest(Object cartUuidOrDocument, BrowsingHistory history) {
        super(CartRouting.getCartPath(cartUuidOrDocument), history, Json.createObject().set("refresh", Instant.now()));
    }

}
