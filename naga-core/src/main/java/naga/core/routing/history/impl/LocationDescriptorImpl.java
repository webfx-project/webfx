package naga.core.routing.history.impl;

import naga.core.json.JsonObject;
import naga.core.routing.history.LocationDescriptor;

/**
 * @author Bruno Salmon
 */
public class LocationDescriptorImpl implements LocationDescriptor {

    private final String pathName;
    private final String search;
    private final JsonObject state;

    public LocationDescriptorImpl(String path) {
        this(path, null, null);
    }

    public LocationDescriptorImpl(LocationDescriptor location) {
        this(location.getPathName(), location.getSearch(), location.getState());
    }

    public LocationDescriptorImpl(String pathName, String search, JsonObject state) {
        this.pathName = pathName;
        this.search = search;
        this.state = state;
    }

    @Override
    public String getPathName() {
        return pathName;
    }

    @Override
    public String getSearch() {
        return search;
    }

    @Override
    public JsonObject getState() {
        return state;
    }
}
