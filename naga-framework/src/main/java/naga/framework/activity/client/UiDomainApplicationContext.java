package naga.framework.activity.client;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface UiDomainApplicationContext<THIS extends UiDomainApplicationContext<THIS>> extends UiApplicationContext<THIS>, UiDomainActivityContext<THIS> {

    static UiDomainApplicationContextFinal create(String[] mainArgs) {
        return new UiDomainApplicationContextFinal(mainArgs, UiDomainActivityContext::create);
    }

    static UiDomainApplicationContextFinal create(DataSourceModel dataSourceModel, String[] mainArgs) {
        return create(mainArgs).setDataSourceModel(dataSourceModel).setI18n(I18n.create("mongoose/dictionaries/{lang}.json"));
    }

}
