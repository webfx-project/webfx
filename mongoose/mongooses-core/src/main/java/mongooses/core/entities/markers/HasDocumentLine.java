package mongooses.core.entities.markers;

import mongooses.core.entities.DocumentLine;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasDocumentLine {

    void setDocumentLine(Object documentLine);

    EntityId getDocumentLineId();

    DocumentLine getDocumentLine();

}
