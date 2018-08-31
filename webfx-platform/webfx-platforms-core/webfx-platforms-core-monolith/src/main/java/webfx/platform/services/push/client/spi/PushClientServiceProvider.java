package webfx.platform.services.push.client.spi;

import webfx.platform.bus.Registration;

/**
 * @author Bruno Salmon
 */
public interface PushClientServiceProvider {

    Registration listenServerPushCalls(Object pushClientId);

}
