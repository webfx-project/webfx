package mongooses.core.shared.entities.markers;

import mongooses.core.shared.entities.Document;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasDocument {

    void setDocument(Object document);

    EntityId getDocumentId();

    Document getDocument();

}
