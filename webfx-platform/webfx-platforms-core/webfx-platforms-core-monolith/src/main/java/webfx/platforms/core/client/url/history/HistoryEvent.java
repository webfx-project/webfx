package webfx.platforms.core.client.url.history;

/**
 * An history event describes the type of change that has been done to a URL. Possible values are:
 * PUSHED – indicates a new item was added to the history
 * REPLACED – indicates the current item in history was altered
 * POPPED – indicates there is a new current item, i.e. the "current pointer" changed
 *
 * @author Bruno Salmon
 */
public enum HistoryEvent {
    PUSHED, REPLACED, POPPED
}
