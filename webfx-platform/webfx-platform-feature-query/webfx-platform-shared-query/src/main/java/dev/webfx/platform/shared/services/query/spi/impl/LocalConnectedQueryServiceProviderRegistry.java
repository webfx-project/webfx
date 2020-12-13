package dev.webfx.platform.shared.services.query.spi.impl;

import dev.webfx.platform.shared.services.query.spi.QueryServiceProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class LocalConnectedQueryServiceProviderRegistry {

    private static final Map</* dataSourceId */ Object, QueryServiceProvider> localConnectedProviders = new HashMap<>();

    public static void registerLocalConnectedProvider(Object dataSourceId, QueryServiceProvider localConnectedProvider) {
        localConnectedProviders.put(dataSourceId, localConnectedProvider);
    }

    public static QueryServiceProvider getLocalConnectedProvider(Object dataSourceId) {
        return localConnectedProviders.get(dataSourceId);
    }
}
