package naga.framework.activity.combinations.viewdomainapplication;

import naga.framework.activity.combinations.viewapplication.ViewApplicationContext;
import naga.framework.activity.combinations.viewdomain.ViewDomainActivityContext;
import naga.framework.activity.combinations.viewdomainapplication.impl.ViewDomainApplicationContextFinal;
import naga.framework.orm.domainmodel.DataSourceModel;

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
