package naga.framework.ui.controls;

import javafx.scene.control.Labeled;

/**
 * @author Bruno Salmon
 */
public class LabeledUtil {

    public static <T extends Labeled> T setGraphic(T labeled, String urlOrJson) {
        labeled.setGraphic(ImageViewUtil.createImageView(urlOrJson));
        return labeled;
    }
}
