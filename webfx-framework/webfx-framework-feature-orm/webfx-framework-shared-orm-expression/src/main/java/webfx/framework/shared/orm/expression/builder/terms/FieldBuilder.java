package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.builder.ThreadLocalReferenceResolver;
import webfx.framework.shared.orm.expression.parser.lci.ParserDomainModelReader;
import webfx.framework.shared.orm.expression.terms.function.Call;
import webfx.framework.shared.orm.expression.terms.function.Function;

/**
 * @author Bruno Salmon
 */
public final class FieldBuilder extends ExpressionBuilder {

    public final String name;
    private Expression field;  // Field or Alias or Argument

    public FieldBuilder(String name) {
        this.name = name;
    }

    @Override
    public Expression build() {
        if (field == null) {
            propagateDomainClasses();
            ParserDomainModelReader modelReader = getModelReader();
            // Checking if it is a field of the building class
            if (modelReader != null)
                field = modelReader.getDomainFieldSymbol(buildingClass, name);
            // If not, checking if it is a reference (such as an alias)
            if (field == null)
                field = ThreadLocalReferenceResolver.resolveReference(name);
            // If not, checking if it is a keyword function (ex: current_date)
            if (field == null) {
                Function function = Function.getFunction(name);
                if (function != null && function.isKeyword())
                    field = new Call(name, null); // resolved as a call
            }
/* Commented as it is not necessary any more (?)
            if (field == null && modelReader != null && "a".equals(name)) { // temporary hack to make ceremony work on statistics screen
                Object attendance = modelReader.getDomainClassByName("Attendance");
                field = new Alias("a", attendance); //, attendance.getField("documentLine"), false) ;
            }
*/
            if (field == null)
                throw new IllegalArgumentException("Unable to resolve reference '" + name + "' on class " + buildingClass);
        }
        return field;
    }
}
