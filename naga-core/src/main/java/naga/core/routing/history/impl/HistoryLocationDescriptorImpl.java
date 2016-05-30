package naga.core.routing.history.impl;

import naga.core.json.JsonObject;
import naga.core.routing.history.HistoryLocationDescriptor;

/**
 * @author Bruno Salmon
 */
public class HistoryLocationDescriptorImpl implements HistoryLocationDescriptor {

    private final String pathName;
    private final String search;
    private final String hash;
    private final JsonObject state;

    public HistoryLocationDescriptorImpl(HistoryLocationDescriptor location) {
        this(location.getPathName(), location.getSearch(), location.getHash(), location.getState());
    }

    public HistoryLocationDescriptorImpl(String pathName, String search, String hash, JsonObject state) {
        this.pathName = pathName;
        this.search = search;
        this.hash = hash;
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
    public String getHash() {
        return hash;
    }

    @Override
    public JsonObject getState() {
        return state;
    }
}
