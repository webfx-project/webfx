package webfx.framework.client.activity.impl.combinations.viewdomain;

import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.activity.impl.elementals.domain.DomainActivityContext;
import webfx.framework.client.activity.impl.elementals.view.ViewActivityContext;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;

/**
 * @author Bruno Salmon
 */
public interface ViewDomainActivityContext
        <THIS extends ViewDomainActivityContext<THIS>>

        extends ViewActivityContext<THIS>,
        DomainActivityContext<THIS> {

    static ViewDomainActivityContextFinal create(ActivityContext parentContext) {
        return new ViewDomainActivityContextFinal(parentContext);
    }

    static ViewDomainActivityContextFinal createViewDomainActivityContext(DataSourceModel dataSourceModel) {
        return create(null).setDataSourceModel(dataSourceModel);
    }

}
