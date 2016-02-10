package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.lci.DataReader;
import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.util.Booleans;

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
