package naga.toolkit.cell.renderers;

import naga.commons.type.Type;

/**
 * @author Bruno Salmon
 */
public interface ValueRendererFactory {

    ValueRenderer createCellRenderer(Type type);

    static ValueRendererFactory getDefault() {
        return ValueRendererFactoryImpl.INSTANCE;
    }

}
