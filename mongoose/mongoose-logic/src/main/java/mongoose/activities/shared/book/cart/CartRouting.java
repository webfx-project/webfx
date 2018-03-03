package mongoose.activities.shared.book.cart;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class CartRouting {

    public final static String PATH = "/book/cart/:cartUuid";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , CartViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static Object getCartUuidFromDocument(Entity document) {
        return document == null ? null : document.evaluate("cart.uuid");
    }

}
