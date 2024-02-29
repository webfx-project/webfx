package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared;

import elemental2.dom.Element;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.SvgUtil;

public class SvgRootBase implements SvgRoot {

    private final Element defsElement = SvgUtil.createSvgDefs();

    public Element getDefsElement() {
        return defsElement;
    }
}
