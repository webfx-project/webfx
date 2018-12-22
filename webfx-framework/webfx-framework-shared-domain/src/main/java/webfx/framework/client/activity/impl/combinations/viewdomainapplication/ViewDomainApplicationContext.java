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

    static ViewDomainApplicationContextFinal createViewDomainApplicationContext() {
        return new ViewDomainApplicationContextFinal(ViewDomainActivityContext::create);
    }

    static ViewDomainApplicationContextFinal createViewDomainApplicationContext(DataSourceModel dataSourceModel) {
        return createViewDomainApplicationContext().setDataSourceModel(dataSourceModel);
    }

}
