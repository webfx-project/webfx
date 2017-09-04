package mongoose.activities.shared.book.event.shared;

import de.saxsys.mvvmfx.utils.validation.ObservableRuleBasedValidator;
import de.saxsys.mvvmfx.utils.validation.ObservableRules;
import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import de.saxsys.mvvmfx.utils.validation.visualization.ControlsFxVisualizer;
import de.saxsys.mvvmfx.utils.validation.visualization.ValidationVisualizer;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import mongoose.activities.shared.generic.MongooseButtonFactoryMixin;
import naga.commons.util.collection.Collections;
import naga.framework.ui.auth.UiUser;
import naga.framework.ui.controls.ButtonUtil;
import naga.framework.ui.controls.GridPaneBuilder;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;
import naga.platform.services.auth.UsernamePasswordToken;
import naga.platform.services.auth.spi.AuthService;
import naga.platform.spi.Platform;


/**
 * @author Bruno Salmon
 */
public class LoginPanel implements MongooseButtonFactoryMixin {
    private final I18n i18n;
    private final Node node;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Hyperlink hyperLink;
    private final Button button;
    private final Property<Boolean> signInMode = new SimpleObjectProperty<>(true);

    public LoginPanel(UiUser uiUser, I18n i18n, AuthService authService) {
        this.i18n = i18n;
        BorderPane loginWindow = createSectionPanel("SignInWindowTitle");
        GridPane gridPane;
        loginWindow.setCenter(
                gridPane = new GridPaneBuilder(i18n)
                        .addNodeFillingRow(i18n.translatePromptText(usernameField = new TextField(), "EmailPlaceholder"))
                        .addNodeFillingRow(i18n.translatePromptText(passwordField = new PasswordField(), "PasswordPlaceholder"))
                        .addNewRow(i18n.translateText(hyperLink = new Hyperlink(), "ForgotPassword?"))
                        .addNodeFillingRow(LayoutUtil.setMaxWidthToInfinite(button = newButton()))
                .build()
        );
        gridPane.setPadding(new Insets(20));
        GridPane.setHalignment(hyperLink, HPos.CENTER);
        hyperLink.setOnAction(e -> signInMode.setValue(!signInMode.getValue()));
        LayoutUtil.setUnmanagedWhenInvisible(passwordField, signInMode);
        Properties.runNowAndOnPropertiesChange(p ->
            i18n.translateText(button, signInMode.getValue() ? "SignIn>>" : "SendPassword>>")
        , signInMode);
        node = LayoutUtil.createGoldLayout(loginWindow);
        ObservableRuleBasedValidator validator = new ObservableRuleBasedValidator();
        validator.addRule(ObservableRules.notEmpty(usernameField.textProperty()), ValidationMessage.error("Username is required"));
        validator.addRule(ObservableRules.notEmpty(passwordField.textProperty()), ValidationMessage.error("Password is required"));
        ValidationVisualizer validationVisualizer = new ControlsFxVisualizer();
        validationVisualizer.initVisualization(validator.getValidationStatus(), usernameField, true);
        validationVisualizer.initVisualization(validator.getValidationStatus(), passwordField, true);
        button.setOnAction(event -> {
            if (!validator.getValidationStatus().isValid())
                Platform.log(Collections.toString(validator.getValidationStatus().getMessages()));
            else
                authService.authenticate(new UsernamePasswordToken(usernameField.getText(), passwordField.getText())).setHandler(ar -> {
                    if (ar.succeeded())
                        uiUser.setUser(ar.result());
                });
        });
        prepareShowing();
    }

    @Override
    public I18n getI18n() {
        return i18n;
    }

    public Node getNode() {
        return node;
    }

    public void prepareShowing() {
        // Resetting the default button (required for JavaFx if displayed a second time)
        ButtonUtil.resetDefaultButton(button);
        usernameField.requestFocus();
    }
}
