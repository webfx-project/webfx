package webfx.framework.client.orm.reactive.mapping.entities_to_visual;

import webfx.extras.cell.renderer.ValueRenderer;
import webfx.extras.type.Type;
import webfx.extras.type.Types;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualColumnBuilder;
import webfx.extras.visual.VisualStyleBuilder;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumnImpl;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.As;
import webfx.framework.shared.orm.expression.terms.UnaryExpression;
import webfx.framework.shared.orm.domainmodel.formatter.ValueFormatter;
import webfx.platform.shared.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public class VisualEntityColumnImpl<E extends Entity> extends EntityColumnImpl<E> implements VisualEntityColumn<E> {

    private VisualColumn visualColumn;

    public VisualEntityColumnImpl(String expressionDefinition, Expression<E> expression, Object label, ValueFormatter displayFormatter, VisualColumn visualColumn, JsonObject json) {
        super(expressionDefinition, expression, label, displayFormatter, json);
        this.visualColumn = visualColumn;
    }

    @Override
    public String getName() {
        return getVisualColumn().getName();
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public VisualColumn getVisualColumn() {
        if (visualColumn == null) {
            Expression<?> topRightExpression = getTopRightExpression();
            if (topRightExpression instanceof As) {
                As<?> as = (As<?>) topRightExpression;
                topRightExpression = as.getOperand();
                if (label == null)
                    label = as.getAlias();
            }
            if (label == null) {
                label = topRightExpression;
                while (label instanceof UnaryExpression)
                    label = ((UnaryExpression<?>) label).getOperand();
            }
            Double prefWidth = null;
            if (topRightExpression instanceof DomainField) {
                int fieldPrefWidth = ((DomainField) topRightExpression).getPrefWidth();
                if (fieldPrefWidth > 0)
                    prefWidth = (double) fieldPrefWidth;
            }
            Type displayType;
            if (displayFormatter != null)
                displayType = displayFormatter.getFormattedValueType();
            else {
                if (getDisplayExpression() != expression)
                    topRightExpression = getTopRightExpression(displayExpression);
                displayType = topRightExpression.getType();
            }
/* Commented as causes a problem with the monitor page which expects numbers
            if (visualFormatter == null && Types.isNumberType(displayType))
                visualFormatter = NumberFormatter.SINGLETON;
*/
            String textAlign = null;
            ValueRenderer fxValueRenderer = null;
            String role = null;
            if (json != null) {
                textAlign = json.getString("textAlign");
                String collator = json.getString("collator");
                if (collator != null)
                    fxValueRenderer = ValueRenderer.create(displayType, collator);
                role = json.getString("role");
                if (json.has("prefWidth"))
                    prefWidth = json.getDouble("prefWidth");
                //json = null;
            }
            if (textAlign == null) {
                Type type = getDisplayExpression().getType();
                textAlign = Types.isNumberType(type) ? "right" : Types.isBooleanType(type) ? "center" : null;
            }
            visualColumn = VisualColumnBuilder.create(label, displayType)
                    .setStyle(VisualStyleBuilder.create().setPrefWidth(prefWidth).setTextAlign(textAlign).build())
                    .setRole(role)
                    .setValueRenderer(fxValueRenderer)
                    .setSource(this)
                    .build();
        }
        return visualColumn;
    }

}
