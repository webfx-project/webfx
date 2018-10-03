package webfx.framework.client.activity.impl.combinations.viewdomainapplication;

import webfx.framework.client.activity.impl.combinations.viewapplication.ViewApplicationContext;
import webfx.framework.client.activity.impl.combinations.viewdomain.ViewDomainActivityContext;
import webfx.framework.client.activity.impl.combinations.viewdomainapplication.impl.ViewDomainApplicationContextFinal;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;

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
