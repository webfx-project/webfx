package webfx.kit.mapper.peers.javafxgraphics.gwt.shared;

import elemental2.dom.Element;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;

public class SvgRootBase implements SvgRoot {

    private final Element defsElement = SvgUtil.createSvgDefs();

    public Element getDefsElement() {
        return defsElement;
    }
}
