package mongoose.entities.markers;

import mongoose.entities.Document;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasDocument {

    void setDocument(Object document);

    EntityId getDocumentId();

    Document getDocument();

}
