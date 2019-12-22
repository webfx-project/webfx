package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.Ordered;

/**
 * @author Bruno Salmon
 */
public final class OrderedBuilder extends UnaryExpressionBuilder {
    private boolean ascending;
    private boolean descending;
    private boolean nullsFirst;
    private boolean nullsLast;

    public OrderedBuilder(ExpressionBuilder operand) {
        super(operand);
    }

    public OrderedBuilder setAscending(boolean ascending) {
        this.ascending = ascending;
        return this;
    }

    public OrderedBuilder setDescending(boolean descending) {
        this.descending = descending;
        return this;
    }

    public OrderedBuilder setNullsFirst(boolean nullsFirst) {
        this.nullsFirst = nullsFirst;
        return this;
    }

    public OrderedBuilder setNullsLast(boolean nullsLast) {
        this.nullsLast = nullsLast;
        return this;
    }

    @Override
    protected Ordered newUnaryOperation(Expression operand) {
        return new Ordered(operand, ascending, descending, nullsFirst, nullsLast);
    }

    public static OrderedBuilder embed(ExpressionBuilder eb) {
        return eb instanceof OrderedBuilder ? (OrderedBuilder) eb : new OrderedBuilder(eb);
    }
}
