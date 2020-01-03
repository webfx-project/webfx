package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public final class Insert<T> extends DqlStatement<T> {

    private final ExpressionArray<T> setClause; // field1=value1, field2=value2, ...

    public Insert(Object domainClass, ExpressionArray<T> setClause) {
        this(null, domainClass, null, null, null, null, setClause, null);
    }

    public Insert(Object id, Object domainClass, String domainClassAlias, String definition, String sqlDefinition, Object[] sqlParameters, ExpressionArray<T> setClause, Expression<T> where) {
        super(id, domainClass, domainClassAlias, definition, sqlDefinition, sqlParameters, where, null, null);
        this.setClause = setClause;
    }

    public ExpressionArray<T> getSetClause() {
        return setClause;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append("insert ")
                .append(_ifNotEmpty(getDomainClass(), sb)).append(_if(" ", domainClassAlias, "", sb))
                .append(_if(" set ", setClause, sb));
    }
}
