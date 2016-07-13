package naga.framework.ui.presentation;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.activity.client.UiActivityContext;

/**
 * @author Bruno Salmon
 */
public interface PresentationActivityContext extends UiActivityContext<PresentationActivityContext> {

    void setDataSourceModel(DataSourceModel dataSourceModel);

    DataSourceModel getDataSourceModel();

}
