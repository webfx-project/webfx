package naga.core.orm.expression.term;

import naga.core.orm.expression.Expressions;
import naga.core.orm.expression.datalci.DataReader;
import naga.core.orm.expression.datalci.DataWriter;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public abstract class AbstractExpression implements Expression {

    private final int precedenceLevel;

    public AbstractExpression(int precedenceLevel) {
        this.precedenceLevel = precedenceLevel;
    }


    @Override
    public int getPrecedenceLevel() {
        return precedenceLevel;
    }

    @Override
    public String toString() {
        return Expressions.toString(this);
    }

    @Override
    public void collectPersistentTerms(Collection<Expression> persistentTerms) {
    }


    protected static DataReader getDataReader() {
        return Expressions.getDataReader();
    }

    protected static DataWriter getDataWriter() {
        return Expressions.getDataWriter();
    }

}
