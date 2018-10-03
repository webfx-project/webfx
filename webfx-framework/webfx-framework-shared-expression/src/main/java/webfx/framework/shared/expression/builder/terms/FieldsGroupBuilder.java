package webfx.framework.shared.expression.builder.terms;

import webfx.framework.shared.expression.terms.Symbol;

/**
 * @author Bruno Salmon
 */
public final class FieldsGroupBuilder extends ExpressionBuilder {

    public final String name;

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
