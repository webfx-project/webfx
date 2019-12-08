package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.Numbers;

/**
 * @author Bruno Salmon
 */
public final class Or<T> extends BinaryBooleanExpression<T> {

    public Or(Expression<T> left, Expression<T> right) {
        super(left, " or ", right, 2);
    }

    /**
     * A shortcut value for the Or operator is a value that will make the operator return true whatever the other operand is.
     * A strict Or operator would accept only true as shortcut value but since this operator is not strict, it accepts
     * any non null value except false and 0 as a shortcut value.
     */
    public boolean isShortcutValue(Object value) {
        return value != null && Booleans.isNotFalse(value) && Numbers.isNotZero(value);
    }

    @Override
    public Boolean evaluateCondition(Object a, Object b, DomainReader<T> domainReader) {
        if (a == null || b == null)
            return null;
        return isShortcutValue(a) || isShortcutValue(b);
    }
}
