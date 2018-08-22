package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
import naga.type.PrimType;
import naga.type.Type;
import naga.util.Booleans;

/**
 * @author Bruno Salmon
 */
public class Not<T> extends UnaryExpression<T> {

    public Not(Expression<T> operand) {
        super(operand);
    }

    @Override
    public Type getType() {
        return PrimType.BOOLEAN;
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        return Booleans.isFalse(super.evaluate(domainObject, dataReader));
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append("not(");
        operand.toString(sb);
        return sb.append(')');
    }
}
