package naga.core.orm.expression.term;

/**
 * @author Bruno Salmon
 */
public interface ParentExpression extends Expression {

    Expression[] getChildren();

}
