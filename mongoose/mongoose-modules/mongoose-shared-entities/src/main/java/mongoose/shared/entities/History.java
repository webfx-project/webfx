package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasDocument;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;

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
