package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.extras.type.PrimType;
import webfx.extras.type.Type;

/**
 * @author Bruno Salmon
 */
public final class Exists extends SelectExpression {

    public Exists(Select select) {
        super(select);
    }

    @Override
    public Type getType() {
        return PrimType.BOOLEAN;
    }

    @Override
    public Object evaluate(Object domainObject, DomainReader domainReader) {
        return null;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return super.toString(sb.append("exists"));
    }
}
