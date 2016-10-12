package mongoose.services;

import mongoose.entities.Person;
import naga.framework.orm.domainmodel.DataSourceModel;

/**
 * @author Bruno Salmon
 */
public interface PersonService {

    static PersonService get(DataSourceModel dataSourceModel) {
        return PersonServiceImpl.get(dataSourceModel);
    }

    static PersonService getOrCreate(DataSourceModel dataSourceModel) {
        return PersonServiceImpl.getOrCreate(dataSourceModel);
    }

    Person getPreselectionProfilePerson();

}
