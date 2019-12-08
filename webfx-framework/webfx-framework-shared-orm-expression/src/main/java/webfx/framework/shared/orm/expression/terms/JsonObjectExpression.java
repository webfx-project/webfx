package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.platform.shared.util.noreflect.IndexedArray;
import webfx.platform.shared.util.noreflect.KeyObject;
import webfx.extras.type.Type;
// TODO: remove platform dependency
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonObject;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class JsonObjectExpression<T> extends AbstractExpression<T> {

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
    public JsonObject evaluate(T domainObject, DomainReader<T> domainReader) {
        WritableJsonObject json = Json.createObject();
        IndexedArray keys = jsonObjectExpressions.keys();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.getString(i);
            Expression<T> expression = jsonObjectExpressions.get(key);
            json.set(key, expression.evaluate(domainObject, domainReader));
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
