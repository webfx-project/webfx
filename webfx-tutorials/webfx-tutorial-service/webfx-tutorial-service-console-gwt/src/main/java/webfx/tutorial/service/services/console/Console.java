package webfx.tutorial.service.services.console;

import elemental2.dom.DomGlobal;

/**
 * @author Bruno Salmon
 */
public final class Console {

    public static void log(String message) {
        DomGlobal.console.log(message);
    }

}
