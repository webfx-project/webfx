package naga.fx.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import naga.fx.scene.Node;

/**
 * Label is a non-editable text control. A Label is useful for displaying
 * text that is required to fit within a specific space, and thus may need
 * to use an ellipsis or truncation to size the string to fit. Labels also are
 * useful in that they can have mnemonics which, if used, will send focus to
 * the Control listed as the target of the <code>labelFor</code> property.
 * <p>
 * Label sets focusTraversable to false.
 * </p>
 *
 * <p>Example:
 * <pre><code>Label label = new Label("a label");</code></pre>
 * @since JavaFX 2.0
 */
public class Label extends Labeled {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates an empty label
     */
    public Label() {
        initialize();
    }

    /**
     * Creates Label with supplied text.
     * @param text null text is treated as the empty string
     */
    public Label(String text) {
        super(text);
        initialize();
    }

    /**
     * Creates a Label with the supplied text and graphic.
     * @param text null text is treated as the empty string
     * @param graphic a null graphic is acceptable
     */
    public Label(String text, Node graphic) {
        super(text, graphic);
        initialize();
    }

    private void initialize() {
        getStyleClass().setAll("label");
        //setAccessibleRole(AccessibleRole.TEXT);
        // Labels are not focus traversable, unlike most other UI Controls.
        // focusTraversable is styleable through css. Calling setFocusTraversable
        // makes it look to css like the user set the value and css will not
        // override. Initializing focusTraversable by calling set on the
        // CssMetaData ensures that css will be able to override the value.
        //((StyleableProperty<Boolean>)(WritableValue<Boolean>)focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * A Label can act as a label for a different Control or
     * Node. This is used for Mnemonics and Accelerator parsing.
     * This allows setting of the target Node.
     */
    public ObjectProperty<Node> labelForProperty() {
        if (labelFor == null) {
            labelFor = new SimpleObjectProperty<>() /* {
                Node oldValue = null;
                @Override protected void invalidated() {
                    if (oldValue != null) {
                        NodeHelper.getNodeAccessor().setLabeledBy(oldValue, null);
                        oldValue.impl_showMnemonicsProperty().removeListener(mnemonicStateListener);
                    }
                    final Node node = get();
                    if (node != null) {
                        NodeHelper.getNodeAccessor().setLabeledBy(node, Label.this);
                        node.impl_showMnemonicsProperty().addListener(mnemonicStateListener);
                        impl_setShowMnemonics(node.impl_isShowMnemonics());
                    } else {
                        impl_setShowMnemonics(false);
                    }
                    oldValue = node;
                }

                @Override public Object getBean() {
                    return Label.this;
                }

                @Override public String getName() {
                    return "labelFor";
                }
            } */;
        }
        return labelFor;
    }
    private ObjectProperty<Node> labelFor;

    public final void setLabelFor(Node value) { labelForProperty().setValue(value); }
    public final Node getLabelFor() { return labelFor == null ? null : labelFor.getValue(); }

}
