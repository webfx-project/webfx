package dev.webfx.platform.shared.services.json.spi.impl.listmap;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class MapJsonObject extends MapBasedJsonObject {

    protected Map<String, Object> map;

    public MapJsonObject() {  // super constructor will call recreateEmptyNativeObject() to initialize the map
    }

    protected MapJsonObject(Map map) {
        super(map);
    }

    @Override
    public Map<String, Object> getMap() {
        return map;
    }

    @Override
    protected void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public Map<String, Object> getNativeElement() {
        return map;
    }

    // Added default implementation due to TeaVm bug
    @Override
    public Object javaToNativeScalar(Object scalar) {
        return scalar;
    }
}
