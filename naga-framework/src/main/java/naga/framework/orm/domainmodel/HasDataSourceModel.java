package naga.framework.orm.domainmodel;

/**
 * @author Bruno Salmon
 */
public interface HasDataSourceModel extends HasDomainModel {

    DataSourceModel getDataSourceModel();

    @Override
    default DomainModel getDomainModel() {
        return getDataSourceModel().getDomainModel();
    }
}
