package webfx.extras.webtext.controls;

import webfx.extras.webtext.controls.registry.WebTextRegistry;

/**
 * @author Bruno Salmon
 */
public final class HtmlTextEditor extends HtmlText {

    static {
        WebTextRegistry.registerHtmlTextEditor();
    }
}
