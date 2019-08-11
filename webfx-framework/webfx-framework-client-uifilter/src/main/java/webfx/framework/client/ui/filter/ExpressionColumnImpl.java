package webfx.framework.client.ui.filter;

import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.terms.As;
import webfx.framework.shared.expression.terms.Dot;
import webfx.framework.shared.expression.terms.UnaryExpression;
import webfx.framework.shared.expression.terms.function.Call;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.util.formatter.Formatter;
import webfx.framework.shared.util.formatter.FormatterRegistry;
import webfx.framework.shared.util.formatter.NumberFormatter;
import webfx.fxkit.extra.cell.renderer.ValueRenderer;
import webfx.fxkit.extra.displaydata.DisplayColumn;
import webfx.fxkit.extra.displaydata.DisplayColumnBuilder;
import webfx.fxkit.extra.displaydata.DisplayStyleBuilder;
import webfx.fxkit.extra.type.DerivedType;
import webfx.fxkit.extra.type.PrimType;
import webfx.fxkit.extra.type.Type;
import webfx.fxkit.extra.type.Types;
import webfx.platform.shared.services.json.JsonObject;

import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
final class ExpressionColumnImpl implements ExpressionColumn {

    private final String expressionDefinition;
    private Expression  expression;
    private Expression displayExpression;
    private Formatter displayFormatter;
    private Object label;
    private DisplayColumn displayColumn;
    private final JsonObject json;
    private Boolean isForeignObject;
    private DomainClass domainClass;
    private DomainClass foreignClass;
    private String foreignAlias;
    private Expression foreignFields;
    private String foreignCondition;
    private String foreignOrderBy;
    private String foreignSearchCondition;
    private Expression applicableCondition;

    ExpressionColumnImpl(String expressionDefinition, Expression expression, Object label, Formatter displayFormatter, DisplayColumn displayColumn, JsonObject json) {
        this.expressionDefinition = expressionDefinition;
        this.displayFormatter = displayFormatter;
        this.label = label;
        this.displayColumn = displayColumn;
        this.json = json;
        setExpression(expression);
    }

    @Override
    public DisplayColumn getDisplayColumn() {
        if (displayColumn == null) {
            Expression topRightExpression = getTopRightExpression();
            if (topRightExpression instanceof As) {
                As as = (As) topRightExpression;
                topRightExpression = as.getOperand();
                if (label == null)
                    label = as.getAlias();
            }
            if (label == null) {
                label = topRightExpression;
                while (label instanceof UnaryExpression)
                    label = ((UnaryExpression) label).getOperand();
            }
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
            if (displayFormatter == null && Types.isNumberType(displayType))
                displayFormatter = NumberFormatter.SINGLETON;
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
            displayColumn = DisplayColumnBuilder.create(label, displayType)
                    .setStyle(DisplayStyleBuilder.create().setPrefWidth(prefWidth).setTextAlign(textAlign).build())
                    .setRole(role)
                    .setValueRenderer(fxValueRenderer)
                    .setSource(this)
                    .build();
        }
        return displayColumn;
    }

    private Expression getTopRightExpression() {
        return getTopRightExpression(expression);
    }

    private static Expression getTopRightExpression(Expression expression) {
        while (true) {
            if (expression instanceof Call) {
                Call call = (Call) expression;
                if (call.getFunction().isIdentity())
                    expression = call.getOperand();
            }
            Expression forwardingTypeExpression = expression.getForwardingTypeExpression();
            if (forwardingTypeExpression == expression)
                break;
            if (expression instanceof As) { // to have 'acco' label instead of 'listing_acco'
                expression = forwardingTypeExpression;
                break;
            }
            if (expression instanceof Dot)
                expression = forwardingTypeExpression;
            else
                break;
        }
        return expression;
    }

