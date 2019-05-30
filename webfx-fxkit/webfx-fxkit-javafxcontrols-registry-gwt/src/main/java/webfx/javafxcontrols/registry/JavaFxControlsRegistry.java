package webfx.javafxcontrols.registry;

import com.sun.javafx.scene.control.skin.LabeledText;
import com.sun.javafx.scene.control.skin.ToolkitTextBox;
import javafx.scene.control.*;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.gwt.html.*;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.HtmlTextPeer;

import static webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry.registerNodePeerFactory;

public class JavaFxControlsRegistry {

    public static void registerLabeledText() {
        registerNodePeerFactory(LabeledText.class, HtmlTextPeer::new);
    }

    public static void registerLabel() {
        registerNodePeerFactory(Label.class, HtmlLabelPeer::new);
    }

    public static void registerHyperlink() {
        registerNodePeerFactory(Hyperlink.class, HtmlHyperlinkPeer::new);
    }

    public static void registerButton() {
        registerNodePeerFactory(Button.class, HtmlButtonPeer::new);
    }

    public static void registerToggleButton() {
        registerNodePeerFactory(ToggleButton.class, HtmlToggleButtonPeer::new);
    }

    public static void registerCheckBox() {
        registerNodePeerFactory(CheckBox.class, HtmlCheckBoxPeer::new);
    }

    public static void registerRadioButton() {
        registerNodePeerFactory(RadioButton.class, HtmlRadioButtonPeer::new);
    }

    public static void registerSlider() {
        registerNodePeerFactory(Slider.class, HtmlSliderPeer::new);
    }

    public static void registerTextField() {
        registerNodePeerFactory(TextField.class, HtmlTextFieldPeer::new);
    }

    public static void registerPasswordField() {
        registerNodePeerFactory(PasswordField.class, HtmlTextFieldPeer::new);
    }

    public static void registerToolkitTextBox() {
        registerNodePeerFactory(ToolkitTextBox.class, HtmlTextFieldPeer::createHtmlTextBoxPeer);
    }

    public static void registerTextArea() {
        registerNodePeerFactory(TextArea.class, HtmlTextAreaPeer::new);
    }

    public static void registerChoiceBox() {
        registerNodePeerFactory(ChoiceBox.class, HtmlChoiceBoxPeer::new);
    }

    public static void registerDatePicker() {
        registerNodePeerFactory(DatePicker.class, HtmlDatePickerPeer::new);
    }

    public static void registerScrollPane() {
        registerNodePeerFactory(ScrollPane.class, HtmlScrollPanePeer::new);
    }

}
