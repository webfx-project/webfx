package mongoose.client.activities.shared;

import mongoose.shared.util.Labels;
import webfx.framework.shared.expression.lci.DataReader;
import webfx.framework.shared.expression.terms.function.Function;
import webfx.framework.client.services.i18n.I18n;
import webfx.fxkits.extra.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class TranslateFunction<T> extends Function<T> {

    public TranslateFunction() {
        this("translate");
    }

    public TranslateFunction(String name) {
        super(name, null, null, PrimType.STRING, true);
    }

    @Override
    public Object evaluate(T argument, DataReader<T> dataReader) {
        if (argument instanceof String)
            return I18n.instantTranslate(argument);
        return translate(dataReader.getDomainObjectFromId(argument, null));
    }

    protected String translate(T t) {
        return bestTranslationOrName(t);
    }

    protected String bestTranslationOrName(Object o) {
        return Labels.instantTranslateLabel(Labels.bestLabelOrName(o));
    }
}
