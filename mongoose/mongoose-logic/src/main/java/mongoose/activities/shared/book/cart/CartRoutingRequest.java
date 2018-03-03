package mongoose.activities.shared.book.cart;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import mongoose.entities.Document;
import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;
import naga.platform.json.Json;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public class CartRoutingRequest extends UiRoutingRequest {

    public CartRoutingRequest(Object cartUuid, History history) {
        super(MongooseRoutingUtil.interpolateCartUuidInPath(getCartUuid(cartUuid), CartRouting.PATH), history, Json.createObject().set("refresh", Instant.now()));
    }

    public static Object getCartUuid(Object o) {
        if (o instanceof Document)
            return ((Document) o).evaluate("cart.uuid");
        return o;
    }
}
