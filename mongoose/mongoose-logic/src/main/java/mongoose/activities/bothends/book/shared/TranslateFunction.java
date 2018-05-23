package mongoose.activities.bothends.book.shared;

import mongoose.util.Labels;
import naga.type.PrimType;
import naga.framework.expression.lci.DataReader;
import naga.framework.expression.terms.function.Function;
import naga.framework.services.i18n.spi.I18nProvider;

/**
 * @author Bruno Salmon
 */
public class TranslateFunction<T> extends Function<T> {

    private final I18nProvider i18n;

    public TranslateFunction(I18nProvider i18n) {
        this("translate", i18n);
    }

    public TranslateFunction(String name, I18nProvider i18n) {
        super(name, null, null, PrimType.STRING, true);
        this.i18n = i18n;
    }

    @Override
    public Object evaluate(T argument, DataReader<T> dataReader) {
        if (argument instanceof String)
            return i18n.instantTranslate(argument);
        return translate(dataReader.getDomainObjectFromId(argument, null));
    }

    protected String translate(T t) {
        return bestTranslationOrName(t);
    }

    protected String bestTranslationOrName(Object o) {
        return Labels.instantTranslateLabel(Labels.bestLabelOrName(o), i18n);
    }
}
