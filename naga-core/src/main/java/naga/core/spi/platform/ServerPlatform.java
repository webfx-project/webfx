package naga.core.spi.platform;

import naga.core.activity.ActivityManager;

/**
 * @author Bruno Salmon
 */
public interface ServerPlatform {

    void startServerActivity(ActivityManager serverActivityManager);

    /*** Static access ***/

    static ServerPlatform get() {
        return (ServerPlatform) Platform.get();
    }

}
