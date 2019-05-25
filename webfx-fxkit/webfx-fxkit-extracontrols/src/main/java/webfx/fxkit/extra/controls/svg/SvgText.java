package webfx.fxkit.extra.controls.svg;

import javafx.scene.text.Text;
import webfx.fxkit.extra.controls.registry.ExtraControlsRegistry;

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
        ExtraControlsRegistry.registerSvgText();
    }
}
