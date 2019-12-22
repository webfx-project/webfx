package webfx.platform.server.services.updatelistener;

import webfx.platform.shared.services.update.UpdateArgument;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class UpdateListenerService {

    private static final List<UpdateListener> listeners = new ArrayList<>();

    public static void addUpdateListener(UpdateListener listener) {
        listeners.add(listener);
    }

    public static void removeUpdateListener(UpdateListener listener) {
        listeners.add(listener);
    }

    public static void fireSuccessfulUpdate(UpdateArgument updateArgument) {
        listeners.forEach(l -> l.onSuccessfulUpdate(updateArgument));
    }
}
