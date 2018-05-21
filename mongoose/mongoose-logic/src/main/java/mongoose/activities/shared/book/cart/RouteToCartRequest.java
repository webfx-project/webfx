package mongoose.activities.shared.book.cart;

import naga.framework.ui.router.PushRouteRequest;
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
