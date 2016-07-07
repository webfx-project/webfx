package naga.framework.expression.builder.terms;

import naga.framework.expression.terms.Symbol;

/**
 * @author Bruno Salmon
 */
public class FieldsGroupBuilder extends ExpressionBuilder {

    public String name;

    public FieldsGroupBuilder(String name) {
        this.name = name;
    }

    private Symbol fieldsGroup;  // Field or Alias

    @Override
    public Symbol build() {
        if (fieldsGroup == null) {
            propagateDomainClasses();
            fieldsGroup = getModelReader().getDomainFieldGroupSymbol(buildingClass, name);
        }
        return fieldsGroup;
    }

}
