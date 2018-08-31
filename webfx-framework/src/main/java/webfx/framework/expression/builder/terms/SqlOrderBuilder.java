package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.builder.BuilderThreadContext;
import webfx.framework.expression.builder.ReferenceResolver;
import webfx.framework.expression.builder.ThreadLocalReferenceResolver;
import webfx.framework.expression.lci.ParserDomainModelReader;
import webfx.framework.expression.terms.Alias;
import webfx.framework.expression.terms.SqlOrder;

/**
 * @author Bruno Salmon
 */
public abstract class SqlOrderBuilder<S extends SqlOrder> implements ReferenceResolver {
    public String definition;
    public String buildingClassName;
    public Object buildingClass;
    public String buildingClassAlias;
    public ExpressionBuilder where;
    public ExpressionBuilder limit;
    public String sqlDefinition;
    public Object[] sqlParameters;

    public S build() {
        propagateDomainClasses();
        ThreadLocalReferenceResolver.pushReferenceResolver(this);
        S sqlOrder = buildSqlOrder();
        ThreadLocalReferenceResolver.popReferenceResolver();
        return sqlOrder;
    }

    protected void propagateDomainClasses() {
        if (buildingClass == null && buildingClassName != null)
            buildingClass = getModelReader().getDomainClassByName(buildingClassName);
        if (where != null)
            where.buildingClass = buildingClass;
        if (limit != null)
            limit.buildingClass = buildingClass;

    }

    protected static ParserDomainModelReader getModelReader() {
        return BuilderThreadContext.getInstance().getModelReader();
    }


    protected abstract S buildSqlOrder();

    @Override
    public Expression resolveReference(String name) {
        Expression reference = getModelReader().getDomainFieldSymbol(buildingClass, name);
        if (reference == null && name.equals(buildingClassAlias))
            return new Alias(name, null, buildingClass);
        return reference;
    }
}
