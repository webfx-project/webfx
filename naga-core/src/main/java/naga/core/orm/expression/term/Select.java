package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public class Select<T> extends SqlOrder<T> {
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
    public String toString() {
        return toString( new StringBuilder()).toString();
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

    // Some Strings static methods helpers

    public static String _if(boolean condition, String s) {
        return condition && s!= null ? s : "";
    }

    private static String _ifNotEmpty(Object s, StringBuilder sb) {
        return _if(null, s, null, sb);
    }

    private static String _if(String before, Object s, StringBuilder sb) {
        return _if(before, s, null, sb);
    }

    private static String _if(Object s, String after, StringBuilder sb) {
        return _if(null, s, after, sb);
    }

    private static String _if(String before, Object s, String after, StringBuilder sb) {
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
