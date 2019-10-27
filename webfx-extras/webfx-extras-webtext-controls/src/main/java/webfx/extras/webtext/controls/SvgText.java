package webfx.extras.webtext.controls;

import javafx.scene.text.Text;
import webfx.extras.webtext.controls.registry.WebTextRegistry;

public class SvgText extends Text {

    public SvgText() {
    }

    public SvgText(String text) {
        super(text);
    }

    public SvgText(double x, double y, String text) {
        super(x, y, text);
    }

    static {
        WebTextRegistry.registerSvgText();
    }
}
