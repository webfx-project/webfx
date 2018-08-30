package mongoose.entities;

import mongoose.entities.markers.EntityHasDocument;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface History extends Entity, EntityHasDocument {

    default void setUsername(String username) {
        setFieldValue("username", username);
    }

    default String getUsername() {
        return getStringFieldValue("username");
    }

    default void setComment(String comment) {
        setFieldValue("comment", comment);
    }

    default String getComment() {
        return getStringFieldValue("comment");
    }


    default void setMail(Object mail) {
        setForeignField("mail", mail);
    }

    default EntityId getMailId() {
        return getForeignEntityId("mail");
    }

    default Mail getMail() {
        return getForeignEntity("mail");
    }
    
}
