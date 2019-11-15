package webfx.framework.shared.orm.expression.terms.function;

import webfx.extras.type.Type;

/**
 * @author Bruno Salmon
 */
public abstract class SqlAggregateFunction<T> extends AggregateFunction<T> {

    public SqlAggregateFunction(String name) {
        super(name);
    }

    public SqlAggregateFunction(String name, Type returnType) {
        super(name, returnType);
    }

    public SqlAggregateFunction(String name, String[] argNames, Type[] argTypes, Type returnType) {
        super(name, argNames, argTypes, returnType);
    }

    public SqlAggregateFunction(String name, String[] argNames, Type[] argTypes, Type returnType, boolean evaluable) {
        super(name, argNames, argTypes, returnType, evaluable);
    }

    @Override
    public boolean isSqlExpressible() {
        return true;
    }
}
