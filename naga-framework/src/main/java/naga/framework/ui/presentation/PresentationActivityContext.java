package naga.framework.ui.presentation;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.ui.activity.UiActivityContext;

/**
 * @author Bruno Salmon
 */
public interface PresentationActivityContext extends UiActivityContext<PresentationActivityContext> {

    void setDataSourceModel(DataSourceModel dataSourceModel);

    DataSourceModel getDataSourceModel();

}
