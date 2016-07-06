package naga.core.spi.platform;

import naga.core.activity.ActivityManager;

/**
 * @author Bruno Salmon
 */
public interface ServerPlatform {

    void startMicroService(ActivityManager microserviceActivityManager);

    /*** Static access ***/

    static ServerPlatform get() {
        return (ServerPlatform) Platform.get();
    }

}
