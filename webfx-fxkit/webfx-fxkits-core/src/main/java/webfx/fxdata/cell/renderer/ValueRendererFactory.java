package webfx.fxdata.cell.renderer;

import webfx.type.Type;

/**
 * @author Bruno Salmon
 */
public interface ValueRendererFactory {

    ValueRenderer createValueRenderer(Type type);

    static ValueRendererFactory getDefault() {
        return ValueRendererFactoryImpl.INSTANCE;
    }

}
