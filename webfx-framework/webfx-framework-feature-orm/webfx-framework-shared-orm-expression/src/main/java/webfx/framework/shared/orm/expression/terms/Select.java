package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class Select<T> extends DqlStatement<T> {

    private final boolean distinct;
    private final boolean includeIdColumn;
    private final ExpressionArray<T> fields;
    private final ExpressionArray<T> groupBy;
    private final Expression<T> having;

    public Select(Object id, Object domainClass, String domainClassAlias, String definition, String sqlDefinition, Object[] sqlParameters, boolean distinct, ExpressionArray<T> fields, Expression<T> where, ExpressionArray<T> groupBy, Expression<T> having, ExpressionArray<T> orderBy, Expression<T> limit, boolean includeIdColumn) {
        super(id, domainClass, domainClassAlias, definition, sqlDefinition, sqlParameters, where, orderBy, limit);
        this.distinct = distinct;
        this.includeIdColumn = includeIdColumn;
        this.fields = fields;
        this.groupBy = groupBy;
        this.having = having;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public boolean isIncludeIdColumn() {
        return includeIdColumn;
    }

    public ExpressionArray<T> getFields() {
        return fields;
    }

    public ExpressionArray<T> getGroupBy() {
        return groupBy;
    }

    public Expression<T> getHaving() {
        return having;
    }

    @Override
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        if (fields != null)
            fields.collectPersistentTerms(persistentTerms);
        super.collectPersistentTerms(persistentTerms);
        if (groupBy != null)
            groupBy.collectPersistentTerms(persistentTerms);
        if (having != null)
            having.collectPersistentTerms(persistentTerms);
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    public StringBuilder toString(StringBuilder sb) {
        return sb.append("select ")
                .append(_if(distinct, "distinct "))
                .append(_if(fields, " from ", sb))
                .append(_ifNotEmpty(getDomainClass(), sb)).append(_if(" ", domainClassAlias, "", sb))
                .append(_if(" where ", where, sb))
                .append(_if(" group by ", groupBy, sb))
                .append(_if(" having ", having, sb))
                .append(_if(" order by ", orderBy, sb))
                .append(_if(" limit ", limit, sb));
    }
}
