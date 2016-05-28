package naga.core.routing;

import naga.core.routing.history.History;
import naga.core.routing.history.Location;
import naga.core.routing.history.impl.MemoryHistory;
import naga.core.routing.router.Router;

/**
 * @author Bruno Salmon
 */
public class HistoryRouter {

    protected final Router router;
    protected final History history;

    public HistoryRouter() {
        this(Router.create());
    }

    public HistoryRouter(Router router) {
        this(router, new MemoryHistory());
    }

    public HistoryRouter(Router router, History history) {
        this.router = router;
        this.history = history;
    }

    public Router getRouter() {
        return router;
    }

    public History getHistory() {
        return history;
    }

    public void start() {
        history.listen(this::onNewHistoryLocation);
        Location currentLocation = history.getCurrentLocation();
        if (currentLocation != null)
            onNewHistoryLocation(currentLocation);
    }

    protected void onNewHistoryLocation(Location location) {
        router.accept(history.createPath(location));
    }

}
