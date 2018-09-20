package webfx.framework.orm.domainmodel;

import webfx.framework.expression.Expression;
import webfx.framework.expression.sqlcompiler.ExpressionSqlCompiler;
import webfx.framework.expression.terms.Symbol;
import webfx.fxkits.extra.type.Type;

/**
 * @author Bruno Salmon
 */
public class FieldsGroup extends Symbol {
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
    public Type getType() {
        return getExpression().getType();
    }

    @Override
    public Expression getExpression() {
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
