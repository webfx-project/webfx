package naga.core.orm.expression.term.function;

import naga.core.orm.expression.lci.DataReader;
import naga.core.orm.expression.term.Alias;
import naga.core.type.Type;

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
