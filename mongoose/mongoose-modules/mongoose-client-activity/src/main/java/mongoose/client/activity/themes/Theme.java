package mongoose.client.activity.themes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import webfx.framework.client.ui.controls.dialog.DialogUtil;

/**
 * @author Bruno Salmon
 */
public final class Theme {

    private final static Property<Background> mainBackgroundProperty = new SimpleObjectProperty<>();
    public static Property<Background> mainBackgroundProperty() {
        return mainBackgroundProperty;
    }
    static void setMainBackground(Background mainBackground) {
        mainBackgroundProperty.setValue(mainBackground);
    }

    private final static Property<Paint> mainTextFillProperty = new SimpleObjectProperty<>();
    public static Property<Paint> mainTextFillProperty() {
        return mainTextFillProperty;
    }
    static void setMainTextFill(Paint mainTextFill) {
        mainTextFillProperty.setValue(mainTextFill);
    }

    private final static Property<Background> dialogBackgroundProperty = new SimpleObjectProperty<>();
    public static Property<Background> dialogBackgroundProperty() {
        return dialogBackgroundProperty;
    }
    static void setDialogBackground(Background dialogBackground) {
        dialogBackgroundProperty.setValue(dialogBackground);
    }

    private final static Property<Border> dialogBorderProperty = new SimpleObjectProperty<>();
    public static Property<Border> dialogBorderProperty() {
        return dialogBorderProperty;
    }
    static void setDialogBorder(Border dialogBorder) {
        dialogBorderProperty.setValue(dialogBorder);
    }

    private final static Property<Paint> dialogTextFillProperty = new SimpleObjectProperty<>();
    public static Property<Paint> dialogTextFillProperty() {
        return dialogTextFillProperty;
    }
    static void setDialogTextFill(Paint dialogTextFill) {
        dialogTextFillProperty.setValue(dialogTextFill);
    }

    static {
        DialogUtil.dialogBackgroundProperty().bind(dialogBackgroundProperty);
        DialogUtil.dialogBorderProperty().bind(dialogBorderProperty);
        new LightTheme().apply();
    }

}
