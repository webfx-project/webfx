package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public interface ParentExpression extends Expression {

    Expression[] getChildren();

}
