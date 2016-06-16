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
    // The default path to be used if the history is initially empty or the path is not found
    private String defaultInitialHistoryPath;

    public HistoryRouter() {
        this(null);
    }

    public HistoryRouter(Router router) {
        this(router, null);
    }

    public HistoryRouter(Router router, History history) {
        this.router = router != null ? router : Router.create();
        this.history = history != null ? history : Platform.get().getBrowserHistory();
        this.router.exceptionHandler(throwable -> {
            Platform.log("Path not found", throwable);
            replaceCurrentHistoryWithInitialDefaultPath();
        });
    }

    public Router getRouter() {
        return router;
    }

    public History getHistory() {
        return history;
    }

    public String getDefaultInitialHistoryPath() {
        return defaultInitialHistoryPath;
    }

    public void setDefaultInitialHistoryPath(String defaultInitialHistoryPath) {
        this.defaultInitialHistoryPath = defaultInitialHistoryPath;
    }

    public void start() {
        HistoryLocation currentLocation = history.getCurrentLocation();
        history.listen(this::onNewHistoryLocation);
        if (currentLocation != null)
            onNewHistoryLocation(currentLocation);
        else
            replaceCurrentHistoryWithInitialDefaultPath();
    }

    private void replaceCurrentHistoryWithInitialDefaultPath() {
        if (defaultInitialHistoryPath != null)
            history.replace(defaultInitialHistoryPath);
    }

    protected void onNewHistoryLocation(HistoryLocation historyLocation) {
        router.accept(history.getPath(historyLocation), historyLocation.getState());
    }

}
