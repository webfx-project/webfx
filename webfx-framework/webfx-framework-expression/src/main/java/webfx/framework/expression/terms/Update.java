package webfx.framework.expression.terms;

import webfx.framework.expression.Expression;

/**
 * @author Bruno Salmon
 */
public class Update<T> extends SqlOrder<T> {

    private final ExpressionArray<T> setClause; // field1=value1, field2=value2, ...

    public Update(Object domainClass, String domainClassAlias, String definition) {
        this(null, domainClass, domainClassAlias, definition, null, null, null, null);
    }

    public Update(Object domainClass, String domainClassAlias, String sqlDefinition, Object[] parameterValues) {
        this(null, domainClass, domainClassAlias, null, sqlDefinition, parameterValues, null, null);
    }

    public Update(Object domainClass, ExpressionArray<T> setClause, Expression<T> where) {
        this(null, domainClass, null, null, null, null, setClause, where);
    }

    public Update(Object id, Object domainClass, String domainClassAlias, String definition, String sqlDefinition, Object[] sqlParameters, ExpressionArray<T> setClause, Expression<T> where) {
        super(id, domainClass, domainClassAlias, definition, sqlDefinition, sqlParameters, where, null, null);
        this.setClause = setClause;
    }

    public ExpressionArray<T> getSetClause() {
        return setClause;
    }
}
