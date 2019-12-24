package webfx.framework.shared.orm.expression.terms;

import webfx.extras.type.Type;
import webfx.extras.type.Types;
import webfx.framework.shared.orm.expression.CollectOptions;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.platform.shared.util.Strings;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class Constant<T> extends AbstractExpression<T> {

    private static final Map<Object, Constant> predefinedConstants = new HashMap<>();
    public static final Constant NULL;

    static {
        Object[] predefinedValues = new Object[] {null, true, false, 0};
        for (Object predefinedValue : predefinedValues)
            predefinedConstants.put(predefinedValue, new Constant(predefinedValue, null));
        NULL = predefinedConstants.get(null);
    }

    private final Object constantValue;
    private final String text;

    private Constant(Object constantValue, String text) {
        super(9);
        this.constantValue = constantValue;
        this.text = text;
    }

    public Object getConstantValue() {
        return constantValue;
    }

    public static <T> Constant<T> newConstant(Object constantValue) {
        return newConstant(constantValue, null);
    }

    public static <T> Constant<T> newConstant(Object constantValue, String text) {
        Constant<T> constant = predefinedConstants.get(constantValue);
        if (constant == null)
            constant = new Constant<>(constantValue, text);
        return constant;
    }

    @Override
    public Object evaluate(T domainObject, DomainReader<T> domainReader) {
        return constantValue;
    }

    @Override
    public Type getType() {
        return Types.guessType(constantValue);
    }

    @Override
    public void collect(CollectOptions options) {
        if (!options.filterPersistentTerms())
            options.addTerm(this);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append(toString());
    }

    @Override
    public String toString() {
        return text != null ? text : constantValue == null ? "null" : (constantValue instanceof String) ? toQuotedString((String) constantValue, false) : constantValue instanceof Date ? "'" + constantValue + "'" : constantValue.toString();
    }

    private static String toQuotedString(String s, boolean sql) {
        return sql ? "'" + Strings.replaceAll(s, "'", "''") + "'" : "\"" + Strings.replaceAll(s, "\"", "\\\"") + "\"" ;
    }
}
