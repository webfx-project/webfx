package dev.webfx.platform.client.services.windowhistory.spi.impl;

import dev.webfx.platform.client.services.windowlocation.spi.PathStateLocation;
import dev.webfx.platform.client.services.windowlocation.spi.impl.PathStateLocationImpl;
import dev.webfx.platform.client.services.windowhistory.spi.BrowsingHistoryEvent;
import dev.webfx.platform.client.services.windowhistory.spi.BrowsingHistoryLocation;

/**
 * @author Bruno Salmon
 */
public final class BrowsingHistoryLocationImpl extends PathStateLocationImpl implements BrowsingHistoryLocation {

    private BrowsingHistoryEvent event;
    private final String key;

    public BrowsingHistoryLocationImpl(PathStateLocation location, BrowsingHistoryEvent event, String key) {
        super(location);
        this.event = event;
        this.key = key;
    }

    public void setEvent(BrowsingHistoryEvent event) {
        this.event = event;
    }

    @Override
    public BrowsingHistoryEvent getEvent() {
        return event;
    }

    @Override
    public String getLocationKey() {
        return key;
    }
}
