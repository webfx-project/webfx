package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.builder.BuilderThreadContext;
import naga.framework.expression.lci.ParserDomainModelReader;

/**
 * @author Bruno Salmon
 */
public abstract class ExpressionBuilder {

    public String buildingClassName;
    public Object buildingClass;

    public abstract Expression build();

    protected void propagateDomainClasses() {
        if (buildingClass == null && buildingClassName != null)
            buildingClass = getModelReader().getDomainClassByName(buildingClassName);
    }

    protected static ParserDomainModelReader getModelReader() {
        BuilderThreadContext context = BuilderThreadContext.getInstance();
        return context == null ? null : context.getModelReader();
    }
}
