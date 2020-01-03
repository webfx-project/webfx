package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.CollectOptions;
import webfx.framework.shared.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public abstract class DqlStatement<T> {

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

    protected DqlStatement(Object id, Object domainClass, String domainClassAlias, String definition, String sqlDefinition, Object[] parameterValues, Expression<T> where, ExpressionArray<T> orderBy, Expression<T> limit) {
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

    public void collect(CollectOptions options) {
        if (where != null)
            where.collect(options);
        if (orderBy != null)
            orderBy.collect(options);
        if (limit != null)
            limit.collect(options);
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    public abstract StringBuilder toString(StringBuilder sb);

    // Some Strings static methods helpers

    protected static String _if(boolean condition, String s) {
        return condition && s!= null ? s : "";
    }

    protected static String _ifNotEmpty(Object s, StringBuilder sb) {
        return _if(null, s, null, sb);
    }

    protected static String _if(String before, Object s, StringBuilder sb) {
        return _if(before, s, null, sb);
    }

    protected static String _if(Object s, String after, StringBuilder sb) {
        return _if(null, s, after, sb);
    }

    protected static String _if(String before, Object s, String after, StringBuilder sb) {
        if (s != null) {
            if (before != null)
                sb.append(before);
            if (s instanceof Expression)
                ((Expression) s).toString(sb);
            else
                sb.append(s);
            if (after != null)
                sb.append(after);
        }
        return "";
    }

}
