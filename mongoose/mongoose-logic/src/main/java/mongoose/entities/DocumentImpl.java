package mongoose.entities;

import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class DocumentImpl extends DynamicEntity implements Document {

    public DocumentImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    @Override
    public void setEventId(Object eventId) {
        setForeignField("event", eventId);
    }

    @Override
    public EntityId getEventId() {
        return getForeignEntityId("event");
    }

    @Override
    public Event getEvent() {
        return getForeignEntity("event");
    }

    @Override
    public void setRef(Integer ref) {
        setFieldValue("ref", ref);
    }

    @Override
    public Integer getRef() {
        return getIntegerFieldValue("ref");
    }

    @Override
    public void setPersonFirstName(String personFirstName) {
        setFieldValue("person_firstName", personFirstName);
    }

    @Override
    public String getPersonFirstName() {
        return getStringFieldValue("person_firstName");
    }

    @Override
    public void setPersonLastName(String personLastName) {
        setFieldValue("person_lastName", personLastName);
    }

    @Override
    public String getPersonLastName() {
        return getStringFieldValue("person_lastName");
    }
}
