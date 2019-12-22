package webfx.framework.client.orm.reactive.mapping.entities_to_grid;

import webfx.extras.type.DerivedType;
import webfx.extras.type.PrimType;
import webfx.extras.type.Type;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.As;
import webfx.framework.shared.orm.expression.terms.Dot;
import webfx.framework.shared.orm.expression.terms.function.Call;
import webfx.framework.shared.orm.domainmodel.formatter.ValueFormatter;
import webfx.framework.shared.orm.domainmodel.formatter.FormatterRegistry;
import webfx.platform.shared.services.json.JsonObject;

import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public class EntityColumnImpl<E extends Entity> implements EntityColumn<E> {

    private final String expressionDefinition;
    protected Expression<E> expression;
    protected Expression<E> displayExpression;
    protected ValueFormatter displayFormatter;
    protected Object label;
    protected final JsonObject json;
    private Boolean isForeignObject;
    private DomainClass domainClass;
    private DomainClass foreignClass;
    private String foreignAlias;
    private Expression<?> foreignFields;
    private String foreignCondition;
    private String foreignOrderBy;
    private String foreignSearchCondition;
    private Expression<E> applicableCondition;

    protected EntityColumnImpl(String expressionDefinition, Expression<E> expression, Object label, ValueFormatter displayFormatter, JsonObject json) {
        this.expressionDefinition = expressionDefinition;
        this.displayFormatter = displayFormatter;
        this.label = label;
        this.json = json;
        setExpression(expression);
    }

    protected Expression<?> getTopRightExpression() {
        return getTopRightExpression(expression);
    }

    protected static Expression<?> getTopRightExpression(Expression<?> expression) {
        while (true) {
            if (expression instanceof Call) {
                Call<?> call = (Call<?>) expression;
                if (call.getFunction().isIdentity())
                    expression = call.getOperand();
            }
            Expression<?> forwardingTypeExpression = expression.getForwardingTypeExpression();
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

    private void setExpression(Expression<E> expression) {
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
    public Expression<E> getExpression() {
        return expression;
    }

    @Override
    public DomainClass getForeignClass() {
        if (isForeignObject == null) {
            Expression<?> topRightExpression = expression.getFinalForwardingTypeExpression();
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
    public Expression<?> getForeignColumns() {
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
    public Expression<E> getApplicableCondition() {
        return applicableCondition = getForeignJsonOrDomainAttribute(applicableCondition, "applicableCondition", null, DomainField::getApplicableCondition, true);
    }

    private <A> A getForeignJsonOrDomainAttribute(A currentAttribute, String jsonKey, Function<DomainClass, ?> classAttributeGetter, Function<DomainField, ?> fieldAttributeGetter, boolean expression) {
        DomainClass domainClass = jsonKey.startsWith("foreign") ? getForeignClass() : this.domainClass;
        if (currentAttribute == null && domainClass != null) {
            Object attributeSource = json == null ? null : json.getString(jsonKey);
            if (attributeSource == null && classAttributeGetter != null)
                attributeSource = classAttributeGetter.apply(domainClass);
            if (attributeSource == null && fieldAttributeGetter != null) {
                Expression<?> topRightExpression = getTopRightExpression();
                if (topRightExpression instanceof DomainField)
                    attributeSource = fieldAttributeGetter.apply((DomainField) topRightExpression);
            }
            if (attributeSource != null)
                currentAttribute = expression && attributeSource instanceof String ? (A) domainClass.parseExpression((String) attributeSource) : (A) attributeSource;
        }
        return currentAttribute;
    }

    @Override
    public Expression<E> getDisplayExpression() {
        if (displayExpression == null)
            displayExpression = getForeignColumns() == null ? expression : new Dot<>(expression, foreignFields);
        return displayExpression;
    }

    @Override
    public ValueFormatter getDisplayFormatter() {
        return displayFormatter;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return json != null && Boolean.TRUE.equals(json.getBoolean("readOnly")) || expression == null || !expression.isEditable();
    }

    @Override
    public EntityColumn<E> parseExpressionDefinitionIfNecessary(DomainModel domainModel, Object domainClassId) {
        if (domainClass == null)
            domainClass = domainModel.getClass(domainClassId);
        if (expression == null)
            setExpression(domainModel.parseExpression(expressionDefinition, domainClassId));
        return this;
    }
}
