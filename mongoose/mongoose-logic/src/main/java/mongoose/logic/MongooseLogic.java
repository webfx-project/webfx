package mongoose.logic;

import mongoose.logic.cart.CartLogic;
import mongoose.logic.organizations.OrganizationsLogic;
import naga.core.ngui.routing.UiRouter;
import naga.core.spi.bus.client.WebSocketBusOptions;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class MongooseLogic {

    public static void runFrontendApplication() {
        setUpRoutes();
        UiRouter.get().defaultPath("/cart").start();
    }

    public static void runBackendApplication() {
        setUpRoutes();
        UiRouter.get().defaultPath("/organizations").start();
    }

    public static void setUpWebSocketConnection() { // Client side setting up
    }

    private static void setUpRoutes() {
        UiRouter uiRouter = UiRouter.get();
        uiRouter.route("/organizations").handler(OrganizationsLogic.organizationsUiRouterHandler);
        uiRouter.route("/cart").handler(CartLogic.cartUiRouterHandler);
    }

}
