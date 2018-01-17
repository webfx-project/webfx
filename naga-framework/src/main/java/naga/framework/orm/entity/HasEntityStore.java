package naga.framework.orm.entity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.HasDataSourceModel;

/**
 * @author Bruno Salmon
 */
public interface HasEntityStore extends HasDataSourceModel {

    EntityStore getStore();

    @Override
    default DataSourceModel getDataSourceModel() {
        return getStore().getDataSourceModel();
    }
}
