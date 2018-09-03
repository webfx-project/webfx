package webfx.framework.ui.filter;

import webfx.fxkits.extra.type.Type;
import webfx.fxkits.extra.type.Types;
import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.As;
import webfx.framework.expression.terms.Dot;
import webfx.framework.orm.domainmodel.DomainClass;
import webfx.framework.orm.domainmodel.DomainField;
import webfx.framework.orm.domainmodel.DomainModel;
import webfx.framework.ui.formatter.Formatter;
import webfx.platforms.core.services.json.JsonObject;
import webfx.fxkits.extra.displaydata.DisplayColumn;
import webfx.fxkits.extra.displaydata.DisplayColumnBuilder;
import webfx.fxkits.extra.displaydata.DisplayStyleBuilder;
import webfx.fxkits.extra.cell.renderer.ValueRenderer;

/**
 * @author Bruno Salmon
 */
class ExpressionColumnImpl implements ExpressionColumn {

    private final String expressionDefinition;
    private Expression  expression;
    private Expression displayExpression;
    private final Formatter displayFormatter;
    private Object label;
    private DisplayColumn displayColumn;
    private JsonObject json;
    private Boolean isForeignObject;
    private DomainClass foreignClass;
    private Expression foreignFields;
    private String foreignCondition;
    private String foreignSearchCondition;

    ExpressionColumnImpl(String expressionDefinition, Expression expression, Object label, Formatter displayFormatter, DisplayColumn displayColumn, JsonObject json) {
        this.expressionDefinition = expressionDefinition;
        this.expression = expression;
        this.label = label;
        this.displayFormatter = displayFormatter;
        this.displayColumn = displayColumn;
        this.json = json;
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
            if (displayFormatter != null)
                displayType = displayFormatter.getOutputType();
            else {
                if (getDisplayExpression() != expression)
                    topRightExpression = getTopRightExpression(displayExpression);
                displayType = topRightExpression.getType();
            }
            String textAlign = null;
            ValueRenderer fxValueRenderer = null;
            String role = null;
            if (json != null) {
                textAlign = json.getString("textAlign");
                String collator = json.getString("collator");
                if (collator != null)
                    fxValueRenderer = ValueRenderer.create(displayType, collator);
                role = json.getString("role");
                //json = null;
            }
            if (textAlign == null) {
                Type type = getDisplayExpression().getType();
                textAlign = Types.isNumberType(type) ? "right" : Types.isBooleanType(type) ? "center" : null;
            }
            displayColumn = DisplayColumnBuilder.create(label, displayType)
                    .setStyle(DisplayStyleBuilder.create().setPrefWidth(prefWidth).setTextAlign(textAlign).build())
                    .setRole(role)
                    .setValueRenderer(fxValueRenderer)
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
        return expression;
    }

    @Override
    public DomainClass getForeignClass() {
        if (isForeignObject == null) {
            Expression topRightExpression = getTopRightExpression(expression);
            if (topRightExpression instanceof DomainField)
                foreignClass = ((DomainField) topRightExpression).getForeignClass();
            isForeignObject = foreignClass != null;
        }
        return foreignClass;
    }

    @Override
    public Expression getForeignFields() {
        if (foreignFields == null && getForeignClass() != null) {
            String localDef = json == null ? null : json.getString("foreignFields");
            foreignFields = localDef == null ? foreignClass.getForeignFields() : foreignClass.parseExpression(localDef);
        }
        return foreignFields;
    }

    @Override
    public String getForeignCondition() {
        if (foreignCondition == null && getForeignClass() != null) {
            foreignCondition = json == null ? null : json.getString("foreignCondition");
            if (foreignCondition == null) {
                Expression topRightExpression = getTopRightExpression(expression);
                if (topRightExpression instanceof DomainField)
                    foreignCondition = ((DomainField) topRightExpression).getForeignCondition();
            }
        }
        return foreignCondition;
    }

    @Override
    public String getForeignSearchCondition() {
        if (foreignSearchCondition == null && getForeignClass() != null) {
            String localDef = json == null ? null : json.getString("foreignSearchCondition");
            foreignSearchCondition = localDef == null ? foreignClass.getSearchCondition() : localDef;
        }
        return foreignSearchCondition;
    }

    @Override
    public Expression getDisplayExpression() {
        if (displayExpression == null)
            displayExpression = getForeignFields() == null ? expression : new Dot(expression, foreignFields);
        return displayExpression;
    }

    @Override
    public Formatter getDisplayFormatter() {
        return displayFormatter;
    }

    @Override
    public ExpressionColumn parseExpressionDefinitionIfNecessary(DomainModel domainModel, Object domainClassId) {
        if (expression == null)
            expression = domainModel.parseExpression(expressionDefinition, domainClassId);
        return this;
    }

}
