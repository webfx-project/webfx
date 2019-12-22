package mongoose.client.businessdata.feesgroup;

import mongoose.client.aggregates.event.EventAggregate;
import mongoose.client.businesslogic.feesgroup.FeesGroupLogic;
import mongoose.shared.entities.Event;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.platform.shared.util.async.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class FeesGroupsByEventStore {

    private final static Map<EntityId, FeesGroup[]> feesGroupsByEventMap = new HashMap<>();

    public static FeesGroup[] getEventFeesGroups(EventAggregate eventAggregate) {
        return getEventFeesGroups(eventAggregate.getEvent());
    }

    public static FeesGroup[] getEventFeesGroups(Event event) {
        return getEventFeesGroups(event.getId());
    }

    public static FeesGroup[] getEventFeesGroups(EntityId eventId) {
        FeesGroup[] feesGroups = feesGroupsByEventMap.get(eventId);
        if (feesGroups == null)
            feesGroupsByEventMap.put(eventId, feesGroups = FeesGroupLogic.createFeesGroups(EventAggregate.get(eventId)));
        return feesGroups;
    }

    public static Future<FeesGroup[]> onEventFeesGroups(EventAggregate eventAggregate) {
        return eventAggregate.onEvent().compose(FeesGroupsByEventStore::onEventFeesGroups);
    }

    public static Future<FeesGroup[]> onEventFeesGroups(Event event) {
        return onEventFeesGroups(event.getId());
    }

    public static Future<FeesGroup[]> onEventFeesGroups(EntityId eventId) {
        FeesGroup[] feesGroups = feesGroupsByEventMap.get(eventId);
        if (feesGroups != null)
            return Future.succeededFuture(feesGroups);
        return EventAggregate.get(eventId).onEventOptions().map(() -> getEventFeesGroups(eventId));
    }

}
