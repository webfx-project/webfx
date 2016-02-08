package naga.core.orm.expression.term;

/**
 * @author Bruno Salmon
 */
public class Select extends SqlOrder {
    private final boolean distinct;
    private final boolean includeIdColumn;
    private final ExpressionArray fields;
    private final ExpressionArray groupBy;
    private final Expression having;

    public Select(Object id, Object domainClass, String domainClassAlias, String definition, String sqlDefinition, Object[] sqlParameters, boolean distinct, ExpressionArray fields, Expression where, ExpressionArray groupBy, Expression having, ExpressionArray orderBy, Expression limit, boolean includeIdColumn) {
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

    public ExpressionArray getFields() {
        return fields;
    }

    public ExpressionArray getGroupBy() {
        return groupBy;
    }

    public Expression getHaving() {
        return having;
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

    public static CharSequence _ifNotEmpty(CharSequence s) {
        return s != null ? s : "";
    }

    public static CharSequence _if(boolean condition, CharSequence s) {
        return condition && s!= null ? s : "";
    }

    public static CharSequence _if(String before, CharSequence s, String after, StringBuilder sb) {
        if (s != null && s.length() > 0) {
            if (before != null)
                sb.append(before);
            sb.append(s);
            if (after != null)
                sb.append(after);
        }
        return "";
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
