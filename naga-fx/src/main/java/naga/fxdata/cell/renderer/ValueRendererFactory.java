package naga.fxdata.cell.renderer;

import naga.type.Type;

/**
 * @author Bruno Salmon
 */
public interface ValueRendererFactory {

    ValueRenderer createValueRenderer(Type type);

    static ValueRendererFactory getDefault() {
        return ValueRendererFactoryImpl.INSTANCE;
    }

}
