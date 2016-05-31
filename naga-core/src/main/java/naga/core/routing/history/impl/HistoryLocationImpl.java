package naga.core.routing.history.impl;

import naga.core.routing.history.HistoryEvent;
import naga.core.routing.history.HistoryLocation;
import naga.core.routing.location.PathStateLocation;
import naga.core.routing.location.impl.PathStateLocationImpl;

/**
 * @author Bruno Salmon
 */
public class HistoryLocationImpl extends PathStateLocationImpl implements HistoryLocation {

    private HistoryEvent event;
    private final String key;

    public HistoryLocationImpl(PathStateLocation location, HistoryEvent event, String key) {
        super(location);
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
