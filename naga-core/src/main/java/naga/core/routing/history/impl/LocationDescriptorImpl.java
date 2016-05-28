package naga.core.routing.history.impl;

import naga.core.routing.history.LocationDescriptor;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class LocationDescriptorImpl implements LocationDescriptor {

    private final String pathName;
    private final String search;
    private final Map state;

    public LocationDescriptorImpl(String path) {
        this(path, null, null);
    }

    public LocationDescriptorImpl(LocationDescriptor location) {
        this(location.getPathName(), location.getSearch(), location.getState());
    }

    public LocationDescriptorImpl(String pathName, String search, Map state) {
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
    public Map getState() {
        return state;
    }
}
