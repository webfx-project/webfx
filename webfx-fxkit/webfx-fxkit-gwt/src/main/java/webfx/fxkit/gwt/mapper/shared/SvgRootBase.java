package webfx.fxkit.gwt.mapper.shared;

import elemental2.dom.Element;
import webfx.fxkit.gwt.mapper.util.SvgUtil;

public class SvgRootBase implements SvgRoot {

    private final Element defsElement = SvgUtil.createSvgDefs();

    public Element getDefsElement() {
        return defsElement;
    }
}
