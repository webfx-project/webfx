package webfx.tutorials.service.services.alert.spi.impl.gwt;

import elemental2.dom.DomGlobal;
import webfx.tutorials.service.services.alert.spi.AlertServiceProvider;

/**
 * @author Bruno Salmon
 */
public class GwtAlertServiceProvider implements AlertServiceProvider {

    @Override
    public void alert(String message) {
        DomGlobal.window.alert(message);
    }
}
