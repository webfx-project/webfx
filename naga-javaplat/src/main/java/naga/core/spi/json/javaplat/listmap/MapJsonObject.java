package naga.core.spi.json.javaplat.listmap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class MapJsonObject extends MapBasedJsonObject {

    protected Map<String, Object> map;

    protected MapJsonObject() {  // super constructor will call recreateEmptyNativeObject() to initialize the map
    }

    protected MapJsonObject(Map map) {
        this.map = map;
    }

    @Override
    public Map<String, Object> getMap() {
        return map;
    }

    @Override
    protected Object getNativeObject() {
        return map;
    }

    @Override
    protected void recreateEmptyNativeObject() {
        map = new LinkedHashMap<>();
    }

    @Override
    protected void deepCopyNativeObject() {
        map = ListMapUtil.convertMap(map);
    }
}
