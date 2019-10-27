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
import webfx.extras.cell.renderer.ValueRenderer;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualColumnBuilder;
import webfx.extras.visual.VisualStyleBuilder;
import webfx.extras.type.DerivedType;
import webfx.extras.type.PrimType;
import webfx.extras.type.Type;
import webfx.extras.type.Types;
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
    private VisualColumn visualColumn;
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

    ExpressionColumnImpl(String expressionDefinition, Expression expression, Object label, Formatter displayFormatter, VisualColumn visualColumn, JsonObject json) {
        this.expressionDefinition = expressionDefinition;
        this.displayFormatter = displayFormatter;
        this.label = label;
        this.visualColumn = visualColumn;
        this.json = json;
        setExpression(expression);
    }

    @Override
    public VisualColumn getVisualColumn() {
        if (visualColumn == null) {
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
                if (getVisualExpression() != expression)
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
                Type type = getVisualExpression().getType();
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
    public Expression getVisualExpression() {
        if (displayExpression == null)
            displayExpression = getForeignColumns() == null ? expression : new Dot(expression, foreignFields);
        return displayExpression;
    }

    @Override
    public Formatter getVisualFormatter() {
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
