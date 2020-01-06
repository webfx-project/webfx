package webfx.platform.shared.datascope;

import webfx.platform.shared.services.json.JsonArray;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.services.serial.SerialCodecManager;
import webfx.platform.shared.services.serial.spi.impl.SerialCodecBase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class AggregateScope implements KeyDataScope {

    public static String KEY = "aggregate";

    private final Map<Object /* Aggregate type */, Object[] /* aggregate keys */> aggregates;

    public AggregateScope(Map<Object, Object[]> aggregates) {
        this.aggregates = aggregates;
    }

    @Override
    public Object getKey() {
        return KEY;
    }

    @Override
    public boolean intersects(DataScope otherScope) {
        return otherScope instanceof AggregateScope && intersects((AggregateScope) otherScope)
                || otherScope instanceof MultiKeyDataScope && ((MultiKeyDataScope) otherScope).intersects(this)
                ;
    }

    public boolean intersects(AggregateScope otherScope) {
        for (Map.Entry<Object, Object[]> entry : aggregates.entrySet()) {
            Object aggregateType = entry.getKey();
            Object[] otherAggregateKeys = otherScope.aggregates.get(aggregateType);
            if (otherAggregateKeys != null) {
                Object[] aggregateKeys = entry.getValue();
                if (ScopeUtil.arraysIntersect(aggregateKeys, otherAggregateKeys))
                    return true;
            }
        }
        return false;
    }

    public static AggregateScopeBuilder builder() {
        return new AggregateScopeBuilder();
    }

    /**************************************
     *           Serial Codec             *
     * ***********************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<AggregateScope> {

        private static final String CODEC_ID = "AggregateScope";

        public ProvidedSerialCodec() {
            super(AggregateScope.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(AggregateScope arg, WritableJsonObject json) {
            for (Map.Entry<Object, Object[]> entry : arg.aggregates.entrySet())
                json.set(entry.getKey().toString(), SerialCodecManager.encodePrimitiveArrayToJsonArray(entry.getValue()));
        }

        @Override
        public AggregateScope decodeFromJson(JsonObject json) {
            Map<Object, Object[]> aggregates = new HashMap<>();
            JsonArray keys = json.keys();
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.getString(i);
                aggregates.put(key, SerialCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(key)));
            }
            return new AggregateScope(aggregates);
        }
    }

}
