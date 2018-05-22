package naga.framework.expression.builder.terms;

import naga.noreflect.IndexedArray;
import naga.framework.expression.terms.JsonExpression;
import naga.platform.services.json.Json;
import naga.platform.services.json.WritableJsonObject;

/**
 * @author Bruno Salmon
 */
public class JsonExpressionBuilder extends ExpressionBuilder {
    public final WritableJsonObject jsonExpressionBuilders = Json.createObject();

    private JsonExpression jsonExpression;

    public JsonExpressionBuilder() {
    }

    public JsonExpressionBuilder(String key, ExpressionBuilder eb) {
        add(key, eb);
    }

    public JsonExpressionBuilder add(String key, ExpressionBuilder eb) {
        jsonExpressionBuilders.set(key, eb);
        return this;
    }

    public JsonExpression build() {
        if (jsonExpression == null) {
            propagateDomainClasses();
            WritableJsonObject jsonExpressions = Json.createObject();
            IndexedArray keys = jsonExpressionBuilders.keys();
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.getString(i);
                ExpressionBuilder expression = jsonExpressionBuilders.get(key);
                expression.buildingClass = buildingClass;
                jsonExpressions.set(key, expression.build());
            }
            jsonExpression = new JsonExpression(jsonExpressions);
        }
        return jsonExpression;
    }
}
