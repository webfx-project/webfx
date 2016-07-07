package naga.core.ui.displayresultset.impl;

import naga.core.format.Formatter;
import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.domainmodel.Label;
import naga.core.orm.expression.Expression;
import naga.core.type.Type;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.ExpressionColumn;

/**
 * @author Bruno Salmon
 */
public class ExpressionColumnImpl implements ExpressionColumn {

    private final String expressionDefinition;
    private Expression  expression;
    private final Formatter expressionFormatter;
    private Object label;
    private DisplayColumn displayColumn;

    public ExpressionColumnImpl(String expressionDefinition, Expression expression, Object label, Formatter expressionFormatter, DisplayColumn displayColumn) {
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
