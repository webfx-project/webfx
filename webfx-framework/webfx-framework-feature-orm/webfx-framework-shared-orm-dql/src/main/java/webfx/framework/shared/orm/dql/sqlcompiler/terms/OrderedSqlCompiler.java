package webfx.framework.shared.orm.dql.sqlcompiler.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.Ordered;

/**
 * @author Bruno Salmon
 */
public final class OrderedSqlCompiler extends AbstractTermSqlCompiler<Ordered> {

    public OrderedSqlCompiler() {
        super(Ordered.class);
    }

    @Override
    public void compileExpressionToSql(Ordered e, Options o) {
        StringBuilder clauseBuilder = o.build.prepareAppend(o);
        Expression operand = e.getOperand();
        try {
            compileChildExpressionToSql(operand, o.changeSeparator(null));
        } catch (Exception ex) { // quick dirty hack to manage ordering by as fields (we can just repeat the alias name in the order clause)
            boolean catched = false;
            /*
            if (operand instanceof Field) { // checking it's a field
                String definition = ((Field) operand).getExpressionDefinition(); // getting its string definition (parsing its definition by getExpression() may produce an error due to unresolved names)
                if (definition != null) {
                    String[] split = definition.split(" as "); // checking it's a as field
                    if (split.length == 2) {
                        clauseBuilder.append(split[1]); // just putting it's alias name in the order clause
                        catched = true; // don't rethrow the exception
                    }
                }
            }*/
            if (!catched)
                throw ex;
        }
        if (e.isAscending())
            clauseBuilder.append(" asc");
        else if (e.isDescending())
            clauseBuilder.append(" desc");
        if (e.isNullsFirst())
            clauseBuilder.append(" nulls first");
        else if (e.isNullsLast())
            clauseBuilder.append(" nulls last");
    }
}
