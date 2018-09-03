package webfx.platforms.core.services.push.client.spi;

import webfx.platforms.core.bus.Registration;

/**
 * @author Bruno Salmon
 */
public interface PushClientServiceProvider {

    Registration listenServerPushCalls(Object pushClientId);

}
