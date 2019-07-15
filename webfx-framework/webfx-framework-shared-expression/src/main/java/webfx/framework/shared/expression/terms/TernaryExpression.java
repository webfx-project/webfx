package webfx.framework.shared.expression.terms;

import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.lci.DataReader;
import webfx.framework.shared.expression.lci.DataWriter;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class TernaryExpression<T> extends AbstractExpression<T> {
    private final Expression<T> question;
    private final Expression<T> yes;
    private final Expression<T> no;

    public TernaryExpression(Expression<T> question, Expression<T> yes, Expression<T> no) {
        super(1);
        this.question = question;
        this.yes = yes;
        this.no = no;
    }

    public Expression<T> getQuestion() {
        return question;
    }

    public Expression<T> getYes() {
        return yes;
    }

    public Expression<T> getNo() {
        return no;
    }

    @Override
    public Expression<T> getForwardingTypeExpression() {
        return yes;
    }

    @Override
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        Object questionValue = question.evaluate(domainObject, dataReader);
        Expression<T> answer = Boolean.TRUE.equals(questionValue) ? yes : no;
        Object value = answer.evaluate(domainObject, dataReader);
        return value;
    }

    @Override
    public void setValue(T domainObject, Object value, DataWriter<T> dataWriter) {
        Object questionValue = question.evaluate(domainObject, dataWriter);
        Expression<T> answer = Boolean.TRUE.equals(questionValue) ? yes : no;
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
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        question.collectPersistentTerms(persistentTerms);
        yes.collectPersistentTerms(persistentTerms);
        no.collectPersistentTerms(persistentTerms);
    }

}
