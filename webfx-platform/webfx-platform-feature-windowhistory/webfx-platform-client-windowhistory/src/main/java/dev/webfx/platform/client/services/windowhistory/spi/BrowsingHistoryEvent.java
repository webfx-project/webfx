package dev.webfx.platform.client.services.windowhistory.spi;

/**
 * An history event describes the type of change that has been done to a URL. Possible values are:
 * PUSHED – indicates a new item was added to the history
 * REPLACED – indicates the current item in history was altered
 * POPPED – indicates there is a new current item, i.e. the "current pointer" changed
 *
 * @author Bruno Salmon
 */
public enum BrowsingHistoryEvent {
    PUSHED, REPLACED, POPPED
}
