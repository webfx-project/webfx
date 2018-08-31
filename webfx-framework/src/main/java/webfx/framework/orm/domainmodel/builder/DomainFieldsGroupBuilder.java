package webfx.framework.orm.domainmodel.builder;

import webfx.framework.orm.domainmodel.DomainClass;
import webfx.framework.orm.domainmodel.FieldsGroup;

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
