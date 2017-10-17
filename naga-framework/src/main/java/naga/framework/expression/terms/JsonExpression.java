package naga.framework.expression.terms;

import naga.noreflect.IndexedArray;
import naga.noreflect.KeyObject;
import naga.type.Type;
import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
// TODO: remove platform dependency
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class JsonExpression<T> extends AbstractExpression<T> {

    protected final KeyObject jsonExpressions;

    public JsonExpression(KeyObject jsonExpressions) {
        super(1);
        this.jsonExpressions = jsonExpressions;
    }

    @Override
    public Type getType() {
        return null; // TODO JsonType?
    }

    @Override
    public JsonObject evaluate(T domainObject, DataReader<T> dataReader) {
        WritableJsonObject json = Json.createObject();
        IndexedArray keys = jsonExpressions.keys();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.getString(i);
            Expression<T> expression = jsonExpressions.get(key);
            json.set(key, expression.evaluate(domainObject, dataReader));
        }
        return json;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append('{');
        IndexedArray keys = jsonExpressions.keys();
        for (int i = 0; i < keys.size(); i++) {
            if (i != 0)
                sb.append(", ");
            String key = keys.getString(i);
            sb.append(key).append(": ");
            Expression<T> expression = jsonExpressions.get(key);
            expression.toString(sb);
        }
        return sb.append('}');
    }

    @Override
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        IndexedArray keys = jsonExpressions.keys();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.getString(i);
            Expression<T> expression = jsonExpressions.get(key);
            expression.collectPersistentTerms(persistentTerms);
        }
    }
}
