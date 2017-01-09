package naga.fx.sun.event;

import naga.fx.event.Event;
import naga.fx.event.EventHandler;
import naga.fx.event.WeakEventHandler;

final class CompositeEventHandler<T extends Event> {
    private EventProcessorRecord<T> firstRecord;
    private EventProcessorRecord<T> lastRecord;

    private EventHandler<? super T> eventHandler;

    void setEventHandler(final EventHandler<? super T> eventHandler) {
        this.eventHandler = eventHandler;
    }

    EventHandler<? super T> getEventHandler() {
        return eventHandler;
    }

    void addEventHandler(final EventHandler<? super T> eventHandler) {
        if (find(eventHandler, false) == null)
            append(lastRecord, createEventHandlerRecord(eventHandler));
    }

    void removeEventHandler(final EventHandler<? super T> eventHandler) {
        EventProcessorRecord<T> record = find(eventHandler, false);
        if (record != null)
            remove(record);
    }

    void addEventFilter(final EventHandler<? super T> eventFilter) {
        if (find(eventFilter, true) == null)
            append(lastRecord, createEventFilterRecord(eventFilter));
    }

    void removeEventFilter(final EventHandler<? super T> eventFilter) {
        EventProcessorRecord<T> record = find(eventFilter, true);
        if (record != null)
            remove(record);
    }

    void dispatchBubblingEvent(final Event event) {
        T specificEvent = (T) event;

        EventProcessorRecord<T> record = firstRecord;
        while (record != null) {
            if (record.isDisconnected())
                remove(record);
            else
                record.handleBubblingEvent(specificEvent);
            record = record.nextRecord;
        }

        if (eventHandler != null)
            eventHandler.handle(specificEvent);
    }

    void dispatchCapturingEvent(final Event event) {
        T specificEvent = (T) event;

        EventProcessorRecord<T> record = firstRecord;
        while (record != null) {
            if (record.isDisconnected())
                remove(record);
            else
                record.handleCapturingEvent(specificEvent);
            record = record.nextRecord;
        }
    }

    /* Used for testing. */
    boolean containsHandler(final EventHandler<? super T> eventHandler) {
        return find(eventHandler, false) != null;
    }

    /* Used for testing. */
    boolean containsFilter(final EventHandler<? super T> eventFilter) {
        return find(eventFilter, true) != null;
    }

    private EventProcessorRecord<T> createEventHandlerRecord(EventHandler<? super T> eventHandler) {
        return (eventHandler instanceof WeakEventHandler)
                ? new WeakEventHandlerRecord(
                (WeakEventHandler<? super T>) eventHandler)
                : new NormalEventHandlerRecord(eventHandler);
    }

    private EventProcessorRecord<T> createEventFilterRecord(EventHandler<? super T> eventFilter) {
        return (eventFilter instanceof WeakEventHandler)
                ? new WeakEventFilterRecord(
                (WeakEventHandler<? super T>) eventFilter)
                : new NormalEventFilterRecord(eventFilter);
    }

    private void remove(EventProcessorRecord<T> record) {
        EventProcessorRecord<T> prevRecord = record.prevRecord;
        EventProcessorRecord<T> nextRecord = record.nextRecord;

        if (prevRecord != null)
            prevRecord.nextRecord = nextRecord;
        else
            firstRecord = nextRecord;

        if (nextRecord != null)
            nextRecord.prevRecord = prevRecord;
        else
            lastRecord = prevRecord;

        // leave record.nextRecord set
    }

    private void append(EventProcessorRecord<T> prevRecord, EventProcessorRecord<T> newRecord) {
        EventProcessorRecord<T> nextRecord;
        if (prevRecord != null) {
            nextRecord = prevRecord.nextRecord;
            prevRecord.nextRecord = newRecord;
        } else {
            nextRecord = firstRecord;
            firstRecord = newRecord;
        }

        if (nextRecord != null)
            nextRecord.prevRecord = newRecord;
        else
            lastRecord = newRecord;

        newRecord.prevRecord = prevRecord;
        newRecord.nextRecord = nextRecord;
    }

