package mongoose.shared.entities.markers;

import mongoose.shared.entities.Document;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface EntityHasDocument extends Entity, HasDocument {

    @Override
    default void setDocument(Object document) {
        setForeignField("document", document);
    }

    @Override
    default EntityId getDocumentId() {
        return getForeignEntityId("document");
    }

    @Override
    default Document getDocument() {
        return getForeignEntity("document");
    }

}
