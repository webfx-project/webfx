package webfx.framework.shared.orm.dql.sqlcompiler.terms;

import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlClause;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.*;

/**
 * @author Bruno Salmon
 */
public final class BooleanExpressionSqlCompiler extends BinaryExpressionSqlCompiler<BinaryBooleanExpression> {

    public BooleanExpressionSqlCompiler() {
        super(And.class, Equals.class, GreaterThan.class, GreaterThanOrEquals.class,
                In.class, LessThan.class, LessThanOrEquals.class, Like.class, NotEquals.class, NotLike.class, Or.class,
                All.class, Any.class);
    }

    @Override
    public void compileExpressionToSql(BinaryBooleanExpression e, Options o) {
        Expression left = e.getLeft();
        Expression right = e.getRight();
        if (o.clause == SqlClause.WHERE) {
            if (right == Constant.NULL) {
                String specialOperator = e instanceof Equals ? " is null" : e instanceof NotEquals ? " is not null" : null;
                if (specialOperator != null) {
                    compileChildExpressionToSql(left, o);
                    o.build.prepareAppend(o.clause, specialOperator);
                    return;
                }
            }
/*
            if (right instanceof Parameter) {
                String specialOperator = e instanceof Equals ? " is not distinct from " : e instanceof NotEquals ? " is distinct from " : null;
                if (specialOperator != null) {
                    compileChildExpressionToSql(left, o);
                    o.build.prepareAppend(o.clause, specialOperator);
                    compileChildExpressionToSql(right, o);
                    return;
                }
            }
*/
        }
        boolean lowerLeftPrecedence = getSqlPrecedenceLevel(left) < e.getPrecedenceLevel();
        StringBuilder sb = o.build.prepareAppend(o);
        if (lowerLeftPrecedence)
            sb.append('(');
        o = o.changeSeparator(null);
        compileChildExpressionToSql(left, o);
        if (lowerLeftPrecedence)
            sb.append(')');
        o.build.prepareAppend(o.clause, getSqlSeparator(e, o));
        boolean lowerRightPrecedence = getSqlPrecedenceLevel(right) < e.getPrecedenceLevel();
        if (lowerRightPrecedence)
            sb.append('(');
        if (e instanceof In) {
            o = o.changeReadForeignFields(false); /* stop reading foreign fields with in (select ...) since only 1 field must be present */
            if (right instanceof ExpressionArray)
                o = o.changeSeparator(", ");
        }
        compileChildExpressionToSql(right, o);
        if (lowerRightPrecedence)
            sb.append(')');
    }

    private int getSqlPrecedenceLevel(Expression e) {
        int precedenceLevel = e.getPrecedenceLevel();
        if (e instanceof Dot) // may be decomposed (ex: document.(cancelled or !arrived) -> document.cancelled or document.!arrived)
            precedenceLevel = Math.min(precedenceLevel, getSqlPrecedenceLevel(((Dot) e).getRight()));
        return precedenceLevel;
    }
}