    private EventProcessorRecord<T> find(EventHandler<? super T> eventProcessor, boolean isFilter) {
        EventProcessorRecord<T> record = firstRecord;
        while (record != null) {
            if (record.isDisconnected())
                remove(record);
            else if (record.stores(eventProcessor, isFilter))
                return record;

            record = record.nextRecord;
        }

        return null;
    }

    private static abstract class EventProcessorRecord<T extends Event> {
        private EventProcessorRecord<T> nextRecord;
        private EventProcessorRecord<T> prevRecord;

        public abstract boolean stores(EventHandler<? super T> eventProcessor, boolean isFilter);

        public abstract void handleBubblingEvent(T event);

        public abstract void handleCapturingEvent(T event);

        public abstract boolean isDisconnected();
    }

    private static final class NormalEventHandlerRecord<T extends Event> extends EventProcessorRecord<T> {
        private final EventHandler<? super T> eventHandler;

        NormalEventHandlerRecord(EventHandler<? super T> eventHandler) {
            this.eventHandler = eventHandler;
        }

        @Override
        public boolean stores(EventHandler<? super T> eventProcessor, boolean isFilter) {
            return !isFilter && (this.eventHandler == eventProcessor);
        }

        @Override
        public void handleBubblingEvent(final T event) {
            eventHandler.handle(event);
        }

        @Override
        public void handleCapturingEvent(final T event) {
        }

        @Override
        public boolean isDisconnected() {
            return false;
        }
    }

    private static final class WeakEventHandlerRecord<T extends Event>  extends EventProcessorRecord<T> {
        private final WeakEventHandler<? super T> weakEventHandler;

        WeakEventHandlerRecord(WeakEventHandler<? super T> weakEventHandler) {
            this.weakEventHandler = weakEventHandler;
        }

        @Override
        public boolean stores(EventHandler<? super T> eventProcessor, boolean isFilter) {
            return !isFilter && (weakEventHandler == eventProcessor);
        }

        @Override
        public void handleBubblingEvent(final T event) {
            weakEventHandler.handle(event);
        }

        @Override
        public void handleCapturingEvent(final T event) {
        }

        @Override
        public boolean isDisconnected() {
            return weakEventHandler.wasGarbageCollected();
        }
    }

    private static final class NormalEventFilterRecord<T extends Event> extends EventProcessorRecord<T> {
        private final EventHandler<? super T> eventFilter;

        NormalEventFilterRecord(EventHandler<? super T> eventFilter) {
            this.eventFilter = eventFilter;
        }

        @Override
        public boolean stores(EventHandler<? super T> eventProcessor, boolean isFilter) {
            return isFilter && (this.eventFilter == eventProcessor);
        }

        @Override
        public void handleBubblingEvent(final T event) {
        }

        @Override
        public void handleCapturingEvent(final T event) {
            eventFilter.handle(event);
        }

        @Override
        public boolean isDisconnected() {
            return false;
        }
    }

    private static final class WeakEventFilterRecord<T extends Event> extends EventProcessorRecord<T> {
        private final WeakEventHandler<? super T> weakEventFilter;

        WeakEventFilterRecord(WeakEventHandler<? super T> weakEventFilter) {
            this.weakEventFilter = weakEventFilter;
        }

        @Override
        public boolean stores(EventHandler<? super T> eventProcessor, boolean isFilter) {
            return isFilter && (weakEventFilter == eventProcessor);
        }

        @Override
        public void handleBubblingEvent(final T event) {
        }

        @Override
        public void handleCapturingEvent(final T event) {
            weakEventFilter.handle(event);
        }

        @Override
        public boolean isDisconnected() {
            return weakEventFilter.wasGarbageCollected();
        }
    }
}
