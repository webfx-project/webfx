package mongoose.services;

import mongoose.entities.Person;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.UpdateStore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
class PersonServiceImpl implements PersonService {

    private static Map<Object, PersonService> services = new HashMap<>();

    static PersonService get(DataSourceModel dataSourceModel) {
        return services.get(dataSourceModel.getId());
    }

    static PersonService getOrCreate(DataSourceModel dataSourceModel) {
        PersonService service = get(dataSourceModel);
        if (service == null)
            services.put(dataSourceModel.getId(), service = new PersonServiceImpl(dataSourceModel));
        return service;
    }

    private final DataSourceModel dataSourceModel;
    private Person preselectionProfilePerson;

    public PersonServiceImpl(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        UpdateStore store = UpdateStore.create(dataSourceModel);
        preselectionProfilePerson = store.insertEntity(Person.class);
    }

    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }

    @Override
    public Person getPreselectionProfilePerson() {
        return preselectionProfilePerson;
    }
}
