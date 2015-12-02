package naga.core.spi.json.javaplat.listmap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class MapJsonObject extends MapBasedJsonObject {

    protected Map<String, Object> map;

    public MapJsonObject() {
        recreateEmptyNativeObject();
    }

    public MapJsonObject(Map map) {
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
    protected void deepCloneNativeObject() {
        map = ListMapUtil.convertMap(map);
    }
}
