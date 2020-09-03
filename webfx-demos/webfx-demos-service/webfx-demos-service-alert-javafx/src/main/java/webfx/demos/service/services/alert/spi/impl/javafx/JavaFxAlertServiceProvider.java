package webfx.demos.service.services.alert.spi.impl.javafx;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import webfx.demos.service.services.alert.spi.AlertServiceProvider;

/**
 * @author Bruno Salmon
 */
public class JavaFxAlertServiceProvider implements AlertServiceProvider {

    @Override
    public void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("From " + getClass().getSimpleName());
        alert.setContentText(message);
        alert.getButtonTypes().add(new ButtonType("Ok"));
        alert.showAndWait();
    }
}
