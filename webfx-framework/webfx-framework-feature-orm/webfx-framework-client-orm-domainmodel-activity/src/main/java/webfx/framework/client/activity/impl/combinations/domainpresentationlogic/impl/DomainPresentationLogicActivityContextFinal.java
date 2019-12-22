package webfx.framework.client.activity.impl.combinations.domainpresentationlogic.impl;

import webfx.framework.client.activity.impl.elementals.domain.DomainActivityContext;
import webfx.framework.client.activity.impl.elementals.presentation.logic.impl.PresentationLogicActivityContextBase;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.client.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class DomainPresentationLogicActivityContextFinal<PM>
        extends PresentationLogicActivityContextBase<DomainPresentationLogicActivityContextFinal<PM>, PM>
        implements DomainActivityContext<DomainPresentationLogicActivityContextFinal<PM>> {

    public DomainPresentationLogicActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, DomainPresentationLogicActivityContextFinal::new);
    }

    private DataSourceModel dataSourceModel;

    @Override
    public DomainPresentationLogicActivityContextFinal<PM> setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        return this;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        if (dataSourceModel != null)
            return dataSourceModel;
        ActivityContext parentContext = getParentContext();
        if (parentContext instanceof DomainActivityContext)
            return ((DomainActivityContext) parentContext).getDataSourceModel();
        return null;
    }
}
