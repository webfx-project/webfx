package webfx.framework.activity.impl.combinations.viewdomainapplication;

import webfx.framework.activity.impl.combinations.viewapplication.ViewApplicationContext;
import webfx.framework.activity.impl.combinations.viewdomain.ViewDomainActivityContext;
import webfx.framework.activity.impl.combinations.viewdomainapplication.impl.ViewDomainApplicationContextFinal;
import webfx.framework.orm.domainmodel.DataSourceModel;

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

    static ViewDomainApplicationContextFinal createViewDomainApplicationContext(DataSourceModel dataSourceModel, String[] mainArgs) {
        return createViewDomainApplicationContext(mainArgs).setDataSourceModel(dataSourceModel);
    }

}
