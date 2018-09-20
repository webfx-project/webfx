package webfx.framework.orm.entity;

import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.orm.domainmodel.HasDataSourceModel;

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
