package javafx.scene.control;

import dev.webfx.kit.mapper.peers.javafxgraphics.markers.*;
import dev.webfx.kit.util.properties.FXProperties;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
/**
 * @author Bruno Salmon
 */
public abstract class Labeled extends Control implements
        HasTextProperty,
        HasGraphicProperty,
        HasImageUrlProperty,
        HasFontProperty,
        HasAlignmentProperty,
        HasTextAlignmentProperty,
        HasTextFillProperty {

    private final static String DEFAULT_ELLIPSIS_STRING = "...";

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a Label with no text and graphic
     */
    public Labeled() {
    }

    /**
     * Creates a Label with text
     *
     * @param text The text for the label.
     */
    public Labeled(String text) {
        setText(text);
    }

    /**
     * Creates a Label with text and a graphic
     *
     * @param text    The text for the label.
     * @param graphic The graphic for the label.
     */
    public Labeled(String text, Node graphic) {
        setText(text);
        setGraphic(graphic);
    }

    private final StringProperty textProperty = new SimpleStringProperty();

    @Override
    public StringProperty textProperty() {
        return textProperty;
    }

    private final ObjectProperty<Node> graphicProperty = new SimpleObjectProperty<>() {
        @Override
        protected void invalidated() {
            setScene(getScene()); // This will propagate the scene reference into the graphic
        }
    };

    @Override
    public ObjectProperty<Node> graphicProperty() {
        return graphicProperty;
    }

    private final StringProperty imageUrlProperty = new SimpleStringProperty() {
        @Override
        protected void invalidated() {
            String url = getValue();
            setGraphic(url == null ? null : new ImageView(url));
        }
    };

    @Override
    public StringProperty imageUrlProperty() {
        return imageUrlProperty;
    }

    private final ObjectProperty<Font> fontProperty = new SimpleObjectProperty<>();

    @Override
    public ObjectProperty<Font> fontProperty() {
        return fontProperty;
    }

    private final ObjectProperty<Pos> alignmentProperty = new SimpleObjectProperty<>(Pos.CENTER_LEFT);

    @Override
    public ObjectProperty<Pos> alignmentProperty() {
        return alignmentProperty;
    }

    private final ObjectProperty<TextAlignment> textAlignmentProperty = new SimpleObjectProperty<>(/* null for web CSS instead of TextAlignment.LEFT */);

    @Override
    public ObjectProperty<TextAlignment> textAlignmentProperty() {
        return textAlignmentProperty;
    }

    private final ObjectProperty<Paint> textFillProperty = new SimpleObjectProperty<>(/* null for web CSS instead of Color.BLACK */);

    @Override
    public ObjectProperty<Paint> textFillProperty() {
        return textFillProperty;
    }


    /**
     * If a run of text exceeds the width of the Labeled, then this variable
     * indicates whether the text should wrap onto another line.
     */
    public final BooleanProperty wrapTextProperty() {
        if (wrapText == null) {
            wrapText = new SimpleBooleanProperty(false)/* {

                @Override
                public CssMetaData<Labeled,Boolean> getCssMetaData() {
                    return StyleableProperties.WRAP_TEXT;
                }

                @Override
                public Object getBean() {
                    return Labeled.this;
                }

                @Override
                public String getName() {
                    return "wrapText";
                }
            }*/;
        }
        return wrapText;
    }

    private BooleanProperty wrapText;

    public final void setWrapText(boolean value) {
        wrapTextProperty().setValue(value);
    }

    public final boolean isWrapText() {
        return wrapText != null && wrapText.getValue();
    }

    /**
     * If wrapText is true, then contentBias will be HORIZONTAL, otherwise it is null.
     * @return orientation of width/height dependency or null if there is none
     */
    @Override public Orientation getContentBias() {
        return isWrapText()? Orientation.HORIZONTAL : null;
    }

    /**
     * Specifies the behavior to use if the text of the {@code Labeled}
     * exceeds the available space for rendering the text.
     */
    public final ObjectProperty<OverrunStyle> textOverrunProperty() {
        if (textOverrun == null) {
            textOverrun = new SimpleObjectProperty<>(OverrunStyle.ELLIPSIS)/* {

                @Override
                public CssMetaData<Labeled,OverrunStyle> getCssMetaData() {
                    return StyleableProperties.TEXT_OVERRUN;
                }

                @Override
                public Object getBean() {
                    return Labeled.this;
                }

                @Override
                public String getName() {
                    return "textOverrun";
                }
            }*/;
        }
        return textOverrun;
    }

    private ObjectProperty<OverrunStyle> textOverrun;

    public final void setTextOverrun(OverrunStyle value) {
        textOverrunProperty().setValue(value);
    }

    public final OverrunStyle getTextOverrun() {
        return textOverrun == null ? OverrunStyle.ELLIPSIS : textOverrun.getValue();
    }

    /**
     * Specifies the string to display for the ellipsis when text is truncated.
     * <p>
     * <table border="0" cellpadding="0" cellspacing="0"><tr><th>Examples</th></tr>
     * <tr class="altColor"><td align="right">"..."</td>        <td>- Default value for most locales</td>
     * <tr class="rowColor"><td align="right">" . . . "</td>    <td></td>
     * <tr class="altColor"><td align="right">" [...] "</td>    <td></td>
     * <tr class="rowColor"><td align="right">"&#92;u2026"</td> <td>- The Unicode ellipsis character '&hellip;'</td>
     * <tr class="altColor"><td align="right">""</td>           <td>- No ellipsis, just display the truncated string</td>
     * </table>
     * <p>
     * <p>Note that not all fonts support all Unicode characters.
     *
     * @see <a href="http://en.wikipedia.org/wiki/Ellipsis#Computer_representations">Wikipedia:ellipsis</a>
     * @since JavaFX 2.2
     */
    public final StringProperty ellipsisStringProperty() {
        if (ellipsisString == null) {
            ellipsisString = new SimpleStringProperty(DEFAULT_ELLIPSIS_STRING)/* {
                @Override
                public Object getBean() {
                    return Labeled.this;
                }

                @Override
                public String getName() {
                    return "ellipsisString";
                }

                @Override
                public CssMetaData<Labeled, String> getCssMetaData() {
                    return StyleableProperties.ELLIPSIS_STRING;
                }
            }*/;
        }
        return ellipsisString;
    }

    private StringProperty ellipsisString;

    public final void setEllipsisString(String value) {
        ellipsisStringProperty().set((value == null) ? "" : value);
    }

    public final String getEllipsisString() {
        return ellipsisString == null ? DEFAULT_ELLIPSIS_STRING : ellipsisString.get();
    }

    /**
     * Specifies the space in pixel between lines.
     * @since JavaFX 8.0
     */
    public final DoubleProperty lineSpacingProperty() {
        if (lineSpacing == null) {
            lineSpacing = new SimpleDoubleProperty(0d)/* {

                @Override
                public CssMetaData<Labeled,Number> getCssMetaData() {
                    return StyleableProperties.LINE_SPACING;
                }

                @Override
                public Object getBean() {
                    return Labeled.this;
                }

                @Override
                public String getName() {
                    return "lineSpacing";
                }
            }*/;
        }
        return lineSpacing;
    }
    private DoubleProperty lineSpacing;
    public final void setLineSpacing(double value) { lineSpacingProperty().setValue(value); }
    public final double getLineSpacing() { return lineSpacing == null ? 0 : lineSpacing.getValue(); }

    /**
     * Specifies the positioning of the graphic relative to the text.
     */
    public final ObjectProperty<ContentDisplay> contentDisplayProperty() {
        if (contentDisplay == null) {
            contentDisplay = new SimpleObjectProperty<>(ContentDisplay.LEFT)/* {

                @Override
                public CssMetaData<Labeled,ContentDisplay> getCssMetaData() {
                    return StyleableProperties.CONTENT_DISPLAY;
                }

                @Override
                public Object getBean() {
                    return Labeled.this;
                }

                @Override
                public String getName() {
                    return "contentDisplay";
                }
            }*/;
        }
        return contentDisplay;
    }

    private ObjectProperty<ContentDisplay> contentDisplay;

    public final void setContentDisplay(ContentDisplay value) {
        contentDisplayProperty().setValue(value);
    }

    public final ContentDisplay getContentDisplay() {
        return contentDisplay == null ? ContentDisplay.LEFT : contentDisplay.getValue();
    }

    /**
     * The padding around the Labeled's text and graphic content.
     * By default labelPadding is Insets.EMPTY and cannot be set to null.
     * Subclasses may add nodes outside this padding and inside the Labeled's padding.
     * <p>
     * This property can only be set from CSS.
     */
    public final Property<Insets> labelPaddingProperty() {
        return labelPaddingPropertyImpl();
    }

    private ObjectProperty<Insets> labelPaddingPropertyImpl() {
        if (labelPadding == null) {
            labelPadding = new SimpleObjectProperty<Insets>(Insets.EMPTY) {
                private Insets lastValidValue = Insets.EMPTY;

                @Override
                public void invalidated() {
                    final Insets newValue = get();
                    if (newValue == null) {
                        set(lastValidValue);
                        throw new NullPointerException("cannot set labelPadding to null");
                    }
                    lastValidValue = newValue;
                    requestLayout();
                }

/*
                @Override
                public CssMetaData<Labeled,Insets> getCssMetaData() {
                    return StyleableProperties.LABEL_PADDING;
                }
*/

                @Override
                public Object getBean() {
                    return Labeled.this;
                }

                @Override
                public String getName() {
                    return "labelPadding";
                }
            };
        }
        return labelPadding;
    }

    private ObjectProperty<Insets> labelPadding;

    private void setLabelPadding(Insets value) {
        labelPaddingPropertyImpl().set(value);
    }

    public final Insets getLabelPadding() {
        return labelPadding == null ? Insets.EMPTY : labelPadding.get();
    }

    /**
     * The amount of space between the graphic and text
     */
    public final DoubleProperty graphicTextGapProperty() {
        if (graphicTextGap == null) {
            graphicTextGap = new SimpleDoubleProperty(4d)/* {

                @Override
                public CssMetaData<Labeled,Number> getCssMetaData() {
                    return StyleableProperties.GRAPHIC_TEXT_GAP;
                }

                @Override
                public Object getBean() {
                    return Labeled.this;
                }

                @Override
                public String getName() {
                    return "graphicTextGap";
                }
            }*/;
        }
        return graphicTextGap;
    }

    private DoubleProperty graphicTextGap;

    public final void setGraphicTextGap(double value) {
        graphicTextGapProperty().setValue(value);
    }

    public final double getGraphicTextGap() {
        return graphicTextGap == null ? 4 : graphicTextGap.getValue();
    }

    @Override
    public void setScene(Scene scene) {
        super.setScene(scene);
        Node graphic = getGraphic();
        if (graphic != null)
            graphic.setScene(scene);
    }

    { // WebFX
        // Requesting a new layout pass on text and image properties change
        FXProperties.runOnPropertiesChange(this::requestParentLayout,
            textProperty, graphicProperty, fontProperty(), alignmentProperty(), textAlignmentProperty());
    }

    @Override
    public String toString() {
        return super.toString() + " id = " + getId() + " - text = " + getText();
    }

}
