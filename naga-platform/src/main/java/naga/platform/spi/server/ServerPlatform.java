package naga.platform.spi.server;

import naga.platform.activity.ActivityManager;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public interface ServerPlatform {

    void makeActivityManagerDrivenByServer(ActivityManager activityManager);

    /*** Static access ***/

    static ServerPlatform get() {
        return (ServerPlatform) Platform.get();
    }

}
