package naga.core.routing.history.impl;

import naga.core.routing.history.HistoryEvent;
import naga.core.routing.history.HistoryLocation;
import naga.core.routing.history.HistoryLocationDescriptor;

/**
 * @author Bruno Salmon
 */
public class HistoryLocationImpl extends HistoryLocationDescriptorImpl implements HistoryLocation {

    private HistoryEvent event;
    private final String key;

    public HistoryLocationImpl(HistoryLocationDescriptor location, HistoryEvent event, String key) {
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
