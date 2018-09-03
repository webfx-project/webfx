package webfx.platforms.core.client.url.history.baseimpl;

import webfx.platforms.core.client.url.history.HistoryEvent;
import webfx.platforms.core.client.url.history.HistoryLocation;
import webfx.platforms.core.client.url.location.PathStateLocation;
import webfx.platforms.core.client.url.location.impl.PathStateLocationImpl;

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