    private void setExpression(Expression expression) {
        this.expression = expression;
        // If no display formatter is passed, trying to find one based on the expression type
        if (displayFormatter == null && expression != null) {
            Type type = expression.getType();
            String formatterName = null;
            // If the type is a derived type (ex: field with type Price), we try to find a formatter with same name (but lowercase). Ex: price
            if (type == PrimType.DATE)
                formatterName = "dateTime";
            else if (type instanceof DerivedType)
                formatterName = ((DerivedType) type).getName().toLowerCase();
            displayFormatter = FormatterRegistry.getFormatter(formatterName);
        }
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public DomainClass getForeignClass() {
        if (isForeignObject == null) {
            Expression topRightExpression = expression.getFinalForwardingTypeExpression();
            if (topRightExpression instanceof DomainField)
                foreignClass = ((DomainField) topRightExpression).getForeignClass();
            isForeignObject = foreignClass != null;
        }
        return foreignClass;
    }

    @Override
    public String getForeignAlias() {
        return foreignAlias = getForeignJsonOrDomainAttribute(foreignAlias, "foreignAlias", null, DomainField::getForeignAlias, false);
    }

    @Override
    public Expression getForeignColumns() {
        return foreignFields = getForeignJsonOrDomainAttribute(foreignFields, "foreignColumns", DomainClass::getForeignFields, null, true);
    }

    @Override
    public String getForeignWhere() {
        return foreignCondition = getForeignJsonOrDomainAttribute(foreignCondition, "foreignWhere", null, DomainField::getForeignCondition, false);
    }

    @Override
    public String getForeignOrderBy() {
        return foreignOrderBy = getForeignJsonOrDomainAttribute(foreignOrderBy, "foreignOrderBy", null, DomainField::getForeignOrderBy, false);
    }

    @Override
    public String getForeignSearchCondition() {
        return foreignSearchCondition = getForeignJsonOrDomainAttribute(foreignSearchCondition, "foreignSearchCondition", DomainClass::getSearchCondition, null, false);
    }

    @Override
    public Expression getApplicableCondition() {
        return applicableCondition = getForeignJsonOrDomainAttribute(applicableCondition, "applicableCondition", null, DomainField::getApplicableCondition, true);
    }

    private <A> A getForeignJsonOrDomainAttribute(A currentAttribute, String jsonKey, Function<DomainClass, ?> classAttributeGetter, Function<DomainField, ?> fieldAttributeGetter, boolean expression) {
        DomainClass domainClass = jsonKey.startsWith("foreign") ? getForeignClass() : this.domainClass;
        if (currentAttribute == null && domainClass != null) {
            Object attributeSource = json == null ? null : json.getString(jsonKey);
            if (attributeSource == null && classAttributeGetter != null)
                attributeSource = classAttributeGetter.apply(domainClass);
            if (attributeSource == null && fieldAttributeGetter != null) {
                Expression topRightExpression = getTopRightExpression();
                if (topRightExpression instanceof DomainField)
                    attributeSource = fieldAttributeGetter.apply((DomainField) topRightExpression);
            }
            if (attributeSource != null)
                currentAttribute = expression && attributeSource instanceof String ? (A) domainClass.parseExpression((String) attributeSource) : (A) attributeSource;
        }
        return currentAttribute;
    }

    @Override
    public Expression getDisplayExpression() {
        if (displayExpression == null)
            displayExpression = getForeignColumns() == null ? expression : new Dot(expression, foreignFields);
        return displayExpression;
    }

    @Override
    public Formatter getDisplayFormatter() {
        return displayFormatter;
    }

    @Override
    public boolean isReadOnly() {
        return json != null && Boolean.TRUE.equals(json.getBoolean("readOnly")) || expression == null || !expression.isEditable();
    }

    @Override
    public ExpressionColumn parseExpressionDefinitionIfNecessary(DomainModel domainModel, Object domainClassId) {
        if (domainClass == null)
            domainClass = domainModel.getClass(domainClassId);
        if (expression == null)
            setExpression(domainModel.parseExpression(expressionDefinition, domainClassId));
        return this;
    }
}
