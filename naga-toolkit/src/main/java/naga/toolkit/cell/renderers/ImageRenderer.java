package naga.toolkit.cell.renderers;

import naga.commons.keyobject.KeyObject;
import naga.commons.util.Strings;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
class ImageRenderer<N> implements ValueRenderer<N> {

    public static ImageRenderer SINGLETON = new ImageRenderer();

    private ImageRenderer() {}

    @Override
    public GuiNode<N> renderCellValue(Object value) {
        boolean isKeyObject = value instanceof KeyObject;
        KeyObject o = isKeyObject ? (KeyObject) value : null;
        String url = isKeyObject ? o.getString("url") : Strings.toString(value);
        Double width = isKeyObject ? o.getDouble("width") : null;
        Double height = isKeyObject ? o.getDouble("width") : null;
        return Toolkit.get().createImage(url, width, height);
    }
}
