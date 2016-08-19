package naga.framework.ui.filter;

import naga.commons.type.Type;
import naga.commons.type.Types;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.As;
import naga.framework.expression.terms.Dot;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.domainmodel.DomainField;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.ui.format.Formatter;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayColumnBuilder;
import naga.toolkit.display.DisplayStyleBuilder;

/**
 * @author Bruno Salmon
 */
class ExpressionColumnImpl implements ExpressionColumn {

    private final String expressionDefinition;
    private Expression  expression;
    private Expression displayExpression;
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
            Expression topRightExpression = getTopRightExpression(expression);
            if (topRightExpression instanceof As)
                topRightExpression = ((As) topRightExpression).getOperand();
            if (label == null)
                label = topRightExpression;
            Double prefWidth = null;
            if (topRightExpression instanceof DomainField) {
                int fieldPrefWidth = ((DomainField) topRightExpression).getPrefWidth();
                if (fieldPrefWidth > 0)
                    prefWidth = (double) fieldPrefWidth;
            }
            Type displayType;
            if (expressionFormatter != null)
                displayType = expressionFormatter.getExpectedFormattedType();
            else {
                if (displayExpression != expression)
                    topRightExpression = getTopRightExpression(displayExpression);
                displayType = topRightExpression.getType();
            }
            String textAlign = Types.isNumberType(displayExpression.getType()) ? "right" : null;
            displayColumn = DisplayColumnBuilder.create(label, displayType)
                    .setStyle(DisplayStyleBuilder.create().setPrefWidth(prefWidth).setTextAlign(textAlign).build())
                    .build();
        }
        return displayColumn;
    }

    private static Expression getTopRightExpression(Expression expression) {
        Expression topRightExpression = expression;
        while (topRightExpression instanceof Dot)
            topRightExpression = ((Dot) topRightExpression).getRight();
        if (topRightExpression instanceof DomainField && ((DomainField) topRightExpression).getExpression() instanceof As) // to have 'acco' label instead of 'listing_acco'
            topRightExpression = ((DomainField) topRightExpression).getExpression();
        return topRightExpression;
    }

    @Override
    public Expression getExpression() {
        if (displayExpression == null) {
            displayExpression = expression;
            Expression topRightExpression = getTopRightExpression(expression);
            if (topRightExpression instanceof DomainField) {
                DomainClass foreignClass = ((DomainField) topRightExpression).getForeignClass();
                if (foreignClass != null) {
                    Expression foreignFields = foreignClass.getForeignFields();
                    if (foreignFields != null)
                        displayExpression = new Dot(expression, foreignFields);
                }
            }
        }
        return displayExpression;
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
