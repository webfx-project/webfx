package webfx.fxkit.gwt.mapper.shared;

import elemental2.dom.Element;

public interface SvgRoot {

    Element getDefsElement();

    default Element addDef(Element def) {
        getDefsElement().appendChild(def);
        return def;
    }

}
