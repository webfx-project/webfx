package mongooses.core.activities.sharedends.authn;

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
import mongooses.core.sharedends.activities.generic.MongooseButtonFactoryMixin;
import mongooses.core.sharedends.activities.generic.MongooseSectionFactoryMixin;
import mongooses.core.sharedends.logic.ui.validation.MongooseValidationSupport;
import webfx.framework.services.authn.AuthenticationRequest;
import webfx.framework.services.authn.UsernamePasswordCredentials;
import webfx.framework.services.i18n.I18n;
import webfx.framework.ui.util.anim.Animations;
import webfx.framework.ui.controls.button.ButtonUtil;
import webfx.framework.ui.controls.dialog.GridPaneBuilder;
import webfx.framework.ui.layouts.LayoutUtil;
import webfx.framework.ui.layouts.SceneUtil;
import webfx.framework.ui.uirouter.uisession.UiSession;
import webfx.fxkits.core.util.properties.Properties;


/**
 * @author Bruno Salmon
 */
public final class LoginPanel implements MongooseButtonFactoryMixin, MongooseSectionFactoryMixin {
    private final Node node;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button button;
    private final Property<Boolean> signInMode = new SimpleObjectProperty<>(true);
    private final MongooseValidationSupport validationSupport = new MongooseValidationSupport();

    public LoginPanel(UiSession uiSession) {
        BorderPane loginWindow = createSectionPanel("SignInWindowTitle");
        Hyperlink hyperLink = newHyperlink("ForgotPassword?", e -> signInMode.setValue(!signInMode.getValue()));
        GridPane gridPane;
        loginWindow.setCenter(
                gridPane = new GridPaneBuilder()
                        .addNodeFillingRow(usernameField = newMaterialTextField("Email", "EmailPlaceholder"))
                        .addNodeFillingRow(passwordField = newMaterialPasswordField("Password", "PasswordPlaceholder"))
                        .addNewRow(hyperLink)
                        .addNodeFillingRow(button = newLargeGreenButton(null))
                .build()
        );
        gridPane.setPadding(new Insets(20));
        GridPane.setHalignment(hyperLink, HPos.CENTER);
        hyperLink.setOnAction(e -> signInMode.setValue(!signInMode.getValue()));
        LayoutUtil.setUnmanagedWhenInvisible(passwordField, signInMode);
        Properties.runNowAndOnPropertiesChange(() ->
            I18n.translateText(button, signInMode.getValue() ? "SignIn>>" : "SendPassword>>")
        , signInMode);
        node = LayoutUtil.createGoldLayout(loginWindow);
        initValidation();
        button.setOnAction(event -> {
            if (validationSupport.isValid())
                new AuthenticationRequest()
                    .setUserCredentials(new UsernamePasswordCredentials(usernameField.getText(), passwordField.getText()))
                    .executeAsync().setHandler(ar -> {
                        if (ar.succeeded())
                            uiSession.setUserPrincipal(ar.result());
                        else
                            Animations.shake(loginWindow);
                    });
        });
        prepareShowing();
    }

    private void initValidation() {
        validationSupport.addRequiredInput(usernameField, "Username is required");
        validationSupport.addRequiredInput(passwordField, "Password is required");
    }

    public Node getNode() {
        return node;
    }

    public void prepareShowing() {
        // Resetting the default button (required for JavaFx if displayed a second time)
        ButtonUtil.resetDefaultButton(button);
        SceneUtil.autoFocusIfEnabled(usernameField);
    }
}