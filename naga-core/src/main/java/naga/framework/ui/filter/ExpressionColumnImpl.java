package naga.framework.ui.filter;

import naga.commons.type.Type;
import naga.framework.expression.Expression;
import naga.framework.ui.format.Formatter;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.domainmodel.Label;
import naga.toolkit.spi.display.DisplayColumn;

/**
 * @author Bruno Salmon
 */
class ExpressionColumnImpl implements ExpressionColumn {

    private final String expressionDefinition;
    private Expression  expression;
    private final Formatter expressionFormatter;
    private Object label;
    private DisplayColumn displayColumn;

    ExpressionColumnImpl(String expressionDefinition, Expression expression, Object label, Formatter expressionFormatter, DisplayColumn displayColumn) {
        this.expressionDefinition = expressionDefinition;
        this.expression = expression;
        this.label = label;
        this.expressionFormatter = expressionFormatter;
        this.displayColumn = displayColumn;
    }

    @Override
    public DisplayColumn getDisplayColumn() {
        if (displayColumn == null) {
            if (label == null)
                label = Label.from(expression);
            Type expectedType = expressionFormatter != null ? expressionFormatter.getExpectedFormattedType() : expression.getType();
            displayColumn = DisplayColumn.create(label, expectedType);
        }
        return displayColumn;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public Formatter getExpressionFormatter() {
        return expressionFormatter;
    }

    @Override
    public void parseExpressionDefinitionIfNecessary(DomainModel domainModel, Object domainClassId) {
        if (expression == null)
            expression = domainModel.parseExpression(expressionDefinition, domainClassId);
    }

}
