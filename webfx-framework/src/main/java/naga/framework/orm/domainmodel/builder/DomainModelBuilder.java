package naga.framework.orm.domainmodel.builder;

import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.domainmodel.DomainModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DomainModelBuilder {
    Map<String, DomainClassBuilder> classBuilderMap = new HashMap<>();

    public Object dataModelId;
    Map<Object, DomainClass> classMap = new HashMap<>();

    public DomainModel dataModel;

    public DomainModelBuilder(Object dataModelId) {
        this.dataModelId = dataModelId;
        dataModel = new DomainModel(dataModelId, classMap);
    }

    public void registerClass(DomainClassBuilder classBuilder) {
        classBuilderMap.put(classBuilder.name, classBuilder);
    }

    public DomainClassBuilder newClassBuilder(String name, boolean register) {
        DomainClassBuilder classBuilder = new DomainClassBuilder(name);
        classBuilder.domainModelBuilder = this;
        if (register)
            registerClass(classBuilder);
        return classBuilder;
    }

    public DomainModel build() {
        for (Map.Entry<String, DomainClassBuilder> entry : classBuilderMap.entrySet()) {
            DomainClass domainClass = entry.getValue().build();
            classMap.put(domainClass.getName(), domainClass);
            classMap.put(domainClass.getId(), domainClass);
            if (domainClass.getSqlTableName() != null)
                classMap.put(domainClass.getSqlTableName(), domainClass);
        }
        return dataModel;
    }


}
