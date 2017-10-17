package mongoose.activities.shared.book.event.shared;

import mongoose.util.Labels;
import naga.type.PrimType;
import naga.framework.expression.lci.DataReader;
import naga.framework.expression.terms.function.Function;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public class TranslateFunction extends Function {

    private I18n i18n;

    public TranslateFunction(I18n i18n) {
        this("translate");
        this.i18n = i18n;
    }

    public TranslateFunction(String name) {
        super(name, null, null, PrimType.STRING, true);
    }

    @Override
    public Object evaluate(Object argument, DataReader dataReader) {
        if (argument instanceof String)
            return i18n.instantTranslate(argument);
        return Labels.instantTranslateLabel(Labels.bestLabelOrName(dataReader.getDomainObjectFromId(argument, null)), i18n);
    }

}
