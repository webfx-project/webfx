package webfx.platform.client.url.location.impl;

import webfx.platform.services.json.JsonObject;
import webfx.platform.client.url.location.PathLocation;
import webfx.platform.client.url.location.PathStateLocation;

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
