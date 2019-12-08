package webfx.framework.shared.orm.domainmodel;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.dql.sqlcompiler.ExpressionSqlCompiler;
import webfx.framework.shared.orm.expression.terms.Equals;
import webfx.framework.shared.orm.expression.terms.HasDomainClass;
import webfx.framework.shared.orm.expression.terms.Parameter;
import webfx.framework.shared.orm.expression.terms.Symbol;
import webfx.extras.label.HasLabel;
import webfx.extras.label.Label;
import webfx.extras.type.Type;

/**
 * @author Bruno Salmon
 */
public final class DomainField extends Symbol implements HasDomainClass, HasLabel {
    private final DomainClass domainClass;
    private final Object modelId;
    private final Object id;
    private final String expressionDefinition;
    private final boolean persistent;
    private final String sqlColumnName;
    private final Label label;
    private final String applicableConditionDefinition;
    private Expression applicableCondition;
    private final int prefWidth;
    private final DomainClass foreignClass;
    private final String foreignAlias;
    private final String foreignCondition;
    private final String foreignAdditionalFields;
    private final String foreignOrderBy;
    private final String foreignComboFields;
    private final String foreignTableFields;
    private DomainField foreignFieldId;

    private final boolean debug = true;

    private Expression fieldEqualsParam;

    static {
        ExpressionSqlCompiler.declareSymbolSubclasses(DomainField.class);
    }

    public DomainField(DomainClass domainClass, Object modelId, Object id, String name, Label label, boolean persistent, Type type, String sqlColumnName, Expression expression, String expressionDefinition, String applicableConditionDefinition, int prefWidth, DomainClass foreignClass, String foreignAlias, String foreignCondition, String foreignAdditionalFields, String foreignOrderBy, String foreignComboFields, String foreignTableFields) {
        super(name, type, expression);
        this.domainClass = domainClass;
        this.modelId = modelId;
        this.id = id;
        this.expressionDefinition = expressionDefinition;
        this.applicableConditionDefinition = applicableConditionDefinition;
        this.persistent = persistent;
        this.sqlColumnName = sqlColumnName;
        this.label = label;
        this.prefWidth = prefWidth;
        this.foreignClass = foreignClass;
        this.foreignAlias = foreignAlias;
        this.foreignCondition = foreignCondition;
        this.foreignAdditionalFields = foreignAdditionalFields;
        this.foreignOrderBy = foreignOrderBy;
        this.foreignComboFields = foreignComboFields;
        this.foreignTableFields = foreignTableFields;
    }

    @Override
    public DomainClass getDomainClass() {
        return domainClass;
    }

    public Object getModelId() {
        return modelId;
    }

    public Object getId() {
        return id;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public String getSqlColumnName() {
        return sqlColumnName;
    }

    public Label getLabel() {
        return label;
    }

    public int getPrefWidth() {
        return prefWidth;
    }

    public DomainClass getForeignClass() {
        return foreignClass;
    }

    public String getForeignAlias() {
        return foreignAlias;
    }

    public String getForeignCondition() {
        return foreignCondition;
    }

    public String getForeignAdditionalFields() {
        return foreignAdditionalFields;
    }

    public String getForeignOrderBy() {
        return foreignOrderBy;
    }

    public String getForeignComboFields() {
        return foreignComboFields;
    }

    public String getForeignTableFields() {
        return foreignTableFields;
    }

    /*
    public DomainField getForeignFieldId() {
        if (foreignFieldId == null && getType().getBaseType() == BaseType.FOREIGN_KEY)
            foreignFieldId = new DomainField(domainClass, modelId, id, name, label, persistent, Type.LONG, sqlColumnName, expression, expressionDefinition, null, -1, null, foreignCondition, foreignAdditionalFields, foreignOrderBy, null, null);
        return foreignFieldId;
    }
    */

    public Expression getExpression() {
        if (expression == null && expressionDefinition != null)
            expression = domainClass.parseExpression(expressionDefinition);
        return expression;
    }

    public String getExpressionDefinition() {
        return expressionDefinition;
    }

    @Override
    public Expression getForwardingTypeExpression() {
        if (expression != null || expressionDefinition != null)
            return getExpression();
        return this;
    }

    @Override
    public Type getType() {
        Expression typeExpression = getForwardingTypeExpression();
        return typeExpression == this ? super.getType() : typeExpression.getType();
    }

    public Expression getApplicableCondition() {
        if (applicableCondition == null && applicableConditionDefinition != null)
            applicableCondition = domainClass.parseExpression(applicableConditionDefinition);
        return applicableCondition;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append(debug ? name : id);
    }


    /*
    @Override
    public void captureParameters(Collection<String> parameterNames, Collection<Object> parameterValues, boolean forSelectClause) {
        if (getExpression() != null)
            expression.captureParameters(parameterNames, parameterValues, forSelectClause);
    }
    */

    public Expression getFieldEqualsParam() {
        if (fieldEqualsParam == null)
            fieldEqualsParam = new Equals(this, Parameter.UNNAMED_PARAMETER);
        return fieldEqualsParam;
    }
}
