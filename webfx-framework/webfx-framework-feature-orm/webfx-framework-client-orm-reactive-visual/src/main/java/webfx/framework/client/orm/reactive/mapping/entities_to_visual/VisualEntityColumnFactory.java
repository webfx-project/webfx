package webfx.framework.client.orm.reactive.mapping.entities_to_visual;

import webfx.extras.visual.VisualColumn;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumnFactory;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.domainmodel.formatter.ValueFormatter;
import webfx.platform.shared.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public interface VisualEntityColumnFactory extends EntityColumnFactory {

    VisualEntityColumnFactory DEFAULT = new VisualEntityColumnFactory() {};

    static VisualEntityColumnFactory get() {
        return DEFAULT;
    }

    default <E extends Entity> VisualEntityColumn<E> create(String expressionDefinition, Expression<E> expression, Object label, ValueFormatter displayFormatter, JsonObject json) {
        return create(expressionDefinition, expression, label, displayFormatter, null, json);
    }

    default <E extends Entity> VisualEntityColumn<E> create(String expressionDefinition, Expression<E> expression, Object label, ValueFormatter displayFormatter, VisualColumn visualColumn, JsonObject json) {
        return new VisualEntityColumnImpl<>(expressionDefinition, expression, label, displayFormatter, visualColumn, json);
    }

    default <E extends Entity> VisualEntityColumn<E> create(Expression<E> expression, VisualColumn visualColumn) {
        return create(null, expression, null, null, visualColumn, null);
    }

    @Override
    default <E extends Entity> VisualEntityColumn<E>[] createArray(int size) {
        return new VisualEntityColumn[size];
    }
}
