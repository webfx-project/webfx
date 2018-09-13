package webfx.framework.ui.uirouter;

import webfx.platforms.core.services.browsinghistory.spi.BrowsingHistory;
import webfx.platforms.core.services.browsinghistory.spi.BrowsingHistoryLocation;
import webfx.framework.router.Router;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public class HistoryRouter {

    protected final Router router;
    protected BrowsingHistory history;
    // The default path to be used if the history is initially empty or the path is not found
    private String defaultInitialHistoryPath;

    public HistoryRouter(Router router, BrowsingHistory history) {
        this.router = router;
        this.history = history;
        router.exceptionHandler(new Handler<Throwable>() {
            @Override
            public void handle(Throwable throwable) {
                Logger.log("Path not found", throwable);
                router.exceptionHandler(null); // removing the handler to avoid an infinite recursion if the default path can't be found
                replaceCurrentHistoryWithInitialDefaultPath();
                router.exceptionHandler(this); // restoring the handler
            }
        });
    }

    public Router getRouter() {
        return router;
    }

    public BrowsingHistory getHistory() {
        return history;
    }

    protected void setHistory(BrowsingHistory history) {
        this.history = history;
    }

    public String getDefaultInitialHistoryPath() {
        return defaultInitialHistoryPath;
    }

    public void setDefaultInitialHistoryPath(String defaultInitialHistoryPath) {
        this.defaultInitialHistoryPath = defaultInitialHistoryPath;
    }

    private void replaceCurrentHistoryWithInitialDefaultPath() {
        if (defaultInitialHistoryPath != null)
            history.replace(defaultInitialHistoryPath);
    }

    public void start() {
        BrowsingHistoryLocation currentLocation = history.getCurrentLocation();
        history.listen(this::onNewHistoryLocation);
        if (currentLocation != null)
            onNewHistoryLocation(currentLocation);
        else
            replaceCurrentHistoryWithInitialDefaultPath();
    }

    public void refresh() {
        onNewHistoryLocation(history.getCurrentLocation());
    }

    protected void onNewHistoryLocation(BrowsingHistoryLocation browsingHistoryLocation) {
        router.accept(history.getPath(browsingHistoryLocation), browsingHistoryLocation.getState());
    }

}
