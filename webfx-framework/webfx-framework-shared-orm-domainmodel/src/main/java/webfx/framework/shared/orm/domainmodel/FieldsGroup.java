package webfx.framework.shared.orm.domainmodel;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.dql.sqlcompiler.ExpressionSqlCompiler;
import webfx.framework.shared.orm.expression.terms.Symbol;

/**
 * @author Bruno Salmon
 */
public final class FieldsGroup<T> extends Symbol<T> {

    private final DomainClass domainClass;
    private String fieldsDefinition;

    static {
        ExpressionSqlCompiler.declareSymbolSubclasses(FieldsGroup.class);
    }

    public FieldsGroup(DomainClass domainClass, String name, String fieldsDefinition) {
        super(name, null, null);
        this.domainClass = domainClass;
        this.fieldsDefinition = fieldsDefinition;
    }

    @Override
    public Expression<T> getForwardingTypeExpression() {
        return getExpression();
    }

    @Override
    public Expression<T> getExpression() {
        if (expression == null) {
            expression = domainClass.parseExpression(fieldsDefinition);
            fieldsDefinition = null;
        }
        return expression;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append('<').append(name).append('>');
    }
}
