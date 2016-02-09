package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public abstract class SqlOrder {
    protected final Object id;
    protected final Object domainClass;
    protected final String domainClassAlias;

    // object level definition
    protected String definition;
    protected final Expression where;
    protected final ExpressionArray orderBy;
    protected final Expression limit;
    protected boolean cacheable;

    // sql level definition
    protected final String sqlDefinition;

    // common
    protected Object[] parameterValues;

    protected SqlOrder(Object id, Object domainClass, String domainClassAlias, String definition, String sqlDefinition, Object[] parameterValues, Expression where, ExpressionArray orderBy, Expression limit) {
        this.id = id;
        this.domainClass = domainClass;
        this.domainClassAlias = domainClassAlias;
        this.sqlDefinition = sqlDefinition;
        this.definition = definition;
        this.parameterValues = parameterValues;
        this.where = where;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    public Object getId() {
        return id;
    }

    public Object getDomainClass() {
        return domainClass;
    }

    public String getDomainClassAlias() {
        return domainClassAlias;
    }

    public String getDefinition() {
        /*if (definition == null)
            definition = toString(new StringBuilder(), false).toString();*/
        return definition;
    }

    public Expression getWhere() {
        return where;
    }

    public ExpressionArray getOrderBy() {
        return orderBy;
    }

    public Expression getLimit() {
        return limit;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public String getSqlDefinition() {
        return sqlDefinition;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    @Override
    public String toString() {
        return getDefinition();
    }

}
