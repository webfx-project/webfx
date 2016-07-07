package naga.framework.expression.terms.function;

import naga.framework.expression.lci.DataReader;
import naga.framework.expression.terms.Alias;
import naga.commons.type.Type;

/**
 * @author Bruno Salmon
 */
public class ArgumentAlias extends Alias {

    private final int index;

    public ArgumentAlias(String name, Type type, int index) {
        super(name, type);
        this.index = index;
    }

    @Override
    public Object evaluate(Object data, DataReader dataReader) {
        return getArgument();
    }

    public Object getArgument() {
        return ThreadLocalArgumentStack.getArgument(index);
    }
}
