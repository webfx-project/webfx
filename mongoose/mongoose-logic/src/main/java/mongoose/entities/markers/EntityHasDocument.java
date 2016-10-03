package mongoose.entities.markers;

import mongoose.entities.Document;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

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
