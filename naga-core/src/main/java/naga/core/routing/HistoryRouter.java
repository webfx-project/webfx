package naga.core.routing;

import naga.core.routing.history.History;
import naga.core.routing.history.HistoryLocation;
import naga.core.routing.router.Router;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class HistoryRouter {

    protected final Router router;
    protected final History history;

    public HistoryRouter() {
        this(null);
    }

    public HistoryRouter(Router router) {
        this(router, null);
    }

    public HistoryRouter(Router router, History history) {
        this.router = router != null ? router : Router.create();
        this.history = history != null ? history : Platform.get().getBrowserHistory();
    }

    public Router getRouter() {
        return router;
    }

    public History getHistory() {
        return history;
    }

    public void start() {
        history.listen(this::onNewHistoryLocation);
        HistoryLocation currentLocation = history.getCurrentLocation();
        if (currentLocation != null)
            onNewHistoryLocation(currentLocation);
    }

    protected void onNewHistoryLocation(HistoryLocation location) {
        router.accept(history.createPath(location));
    }

}
