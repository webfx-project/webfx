package naga.commons.url.history.baseimpl;

import naga.commons.url.history.spi.HistoryEvent;
import naga.commons.url.history.spi.HistoryLocation;
import naga.commons.url.location.PathStateLocation;
import naga.commons.url.location.impl.PathStateLocationImpl;

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
