package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.shared;

import elemental2.dom.Element;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.SvgUtil;

public class SvgRootBase implements SvgRoot {

    private final Element defsElement = SvgUtil.createSvgDefs();

    public Element getDefsElement() {
        return defsElement;
    }
}
