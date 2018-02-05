package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.builder.ThreadLocalReferenceResolver;
import naga.framework.expression.terms.Alias;
import naga.framework.expression.terms.function.Call;
import naga.framework.expression.terms.function.Function;

/**
 * @author Bruno Salmon
 */
public class FieldBuilder extends ExpressionBuilder {
    public String name;

    private Expression field;  // Field or Alias or Argument

    public FieldBuilder(String name) {
        this.name = name;
    }

    @Override
    public Expression build() {
        if (field == null) {
            propagateDomainClasses();
            if (field == null)
                field = ThreadLocalReferenceResolver.resolveReference(name);
            if (field == null) {
                field = getModelReader().getDomainFieldSymbol(buildingClass, name);
                if (field == null) {
                    if ("a".equals(name)) { // temporary hack to make ceremony work on statistics screen
                        Object attendance = getModelReader().getDomainClassByName("Attendance");
                        field = new Alias("a", attendance); //, attendance.getField("documentLine"), false) ;
                    } else {
                        Function function = Function.getFunction(name);
                        if (function != null && function.isKeyword())
                            return new Call(name, null);
                        throw new IllegalArgumentException("Unable to resolve reference '" + name + "' on class " + buildingClass);
                    }
                }
            }
        }
        return field;
    }
}
