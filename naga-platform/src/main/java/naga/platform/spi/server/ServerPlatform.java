package naga.platform.spi.server;

import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public interface ServerPlatform {

    void startServerModule(ServerModule serverModule);

    /*** Static access ***/

    static ServerPlatform get() {
        return (ServerPlatform) Platform.get();
    }

}
