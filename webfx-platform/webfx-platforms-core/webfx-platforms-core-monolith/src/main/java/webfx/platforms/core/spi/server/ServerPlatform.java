package webfx.platforms.core.spi.server;

import webfx.platforms.core.spi.Platform;

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
