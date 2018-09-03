package webfx.fxkits.extra.cell.renderer;

import webfx.fxkits.extra.type.Type;

/**
 * @author Bruno Salmon
 */
public interface ValueRendererFactory {

    ValueRenderer createValueRenderer(Type type);

    static ValueRendererFactory getDefault() {
        return ValueRendererFactoryImpl.INSTANCE;
    }

}
