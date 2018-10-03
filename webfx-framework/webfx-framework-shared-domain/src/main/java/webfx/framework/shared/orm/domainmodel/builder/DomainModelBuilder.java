package webfx.framework.shared.orm.domainmodel.builder;

import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class DomainModelBuilder {
    final Map<String, DomainClassBuilder> classBuilderMap = new HashMap<>();

    public final Object dataModelId;
    final Map<Object, DomainClass> classMap = new HashMap<>();

    public final DomainModel dataModel;

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
