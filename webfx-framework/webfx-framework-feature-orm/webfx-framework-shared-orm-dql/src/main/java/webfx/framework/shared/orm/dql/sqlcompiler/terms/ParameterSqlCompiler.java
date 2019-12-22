package webfx.framework.shared.orm.dql.sqlcompiler.terms;

import webfx.framework.shared.orm.expression.terms.Parameter;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlClause;

/**
 * @author Bruno Salmon
 */
public final class ParameterSqlCompiler extends AbstractTermSqlCompiler<Parameter> {

    public ParameterSqlCompiler() {
        super(Parameter.class);
    }

    @Override
    public void compileExpressionToSql(Parameter e, Options o) {
        compileParameter(e, o, true);
    }

    public void compileParameter(Parameter e, Options o, boolean isRightOperand) {
        String name = e.getName();
        if (name != null) {
            if (isClientOnly(e, o.clause == SqlClause.SELECT)) // TODO: distinguer les parametres sql des parametres locaux
                return;
            o.build.getParameterNames().add(name);
            if (e.getRightDot() != null)
                o.build.setCacheable(false);
            /*
            int parameterIndex = e.getIndex() != -1 ? e.getIndex() : o.build.getParameterCount();
            Object parameterValue = o.parameterValues[parameterIndex];
            if (parameterValue instanceof ParameterJoinValue) {
                ParameterJoinValue value = (ParameterJoinValue) parameterValue;
                Object previousCompilingClass = o.build.getCompilingClass();
                String previousTableAlias = o.build.getCompilingTableAlias();
                Object parameterClass = previousCompilingClass.getDomainModel().getClass(value.getId().getClassId());
                String tableAlias = o.build.getNewTableAlias(o.modelReader.getDomainClassSqlTableName(parameterClass), null, false);
                Expression right = Parser.parseExpression(value.getRightDot(), parameterClass);
                o.build.setCompilingClass(parameterClass);
                o.build.setCompilingTableAlias(tableAlias);
                boolean lowerRightPrecedence = right.getPrecedenceLevel() < 8; // DOT
                // assuming it's a right value ex: x=?p.y => compiled to x=p.y and p.id=? (left value (ex: p.y=null) is not well compiled!...
                if (isRightOperand) {
                    StringBuilder sb =  o.build.prepareAppend(o);
                    if (lowerRightPrecedence)
                        sb.append('(');
                    compileChildExpressionToSql(right, o.changeSeparator(null));
                    if (lowerRightPrecedence)
                        sb.append(')');
                    sb = o.build.prepareAppend(SqlClause.WHERE, " and ");
                    sb.append(tableAlias).append('.').append(o.modelReader.getDomainClassPrimaryKeySqlColumnName(parameterClass)).append("=?");
                } else {
                    StringBuilder sb =  o.build.prepareAppend(o);
                    sb.append(tableAlias).append('.').append(o.modelReader.getDomainClassPrimaryKeySqlColumnName(parameterClass)).append("=?");
                    sb = o.build.prepareAppend(SqlClause.WHERE, " and ");
                    if (lowerRightPrecedence)
                        sb.append('(');
                    compileChildExpressionToSql(right, o.changeSeparator(null));
                    if (lowerRightPrecedence)
                        sb.append(')');
                }
                o.build.setCompilingClass(parameterClass);
                o.build.setCompilingTableAlias(previousTableAlias);
                return;
            }
            */
        }
        o.build.addColumnInClause(null, "?", null, null, o.clause, o.separator, false, false, o.generateQueryMapping);
    }

    private boolean isClientOnly(Parameter e, boolean forSelectClause) {
        String name = e.getName();
        return name != null && (name.equals("lang") || forSelectClause && name.startsWith("selected") && e.getRightDot() == null);
    }

}
