package mongooses.core.shared.entities.markers;

import mongooses.core.shared.entities.DocumentLine;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasDocumentLine {

    void setDocumentLine(Object documentLine);

    EntityId getDocumentLineId();

    DocumentLine getDocumentLine();

}
