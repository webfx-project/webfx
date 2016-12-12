package naga.toolkit.fx.ext.control;

import naga.toolkit.fx.ext.impl.HtmlTextImpl;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.properties.markers.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public interface HtmlText extends Region,
        HasTextProperty {

    static HtmlText create() {
        return new HtmlTextImpl();
    }

    static HtmlText create(String text) {
        return new HtmlTextImpl(text);
    }

}
