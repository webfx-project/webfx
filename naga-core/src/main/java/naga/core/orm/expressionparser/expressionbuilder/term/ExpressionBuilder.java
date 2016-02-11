package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expressionparser.expressionbuilder.BuilderThreadContext;
import naga.core.orm.expressionparser.lci.ParserModelReader;

/**
 * @author Bruno Salmon
 */
public abstract class ExpressionBuilder {

    public String buildingClassName;
    public Object buildingClass;

    public abstract Expression build();

    protected void propagateDomainClasses() {
        if (buildingClass == null && buildingClassName != null)
            buildingClass = getModelReader().getDomainClass(buildingClassName);
    }

    protected static ParserModelReader getModelReader() {
        return BuilderThreadContext.getInstance().getModelReader();
    }
}
