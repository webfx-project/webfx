package mongoose.logic;

import mongoose.logic.cart.CartLogic;
import mongoose.logic.organizations.OrganizationsLogic;
import naga.core.ngui.lifecycle.Application;
import naga.core.ngui.lifecycle.ApplicationContext;
import naga.core.routing.Router;

/**
 * @author Bruno Salmon
 */
abstract class MongooseApplication implements Application {

    private Router router;

    @Override
    public void onCreate(ApplicationContext context) {
        setUpWebSocketConnection();
        router = setUpRouter();
    }

    protected void setUpWebSocketConnection() { // Client side setting up
    }

    protected Router setUpRouter() {
        return new Router()
                .route("/organizations", OrganizationsLogic.organizationsUiRouterHandler)
                .route("/cart/:cartUuid", CartLogic.cartUiRouterHandler);
    }

    @Override
    public void onStart() {
        router.start();
    }
}
