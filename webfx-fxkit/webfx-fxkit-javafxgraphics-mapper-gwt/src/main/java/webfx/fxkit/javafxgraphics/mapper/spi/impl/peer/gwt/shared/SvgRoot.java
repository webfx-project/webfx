package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.shared;

import elemental2.dom.Element;

public interface SvgRoot {

    Element getDefsElement();

    default Element addDef(Element def) {
        getDefsElement().appendChild(def);
        return def;
    }

}
