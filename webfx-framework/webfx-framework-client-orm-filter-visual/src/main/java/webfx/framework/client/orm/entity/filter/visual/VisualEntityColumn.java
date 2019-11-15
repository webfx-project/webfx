package webfx.framework.client.orm.entity.filter.visual;

import webfx.extras.visual.VisualColumn;
import webfx.framework.client.orm.entity.filter.table.EntityColumn;
import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface VisualEntityColumn<E extends Entity> extends EntityColumn<E> {

    /**
     * @return the associated visual column.
     */
    VisualColumn getVisualColumn();

}
