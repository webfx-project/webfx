package naga.toolkit.cell.renderers;

import naga.commons.type.Type;

/**
 * @author Bruno Salmon
 */
public interface CellRendererFactory {

    CellRenderer createCellRenderer(Type type);

    static CellRendererFactory getDefault() {
        return DefaultCellRendererFactory.INSTANCE;
    }

}
