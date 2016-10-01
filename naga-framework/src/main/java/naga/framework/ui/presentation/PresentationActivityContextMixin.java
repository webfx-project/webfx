package naga.framework.ui.presentation;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.ActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface PresentationActivityContextMixin extends ActivityContextMixin<PresentationActivityContext>, PresentationActivityContext {

    @Override
    default void setDataSourceModel(DataSourceModel dataSourceModel) {
        getActivityContext().setDataSourceModel(dataSourceModel);
    }

    @Override
    default DataSourceModel getDataSourceModel() {
        return getActivityContext().getDataSourceModel();
    }
}
