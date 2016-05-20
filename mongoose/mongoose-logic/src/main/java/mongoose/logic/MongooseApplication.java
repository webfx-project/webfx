package mongoose.logic;

import mongoose.logic.cart.CartActivity;
import mongoose.logic.organizations.OrganizationsActivity;
import naga.core.activity.Activity;
import naga.core.activity.ActivityContext;
import naga.core.activity.ActivityRouterHelper;
import naga.core.routing.Router;

/**
 * @author Bruno Salmon
 */
abstract class MongooseApplication implements Activity {

    protected final Router router = new Router();

    @Override
    public void onCreate(ActivityContext context) {
        new ActivityRouterHelper(router, context)
                .route("/organizations", OrganizationsActivity::new)
                .route("/cart/:cartUuid", CartActivity::new);
    }

    @Override
    public void onStart() {
        router.start();
    }

}
