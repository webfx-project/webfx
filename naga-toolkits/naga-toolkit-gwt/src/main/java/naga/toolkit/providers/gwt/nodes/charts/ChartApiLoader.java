package naga.toolkit.providers.gwt.nodes.charts;

import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * A class responsible for loading the google chart API. It's a wrapper of the google ChartLoader which has the drawback
 * that it can't be used several times (only the first call is working, further calls do nothing, even not calling the
 * callback). This wrapper always call the callback.
 *
 * @author Bruno Salmon
 */
public class ChartApiLoader {

    private static boolean loaded;
    private static List<Runnable> pendingCallbacks;

    public static void onChartApiLoaded(Runnable callback) {
        if (loaded)
            callback.run();
        else {
            boolean firstTime = pendingCallbacks == null;
            if (firstTime)
                pendingCallbacks = new ArrayList<>();
            pendingCallbacks.add(callback);
            if (firstTime)
                // TODO: load only the required packages depending on what GWT compiled (ex: not loading Gauge if not used by the application)
                new ChartLoader(ChartPackage.CORECHART, ChartPackage.GAUGE).loadApi(() -> {
                    loaded = true;
                    for (Runnable pendingCallback : pendingCallbacks)
                        pendingCallback.run();
                    pendingCallbacks = null;
                });
        }
    }
}
