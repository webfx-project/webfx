package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared;

import elemental2.dom.Element;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.SvgUtil;

public class SvgRootBase implements SvgRoot {

    private final Element defsElement = SvgUtil.createSvgDefs();

    public Element getDefsElement() {
        return defsElement;
    }
}
