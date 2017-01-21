package naga.framework.activity.view;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface ViewDomainApplicationContext
        <THIS extends ViewDomainApplicationContext<THIS>>

        extends ViewApplicationContext<THIS>,
        ViewDomainActivityContext<THIS> {

    static ViewDomainApplicationContextFinal create(String[] mainArgs) {
        return new ViewDomainApplicationContextFinal(mainArgs, ViewDomainActivityContext::create);
    }

    static ViewDomainApplicationContextFinal create(DataSourceModel dataSourceModel, String[] mainArgs) {
        return create(mainArgs).setDataSourceModel(dataSourceModel).setI18n(I18n.create("mongoose/dictionaries/{lang}.json"));
    }

}
