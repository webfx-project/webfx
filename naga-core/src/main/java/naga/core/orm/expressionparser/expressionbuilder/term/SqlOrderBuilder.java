package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Alias;
import naga.core.orm.expression.term.SqlOrder;
import naga.core.orm.expressionparser.expressionbuilder.BuilderThreadContext;
import naga.core.orm.expressionparser.expressionbuilder.ReferenceResolver;
import naga.core.orm.expressionparser.expressionbuilder.ThreadLocalReferenceResolver;
import naga.core.orm.expressionparser.lci.ParserDomainModelReader;

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
