package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared;

import elemental2.dom.Element;

public interface SvgRoot {

    Element getDefsElement();

    default Element addDef(Element def) {
        getDefsElement().appendChild(def);
        return def;
    }

}
