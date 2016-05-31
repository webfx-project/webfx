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

    /**
     *  Implementing equals() and hashCode() so HistoryLocation can be identified in the history from a descriptor.
     *  So we consider here only the pathname, the search and the hash.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryLocationDescriptorImpl)) return false;

        HistoryLocationDescriptorImpl that = (HistoryLocationDescriptorImpl) o;

        if (pathName != null ? !pathName.equals(that.pathName) : that.pathName != null) return false;
        if (search != null ? !search.equals(that.search) : that.search != null) return false;
        return hash != null ? hash.equals(that.hash) : that.hash == null;

    }

    @Override
    public int hashCode() {
        int result = pathName != null ? pathName.hashCode() : 0;
        result = 31 * result + (search != null ? search.hashCode() : 0);
        result = 31 * result + (hash != null ? hash.hashCode() : 0);
        return result;
    }
}
