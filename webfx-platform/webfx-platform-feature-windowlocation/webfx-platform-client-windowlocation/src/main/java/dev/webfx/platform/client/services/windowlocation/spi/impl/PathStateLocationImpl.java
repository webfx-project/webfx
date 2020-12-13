package dev.webfx.platform.client.services.windowlocation.spi.impl;

import dev.webfx.platform.client.services.windowlocation.spi.PathLocation;
import dev.webfx.platform.client.services.windowlocation.spi.PathStateLocation;
import dev.webfx.platform.shared.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public class PathStateLocationImpl extends PathLocationImpl implements PathStateLocation {

    private final JsonObject state;

    public PathStateLocationImpl(PathStateLocation pathStateLocation) {
        this(pathStateLocation, pathStateLocation.getState());
    }

    public PathStateLocationImpl(PathLocation pathLocation, JsonObject state) {
        super(pathLocation);
        this.state = state;
    }

    public PathStateLocationImpl(String path, String queryString, String fragment, JsonObject state) {
        super(path, queryString, fragment);
        this.state = state;
    }

    @Override
    public JsonObject getState() {
        return state;
    }
}
