package naga.core.spi.json.listmap;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class MapCompositeObject extends MapBasedCompositeObject {

    protected Map<String, Object> map;

    protected MapCompositeObject() {  // super constructor will call recreateEmptyNativeObject() to initialize the map
    }

    protected MapCompositeObject(Map map) {
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

}
