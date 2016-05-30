package naga.core.routing.history.impl;

import naga.core.json.JsonObject;
import naga.core.routing.history.HistoryEvent;
import naga.core.routing.history.Location;
import naga.core.routing.history.LocationDescriptor;

/**
 * @author Bruno Salmon
 */
public class LocationImpl extends LocationDescriptorImpl implements Location {

    private HistoryEvent event;
    private final String key;

    public LocationImpl(String path, HistoryEvent event, String key) {
        this(path, null, null, null, event, key);
    }

    public LocationImpl(LocationDescriptor location, HistoryEvent event, String key) {
        this(location.getPathName(), location.getSearch(), location.getHash(), location.getState(), event, key);
    }

    public LocationImpl(String pathName, String search, String hash, JsonObject state, HistoryEvent event, String key) {
        super(pathName, search, hash, state);
        this.event = event;
        this.key = key;
    }

    public void setEvent(HistoryEvent event) {
        this.event = event;
    }

    @Override
    public HistoryEvent getEvent() {
        return event;
    }

    @Override
    public String getLocationKey() {
        return key;
    }
}
