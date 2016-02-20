package naga.core.orm.domainmodelloader.domainmodelbuilder;

import naga.core.orm.domainmodel.DomainClass;
import naga.core.orm.domainmodel.FieldsGroup;

/**
 * @author Bruno Salmon
 */
public class DomainFieldsGroupBuilder {

    public DomainClassBuilder classBuilder;
    public DomainClass domainClass;
    public String name;
    public String fieldsDefinition;

    public DomainFieldsGroupBuilder(String name) {
        this.name = name;
    }

    private FieldsGroup fieldsGroup;  // Field or Alias

    public FieldsGroup build() {
        if (fieldsGroup == null) {
            if (domainClass == null)
                domainClass = classBuilder.domainClass;
            fieldsGroup = domainClass.getFieldsGroup(name);
            if (fieldsGroup == null)
                fieldsGroup = new FieldsGroup(domainClass, name, fieldsDefinition);
        }
        return fieldsGroup;
    }

}
