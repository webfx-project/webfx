package naga.framework.expression.terms;

import naga.noreflect.IndexedArray;
import naga.noreflect.KeyObject;
import naga.type.Type;
import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
// TODO: remove platform dependency
import naga.platform.services.json.Json;
import naga.platform.services.json.JsonObject;
import naga.platform.services.json.WritableJsonObject;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class JsonObjectExpression<T> extends AbstractExpression<T> {

    private final KeyObject jsonObjectExpressions;

    public JsonObjectExpression(KeyObject jsonObjectExpressions) {
        super(1);
        this.jsonObjectExpressions = jsonObjectExpressions;
    }

    @Override
    public Type getType() {
        return null; // TODO JsonType?
    }

    @Override
    public JsonObject evaluate(T domainObject, DataReader<T> dataReader) {
        WritableJsonObject json = Json.createObject();
        IndexedArray keys = jsonObjectExpressions.keys();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.getString(i);
            Expression<T> expression = jsonObjectExpressions.get(key);
            json.set(key, expression.evaluate(domainObject, dataReader));
        }
        return json;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append('{');
        IndexedArray keys = jsonObjectExpressions.keys();
        for (int i = 0; i < keys.size(); i++) {
            if (i != 0)
                sb.append(", ");
            String key = keys.getString(i);
            sb.append(key).append(": ");
            Expression<T> expression = jsonObjectExpressions.get(key);
            expression.toString(sb);
        }
        return sb.append('}');
    }

    @Override
    public void collectPersistentTerms(Collection<Expression<T>> persistentTerms) {
        IndexedArray keys = jsonObjectExpressions.keys();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.getString(i);
            Expression<T> expression = jsonObjectExpressions.get(key);
            expression.collectPersistentTerms(persistentTerms);
        }
    }
}
