package naga.framework.activity.combinations.viewdomainapplication;

import naga.framework.activity.combinations.viewapplication.ViewApplicationContext;
import naga.framework.activity.combinations.viewdomain.ViewDomainActivityContext;
import naga.framework.activity.combinations.viewdomainapplication.impl.ViewDomainApplicationContextFinal;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface ViewDomainApplicationContext
        <THIS extends ViewDomainApplicationContext<THIS>>

        extends ViewApplicationContext<THIS>,
        ViewDomainActivityContext<THIS> {

    static ViewDomainApplicationContextFinal createViewDomainApplicationContext(String[] mainArgs) {
        return new ViewDomainApplicationContextFinal(mainArgs, ViewDomainActivityContext::create);
    }

    static ViewDomainApplicationContextFinal createViewDomainApplicationContext(DataSourceModel dataSourceModel, I18n i18n, String[] mainArgs) {
        ViewDomainApplicationContextFinal context = createViewDomainApplicationContext(mainArgs).setDataSourceModel(dataSourceModel);
        context.setI18n(i18n);
        return context;
    }

}
