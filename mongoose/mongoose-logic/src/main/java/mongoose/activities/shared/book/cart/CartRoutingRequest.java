package mongoose.activities.shared.book.cart;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;
import naga.platform.json.Json;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public class CartRoutingRequest extends PushRoutingRequest {

    public CartRoutingRequest(Object cartUuidOrDocument, History history) {
        super(CartRouting.getCartPath(cartUuidOrDocument), history, Json.createObject().set("refresh", Instant.now()));
    }

}
