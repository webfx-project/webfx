package webfx.framework.expression.sqlcompiler.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.*;

/**
 * @author Bruno Salmon
 */
public class BooleanExpressionSqlCompiler extends BinaryExpressionSqlCompiler<BinaryBooleanExpression> {

    public BooleanExpressionSqlCompiler() {
        super(And.class, Equals.class, GreaterThan.class, GreaterThanOrEquals.class,
                In.class, LessThan.class, LessThanOrEquals.class, Like.class, NotEquals.class, NotLike.class, Or.class,
                All.class, Any.class);
    }

    @Override
    public void compileExpressionToSql(BinaryBooleanExpression e, Options o) {
        if (e.getRight() == Constant.NULL) {
            String right = e instanceof Equals ? " is null" : e instanceof NotEquals ? " is not null" : null;
            if (right != null) { // check left Parameter case in kbs 2.0
                compileChildExpressionToSql(e.getLeft(), o);
                o.build.prepareAppend(o.clause, right);
                return;
            }
        }
        boolean lowerLeftPrecedence = getSqlPrecedenceLevel(e.getLeft()) < e.getPrecedenceLevel();
        StringBuilder sb = o.build.prepareAppend(o);
        if (lowerLeftPrecedence)
            sb.append('(');
        o = o.changeSeparator(null);
        compileChildExpressionToSql(e.getLeft(), o);
        if (lowerLeftPrecedence)
            sb.append(')');
        o.build.prepareAppend(o.clause, getSqlSeparator(e, o));
        boolean lowerRightPrecedence = getSqlPrecedenceLevel(e.getRight()) < e.getPrecedenceLevel();
        if (lowerRightPrecedence)
            sb.append('(');
        if (e instanceof In) {
            o = o.changeReadForeignFields(false); /* stop reading foreign fields with in (select ...) since only 1 field must be present */
            if (e.getRight() instanceof ExpressionArray)
                o = o.changeSeparator(", ");

        }
        compileChildExpressionToSql(e.getRight(), o);
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
