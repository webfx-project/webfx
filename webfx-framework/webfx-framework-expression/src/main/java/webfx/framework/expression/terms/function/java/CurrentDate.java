package webfx.framework.expression.terms.function.java;

import webfx.framework.expression.lci.DataReader;
import webfx.framework.expression.terms.function.Function;
import webfx.fxkits.extra.type.PrimType;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public class CurrentDate extends Function {

    public CurrentDate() {
        super("current_date", PrimType.DATE, true, true);
    }

    @Override
    public Object evaluate(Object argument, DataReader dataReader) {
        return LocalDate.now();
    }
}
