package naga.core.orm.expression.term;

import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.util.Booleans;

/**
 * @author Bruno Salmon
 */
public class Not extends UnaryExpression {

    public Not(Expression operand) {
        super(operand);
    }

    @Override
    public Type getType() {
        return PrimType.BOOLEAN;
    }

    @Override
    public Object evaluate(Object data) {
        return Booleans.isFalse(super.evaluate(data));
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append("not(");
        operand.toString(sb);
        return sb.append(')');
    }
}
