package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.datalci.DataReader;
import naga.core.orm.expression.datalci.DataWriter;
import naga.core.type.Type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class TernaryExpression extends AbstractExpression {
    private final Expression question;
    private final Expression yes;
    private final Expression no;

    public TernaryExpression(Expression question, Expression yes, Expression no) {
        super(1);
        this.question = question;
        this.yes = yes;
        this.no = no;
    }

    public Expression getQuestion() {
        return question;
    }

    public Expression getYes() {
        return yes;
    }

    public Expression getNo() {
        return no;
    }

    @Override
    public Type getType() {
        return yes.getType();
    }

    @Override
    public Object evaluate(Object domainObject, DataReader dataReader) {
        Object questionValue = question.evaluate(domainObject, dataReader);
        Expression answer = Boolean.TRUE.equals(questionValue) ? yes : no;
        Object value = answer.evaluate(domainObject, dataReader);
        return value;
    }

    @Override
    public void setValue(Object domainObject, Object value, DataWriter dataWriter) {
        Object questionValue = question.evaluate(domainObject, dataWriter);
        Expression answer = Boolean.TRUE.equals(questionValue) ? yes : no;
        answer.setValue(domainObject, value, dataWriter);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        question.toString(sb);
        sb.append(" ? ");
        yes.toString(sb);
        sb.append(" : ");
        return no.toString(sb);
    }

    @Override
    public void collectPersistentTerms(Collection<Expression> persistentTerms) {
        question.collectPersistentTerms(persistentTerms);
        yes.collectPersistentTerms(persistentTerms);
        no.collectPersistentTerms(persistentTerms);
    }

}
