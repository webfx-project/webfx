package webfx.framework.shared.expression.terms.function;

import webfx.framework.shared.expression.lci.DataReader;
import webfx.framework.shared.expression.terms.Alias;
import webfx.fxkits.extra.type.Type;

/**
 * @author Bruno Salmon
 */
public final class ArgumentAlias extends Alias {

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
