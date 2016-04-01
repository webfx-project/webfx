package naga.core.spi.plat;

import naga.core.spi.json.JsonFactory;

/**
 * @author Bruno Salmon
 */
public class Cn1Platform implements Platform {

    @Override
    public Type type() {
        return null;
    }

    @Override
    public Net net() {
        return null;
    }

    @Override
    public Scheduler scheduler() {
        return null;
    }

    @Override
    public JsonFactory jsonFactory() {
        return null;
    }
}
