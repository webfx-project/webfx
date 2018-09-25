package webfx.framework.expression.terms;

import webfx.framework.expression.Expression;

/**
 * @author Bruno Salmon
 */
public abstract class SqlOrder<T> {
    protected final Object id;
    protected final Object domainClass;
    protected final String domainClassAlias;

    // object level definition
    protected final String definition;
    protected final Expression<T> where;
    protected final ExpressionArray<T> orderBy;
    protected final Expression<T> limit;
    protected boolean cacheable;

    // sql level definition
    protected final String sqlDefinition;

    // common
    protected final Object[] parameterValues;

    protected SqlOrder(Object id, Object domainClass, String domainClassAlias, String definition, String sqlDefinition, Object[] parameterValues, Expression<T> where, ExpressionArray<T> orderBy, Expression<T> limit) {
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

    public Expression<T> getWhere() {
        return where;
    }

    public ExpressionArray<T> getOrderBy() {
        return orderBy;
    }

    public Expression<T> getLimit() {
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
