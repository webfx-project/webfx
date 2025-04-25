package javafx.scene.control.skin;

import com.sun.javafx.scene.control.LabeledText;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.util.Strings;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.Objects;

import static javafx.scene.control.ContentDisplay.*;
import static javafx.scene.control.OverrunStyle.CLIP;

public abstract class LabeledSkinBase<C extends Labeled, B extends BehaviorBase<C>> extends BehaviorSkinBase<C, B> {

    /**
     *  The Text node used to display the text. This is package only
     *  for the sake of testing!
     */
    LabeledText text;
    final Text noWrappingText = new Text(); { noWrappingText.setVisible(false); } // WebFX addition (to remove if possible)
    private double noWrappingTextWidth;

    /**
     * Indicates that the text content is invalid and needs to be updated.
     * This is package private only for the sake of testing.
     */
    boolean invalidText = true;

    /**
     * A reference to the last-known graphic on the Labeled. This reference
     * is kept so that we can remove listeners from the old graphic later
     */
    Node graphic;

    /**
     * The cached full width of the non-truncated text. We only want to
     * recompute this if the text has itself changed, or if the font has changed.
     * This is package private ONLY FOR THE SAKE OF TESTING
     */
    double textWidth = Double.NEGATIVE_INFINITY;

    /**
     * The cached width of the ellipsis string. This will be recomputed
     * if the font or the ellipsisString property have changed.
     * This is package private ONLY FOR THE SAKE OF TESTING
     */
    double ellipsisWidth = Double.NEGATIVE_INFINITY;

    /**
     * A listener which is applied to the graphic whenever the graphic is set
     * and is visible within the labeled. For example, if there is a graphic
     * defined on the Labeled but the ContentDisplay is set to TEXT_ONLY, then
     * we will not bother installing this listener on the graphic. In all
     * other cases, if the graphic is defined, it will have this listener
     * added to it, which ensures that if the graphic's layout bounds change,
     * we end up performing a layout and potentially update the visible text.
     *
     * This is package private ONLY FOR THE SAKE OF TESTING
     */
/*
    final InvalidationListener graphicPropertyChangedListener = valueModel -> {
        invalidText = true;
        getSkinnable().requestLayout();
    };
*/

    private Rectangle textClip;
    private double wrapWidth;
    private double wrapHeight;


    /**
     * Constructor for LabeledSkinBase. The Labeled must be specified, and cannot be null.
     * At the conclusion of the constructor call, the skin will be marked as
     * needsLayout, and will be fully configured based on the current state of
     * the labeled. Any subsequent changes to the Labeled will be handled via
     * listeners and applied appropriately.
     *
     * @param labeled
     */
    public LabeledSkinBase(final C labeled, final B behavior) {
        super(labeled, behavior);

        // Configure the Text node with all of the attributes from the
        // Labeled which apply to it.
        text = new LabeledText(labeled);

        updateChildren();

        // Labels do not block the mouse by default, unlike most other UI Controls.
        //consumeMouseEvents(false);

        // Register listeners
        registerChangeListener(labeled.ellipsisStringProperty(), "ELLIPSIS_STRING");
        registerChangeListener(labeled.widthProperty(), "WIDTH");
        registerChangeListener(labeled.heightProperty(), "HEIGHT");
        registerChangeListener(labeled.textFillProperty(), "TEXT_FILL");
        registerChangeListener(labeled.fontProperty(), "FONT");
        registerChangeListener(labeled.graphicProperty(), "GRAPHIC");
        registerChangeListener(labeled.contentDisplayProperty(), "CONTENT_DISPLAY");
        registerChangeListener(labeled.labelPaddingProperty(), "LABEL_PADDING");
        registerChangeListener(labeled.graphicTextGapProperty(), "GRAPHIC_TEXT_GAP");
        registerChangeListener(labeled.alignmentProperty(), "ALIGNMENT");
        //registerChangeListener(labeled.mnemonicParsingProperty(), "MNEMONIC_PARSING");
        registerChangeListener(labeled.textProperty(), "TEXT");
        registerChangeListener(labeled.textAlignmentProperty(), "TEXT_ALIGNMENT");
        registerChangeListener(labeled.textOverrunProperty(), "TEXT_OVERRUN");
        registerChangeListener(labeled.wrapTextProperty(), "WRAP_TEXT");
        //registerChangeListener(labeled.underlineProperty(), "UNDERLINE");
        registerChangeListener(labeled.lineSpacingProperty(), "LINE_SPACING");
        registerChangeListener(labeled.sceneProperty(), "SCENE");
    }

    /***************************************************************************
     *                                                                         *
     * Control State Changes                                                   *
     *                                                                         *
     **************************************************************************/

    @Override protected void handleControlPropertyChanged(String p) {
        super.handleControlPropertyChanged(p);
        /*
         * There are basically 2 things to worry about in each of these handlers
         *  1) Update the Text node
         *  2) Have the text metrics changed?
         *
         * If the metrics have changed, we need to request a layout and invalidate
         * the text so that we recompute the display text on next read.
         */
        if ("WIDTH".equals(p)) {
            updateWrappingWidth();
            invalidText = true;
            // No requestLayout() because Control will force a layout
        } else if ("HEIGHT".equals(p)) {
            invalidText = true;
            // No requestLayout() because Control will force a layout
        } else if ("FONT".equals(p)) {
            textMetricsChanged();
            invalidateWidths();
            ellipsisWidth = Double.NEGATIVE_INFINITY;
        } else if ("GRAPHIC".equals(p)) {
            updateChildren();
            textMetricsChanged();
        } else if ("CONTENT_DISPLAY".equals(p)) {
            updateChildren();
            textMetricsChanged();
        } else if ("LABEL_PADDING".equals(p)) {
            textMetricsChanged();
        } else if ("GRAPHIC_TEXT_GAP".equals(p)) {
            textMetricsChanged();
        } else if ("ALIGNMENT".equals(p)) {
            // Doesn't involve text metrics because if the text is too long, then
            // it will already have fit all available width and a change to hpos
            // has no effect. Or it is too short (i.e. it all fits) and we don't
            // have to worry about truncation. So just call request layout.
            // Doesn't involve text metrics because if the text is too long, then
            // it will already have fit all available height and a change to vpos
            // has no effect. Or it is too short (i.e. it all fits) and we don't
            // have to worry about truncation. So just call request layout.
            getSkinnable().requestLayout();
        } else if ("MNEMONIC_PARSING".equals(p)) {
            //containsMnemonic = false;
            textMetricsChanged();
        } else if ("TEXT".equals(p)) {
            updateChildren();
            textMetricsChanged();
            invalidateWidths();
        } else if ("TEXT_ALIGNMENT".equals(p)) {
            // NO-OP
        } else if ("TEXT_OVERRUN".equals(p)) {
            textMetricsChanged();
        } else if ("ELLIPSIS_STRING".equals(p)) {
            textMetricsChanged();
            invalidateWidths();
            ellipsisWidth = Double.NEGATIVE_INFINITY;
        } else if ("WRAP_TEXT".equals(p)) {
            updateWrappingWidth();
            textMetricsChanged();
        } else if ("UNDERLINE".equals(p)) {
            textMetricsChanged();
        } else if ("LINE_SPACING".equals(p)) {
            textMetricsChanged();
        } else if ("SCENE".equals(p)) {
            //sceneChanged();
        }
    }

    protected double topLabelPadding() { // TODOJASPER remove these if you can
        return snapSize(getSkinnable().getLabelPadding().getTop());
    }

    protected double bottomLabelPadding() {
        return snapSize(getSkinnable().getLabelPadding().getBottom());
    }

    protected double leftLabelPadding() {
        return snapSize(getSkinnable().getLabelPadding().getLeft());
    }

    protected double rightLabelPadding() {
        return snapSize(getSkinnable().getLabelPadding().getRight());
    }


    /**
     * Called whenever some state has changed that affects the text metrics.
     * Changes here will involve invalidating the display text so the next
     * call to updateDisplayedText computes a new value, and call requestLayout.
     */
    private void textMetricsChanged() {
        invalidText = true;
        getSkinnable().requestLayout();
    }

    /*
    ** The Label is a mnemonic, and it's target node
    ** has changed, but it's label hasn't so just
    ** swap them over, and tidy up.
    */
/*
    protected void mnemonicTargetChanged() {
        if (containsMnemonic == true) {
            */
/*
            ** was there previously a labelFor
            *//*

            removeMnemonic();

            */
/*
            ** is there a new labelFor
            *//*

            Control control = getSkinnable();
            if (control instanceof Label) {
                labeledNode = ((Label)control).getLabelFor();
                addMnemonic();
            }
            else {
                labeledNode = null;
            }
        }
    }
*/

/*
    private void sceneChanged() {
        final Labeled labeled = getSkinnable();
        Scene scene = labeled.getScene();

        if (scene != null && containsMnemonic) {
            addMnemonic();
        }

    }
*/

    /**
     * Marks minWidth as being invalid and in need of recomputation.
     */
    private void invalidateWidths() {
        textWidth = Double.NEGATIVE_INFINITY;
    }

    /**
     * Updates the content of the underlying Text node. This method should
     * only be called when necessary. If the invalidText flag is not set, then
     * the method is a no-op. This care is taken because recomputing the
     * text to display is an expensive operation. Package private ONLY FOR THE
     * SAKE OF TESTING.
     */
    void updateDisplayedText() {
        updateDisplayedText(-1, -1);
    }

    private void updateDisplayedText(double w, double h) {
        if (invalidText) {
            final Labeled labeled = getSkinnable();
            String s = labeled.getText();

            String result = s;
/*
            int mnemonicIndex = -1;

            */
/*
            ** if there's a valid string then parse it
            *//*

            if (s != null && s.length() > 0) {
                bindings = new TextBinding(s);

                if (!com.sun.javafx.PlatformUtil.isMac() && getSkinnable().isMnemonicParsing() == true) {
                    */
/*
                    ** the Labeled has a MnemonicParsing property,
                    ** if set true, then auto-parsing will check for
                    ** a mnemonic
                    *//*

                    if (labeled instanceof Label) {
                        // buttons etc
                        labeledNode = ((Label)labeled).getLabelFor();
                    } else {
                        labeledNode = labeled;
                    }

                    if (labeledNode == null) {
                        labeledNode = labeled;
                    }
                    mnemonicIndex = bindings.getMnemonicIndex() ;
                }
            }

            */
/*
            ** we were previously a mnemonic
            *//*

            if (containsMnemonic) {
                */
/*
                ** are we no longer a mnemonic, or have we changed code?
                *//*

                if (mnemonicScene != null) {
                    if (mnemonicIndex == -1 ||
                            (bindings != null && !bindings.getMnemonicKeyCombination().equals(mnemonicCode))) {
                        removeMnemonic();
                    }
                    containsMnemonic = false;
                }
            }
            else {
                */
/*
                ** this can happen if mnemonic parsing is
                ** disabled on a previously valid mnemonic
                *//*

                removeMnemonic();
            }

            */
/*
            ** check we have a labeled
            *//*

            if (s != null && s.length() > 0) {
                if (mnemonicIndex >= 0 && containsMnemonic == false) {
                    containsMnemonic = true;
                    mnemonicCode = bindings.getMnemonicKeyCombination();
                    addMnemonic();
                }
            }

            if (containsMnemonic == true) {
                s = bindings.getText();
                if (mnemonic_underscore == null) {
                    mnemonic_underscore = new Line();
                    mnemonic_underscore.setStartX(0.0f);
                    mnemonic_underscore.setStartY(0.0f);
                    mnemonic_underscore.setEndY(0.0f);
                    mnemonic_underscore.getStyleClass().clear();
                    mnemonic_underscore.getStyleClass().setAll("mnemonic-underline");
                }
                if (!getChildren().contains(mnemonic_underscore)) {
                    getChildren().add(mnemonic_underscore);
                }
            } else {
                */
/*
                ** we don't need a mnemonic....
                *//*

                if (getSkinnable().isMnemonicParsing() == true && com.sun.javafx.PlatformUtil.isMac() && bindings != null) {
                    s = bindings.getText();
                }
                else {
                    s = labeled.getText();
                }
                if (mnemonic_underscore != null) {
                    if (getChildren().contains(mnemonic_underscore)) {
                        Platform.runLater(() -> {
                            getChildren().remove(mnemonic_underscore);
                            mnemonic_underscore = null;
                        });
                    }
                }
            }

            int len = s != null ? s.length() : 0;
            boolean multiline = false;

            if (s != null && len > 0) {
                int i = s.indexOf('\n');
                if (i > -1 && i < len - 1) {
                    // Multiline text with embedded newlines - not
                    // taking into account a potential trailing newline.
                    multiline = true;
                }
            }

            String result;
            boolean horizontalPosition =
                    (labeled.getContentDisplay() == ContentDisplay.LEFT ||
                            labeled.getContentDisplay() == ContentDisplay.RIGHT);

            double availableWidth = labeled.getWidth() - snappedLeftInset() - leftLabelPadding() -
                    snappedRightInset() - rightLabelPadding();
            availableWidth = Math.max(availableWidth, 0);

            if (w == -1) {
                w = availableWidth;
            }
            double minW = Math.min(computeMinLabeledPartWidth(-1, snappedTopInset() , snappedRightInset(), snappedBottomInset(), snappedLeftInset()), availableWidth);
            if (horizontalPosition && !isIgnoreGraphic()) {
                double graphicW = (labeled.getGraphic().getLayoutBounds().getWidth() + labeled.getGraphicTextGap());
                w -= graphicW;
                minW -= graphicW;
            }
            wrapWidth = Math.max(minW, w);

            boolean verticalPosition =
                    (labeled.getContentDisplay() == ContentDisplay.TOP ||
                            labeled.getContentDisplay() == ContentDisplay.BOTTOM);

            double availableHeight = labeled.getHeight() - snappedTopInset() - topLabelPadding() -
                    snappedBottomInset() - bottomLabelPadding();
            availableHeight = Math.max(availableHeight, 0);

            if (h == -1) {
                h = availableHeight;
            }
            double minH = Math.min(computeMinLabeledPartHeight(wrapWidth, snappedTopInset() , snappedRightInset(), snappedBottomInset(), snappedLeftInset()), availableHeight);
            if (verticalPosition && labeled.getGraphic() != null) {
                double graphicH = labeled.getGraphic().getLayoutBounds().getHeight() + labeled.getGraphicTextGap();
                h -= graphicH;
                minH -= graphicH;
            }
            wrapHeight = Math.max(minH, h);

            updateWrappingWidth();

            Font font = text.getFont();
            OverrunStyle truncationStyle = labeled.getTextOverrun();
            String ellipsisString = labeled.getEllipsisString();

            if (labeled.isWrapText()) {
                result = Utils.computeClippedWrappedText(font, s, wrapWidth, wrapHeight, truncationStyle, ellipsisString, text.getBoundsType());
            } else if (multiline) {
                StringBuilder sb = new StringBuilder();

                String[] splits = s.split("\n");
                for (int i = 0; i < splits.length; i++) {
                    sb.append(Utils.computeClippedText(font, splits[i], wrapWidth, truncationStyle, ellipsisString));
                    if (i < splits.length - 1) {
                        sb.append('\n');
                    }
                }

                // TODO: Consider what to do in the case where vertical space is
                // limited and the last visible line isn't already truncated
                // with a trailing ellipsis. What if the style calls for leading
                // or center ellipses? We could possibly add an additional
                // trailing ellipsis to the last visible line, like this:

                // +--------------------------------+
                // |  This is some long text with multiple lines\n
                // |  where more than one exceed the|width\n
                // |  and wrapText is false, and all|lines\n
                // +--don't fit.--------------------+
                //
                // +--------------------------------+
                // |  This is some...multiple lines |
                // |  where more t...ceed the width |
                // |  and wrapText...d all lines... |
                // +--------------------------------+

                result = sb.toString();
            } else {
                result = Utils.computeClippedText(font, s, wrapWidth, truncationStyle, ellipsisString);
            }

            if (result != null && result.endsWith("\n")) {
                // Strip ending newline so we don't display another row.
                result = result.substring(0, result.length() - 1);
            }
*/
            wrapWidth = w;
            wrapHeight = h;

            text.setText(result);
            updateWrappingWidth();
            invalidText = false;
        }
    }

/*
    private void addMnemonic() {
        if (labeledNode != null) {
            mnemonicScene = labeledNode.getScene();
            if (mnemonicScene != null) {
                mnemonicScene.addMnemonic(new Mnemonic(labeledNode, mnemonicCode));
            }
        }
    }


    private void removeMnemonic() {
        if (mnemonicScene != null && labeledNode != null) {
            mnemonicScene.removeMnemonic(new Mnemonic(labeledNode, mnemonicCode));
            mnemonicScene = null;
        }
    }
*/

    /**
     * Updates the wrapping width of the text node. Although changing the font
     * does affect the metrics used for text layout, this method does not
     * call requestLayout or invalidate the text, since it may be called
     * from the constructor and such work would be duplicative and wasted.
     */
    private void updateWrappingWidth() {
        final Labeled labeled = getSkinnable();
        //text.setWrappingWidth(0d);
        if (labeled.isWrapText() /* WebFX addition: */ && wrapWidth < noWrappingTextWidth) { // we don't set the wrapping width if not necessary due to lack of double precision in HTML (rounding to inferior pixel can cause an unwanted text wrap)
            // Note that the wrapping width needs to be set to zero before
            // getting the text's real preferred width.
            double w = wrapWidth; //Math.min(text.prefWidth(-1), wrapWidth);
            text.setWrappingWidth(w);
        } else
            text.setWrappingWidth(0);
        updateLineClamp(); // WebFX addition
    }

    /**
     * Updates the children managed by LabeledSkinBase, which can be the Labeled
     * graphic and/or a Text node. Only those nodes which actually must
     * be used are used. For example, with a ContentDisplay of
     * GRAPHIC_ONLY the text node is not added, and with a ContentDisplay
     * of TEXT_ONLY, the graphic is not added.
     */
    protected void updateChildren() {
        final Labeled labeled = getSkinnable();
        // Only in some situations do we want to have the graphicPropertyChangedListener
        // installed. Since updateChildren() is not called much, we'll just remove it always
        // and reinstall it later if it is necessary to do so.
        if (graphic != null) {
            //graphic.layoutBoundsProperty().removeListener(graphicPropertyChangedListener);
        }
        // Now update the graphic (since it may have changed)
        graphic = labeled.getGraphic();

        // RT-19851 Only setMouseTransparent(true) for an ImageView.  This allows the button
        // to be picked regardless of the changing images on top of it.
        if (graphic instanceof ImageView) {
            graphic.setMouseTransparent(true);
        }

        // Now update the children (and add the graphicPropertyChangedListener as necessary)
        if (isIgnoreGraphic()) {
            if (labeled.getContentDisplay() == GRAPHIC_ONLY) {
                getChildren().clear();
            } else {
                getChildren().setAll(noWrappingText, text);
            }
        } else {
            //graphic.layoutBoundsProperty().addListener(graphicPropertyChangedListener);
            if (isIgnoreText()) {
                getChildren().setAll(graphic);
            } else {
                getChildren().setAll(graphic, noWrappingText, text);
            }

            // RT-37420
            //graphic.impl_processCSS(false);

        }

    }

    /**
     * Gets whether for various computations we can ignore the presence of the graphic
     * (or lack thereof).
     * @return
     */
    protected boolean isIgnoreGraphic() {
        return (graphic == null ||
                !graphic.isManaged() ||
                getSkinnable().getContentDisplay() == TEXT_ONLY);
    }

    /**
     * Gets whether for various computations we can ignore the presence of the text.
     * @return
     */
    protected boolean isIgnoreText() {
        final Labeled labeled = getSkinnable();
        final String txt = labeled.getText();
        return (txt == null ||
                txt.equals("") ||
                labeled.getContentDisplay() == GRAPHIC_ONLY);
    }

    /***************************************************************************
     *                                                                         *
     * Skin Layout                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Compute and return the minimum width of this Labeled. The minimum width is
     * the smaller of the width of "..." and the width with the actual text.
     * In this way, if the text width itself is smaller than the ellipsis then
     * we should use that as the min width, otherwise the ellipsis needs to be the
     * min width.
     * <p>
     * We use the same calculation here regardless of whether we are talking
     * about a single or multiline labeled. So a multiline labeled may find that
     * the width of the "..." is as small as it will ever get.
     */
    @Override protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computeMinLabeledPartWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    private double computeMinLabeledPartWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        // First compute the minTextWidth by checking the width of the string
        // made by the ellipsis "...", and then by checking the width of the
        // string made up by labeled.text. We want the smaller of the two.
        final Labeled labeled = getSkinnable();
        final ContentDisplay contentDisplay = labeled.getContentDisplay();
        final double gap = labeled.getGraphicTextGap();
        double minTextWidth = 0;

        final Font font = text.getFont();
        OverrunStyle truncationStyle = labeled.getTextOverrun();
        String ellipsisString = labeled.getEllipsisString();
        final String string = labeled.getText();
        final boolean emptyText = string == null || string.isEmpty();

        if (!emptyText) {
            // We only want to recompute the full text width if the font or text changed
            if (truncationStyle == CLIP) {
                if (textWidth == Double.NEGATIVE_INFINITY) {
                    // Show at minimum the first character
                    textWidth = computeTextWidth(font, string.substring(0, 1), 0);
                }
                minTextWidth = textWidth;
            } else {
                if (textWidth == Double.NEGATIVE_INFINITY) {
                    textWidth = computeTextWidth(font, string, 0);
                }
                // We only want to recompute the ellipsis width if the font has changed
                if (ellipsisWidth == Double.NEGATIVE_INFINITY) {
                    ellipsisWidth = computeTextWidth(font, ellipsisString, 0);
                }
                minTextWidth = Math.min(textWidth, ellipsisWidth);
            }
        }

        // Now inspect the graphic and the hpos to determine the the minWidth
        final Node graphic = labeled.getGraphic();
        double width;
        if (isIgnoreGraphic()) {
            width = minTextWidth;
        } else if (isIgnoreText()) {
            width = graphic.minWidth(-1);
        } else if (contentDisplay == LEFT || contentDisplay == RIGHT){
            width = (minTextWidth + graphic.minWidth(-1) + gap);
        } else {
            width = Math.max(minTextWidth, graphic.minWidth(-1));
        }

        return width + leftInset + leftLabelPadding() +
                rightInset + rightLabelPadding();
    }

    private double computeTextWidth(Font font, String text, double wrappingWidth) { // Note: always called with wrappingWidth = 0 for some reason
        //return Utils.computeTextWidth(font, text, wrappingWidth); // Not supported by WebFX
        // Alternative WebFX code:
        if (Strings.isEmpty(text))
            return 0;
        /*if (wrappingWidth <= 0) // Commented because it doesn't include the extra space around the text like in the html node
            return WebFxKitLauncher.measureText(text, font).getWidth();*/
        return measureText(font, text, wrappingWidth, true);
    }

    private double computeTextHeight(Font font, String text, double wrappingWidth, double lineSpacing, TextBoundsType boundsType) {
        //return Utils.computeTextHeight(font, text, wrappingWidth, lineSpacing, boundsType); // Not supported by WebFX
        // Alternative WebFX code:
        /*if (wrappingWidth <= 0 || Strings.isEmpty(text)) // Commented because it doesn't include the extra space around the text like in the html node
            return WebFxKitLauncher.measureText(text, font).getHeight();*/
        return measureText(font, text, wrappingWidth, false); // Note: this actually applies the wrappingWidth to the text
        // TODO: check if there are cases where wrappingWidth = 0 is not applied to the text
    }

    private double measureText(Font font, String text, double wrappingWidth, boolean width) { // WebFX code
        Text textToMeasure;
        // Using noWrappingText (and not this.text) in case of computation with no wrappingWidth (which happens each
        // time JavaFX computes the label min and pref widths). Because if we were using this.text, this constant
        // changing of wrappingWidth (between 0 and the actual value set by the application) would create an infinite
        // loop in the layout pass (not blocking loop but very time-consuming on each animation frame)
        double oldWrappingWidth = -1;
        if (wrappingWidth <= 0) {
            textToMeasure = noWrappingText;
            textToMeasure.setFont(font);
            //textToMeasure.setTextAlignment(getSkinnable().getTextAlignment());
            // Reusing also noWrappingText if the passed wrapping width is greater (=> for sure the text will stay on 1 line), and text & font identical
        } else if (noWrappingTextWidth > 0 && wrappingWidth >= noWrappingTextWidth && Objects.equals(noWrappingText.getText(), text) && Objects.equals(noWrappingText.getFont(), font)) {
            textToMeasure = noWrappingText;
        } else { // Otherwise using this.text to measure and apply wrapping width and text to it (should be final values to apply for HTML mapping)
            textToMeasure = this.text;
            //textToMeasure.setFont(font); // already bound in LabeledText
            //textToMeasure.setLineSpacing(lineSpacing) // already bound in LabeledText
            // We change the wrapping width for measure purpose only, and we will restore its original value after
            oldWrappingWidth = textToMeasure.getWrappingWidth();
            // It's also important to position the property webfx-measure-only to true, which will be recognized by
            // HtmlTextPeer as a request to skip the notification of the text size change. Such notification is indeed
            // unnecessary because we will restore the original wrapping width after measurement. Moreover, such
            // notification may cause a layout request on the scene branch, causing the parent to measure the text again
            // in the next layout pass, causing an infinite layout loop.
            textToMeasure.getProperties().put("webfx-measure-only", true);
            // Now we can safely change the wrapping width, just for the time of the measurement.
            textToMeasure.setWrappingWidth(wrappingWidth);
        }
        FXProperties.setIfNotEquals(textToMeasure.textProperty(), text);
        // Measuring the text width or height. Note: prefHeight(-1) alone may cause a wrong value at initialization (use Podcasts page to test)
        double measure = width ? textToMeasure.prefWidth(-1) : textToMeasure.prefHeight(wrappingWidth > 0 ? wrappingWidth : -1);
        if (width && wrappingWidth == 0) // Memorizing the width for the noWrappingText when it was measured
            noWrappingTextWidth = measure;
        if (oldWrappingWidth >= 0) { // Restoring the wrapping width after measurement if necessary
            textToMeasure.setWrappingWidth(oldWrappingWidth);
            textToMeasure.getProperties().remove("webfx-measure-only");
        }
        return measure;
    }

    @Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computeMinLabeledPartHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    private double computeMinLabeledPartHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final Labeled labeled = getSkinnable();
        final Font font = text.getFont();

        String str = labeled.getText();
        /*if (str != null && str.length() > 0) { // Commented in WebFX because changing the text was introducing an infinite loop back between layout and html mapping (text change => size changed callback => layout)
            int newlineIndex = str.indexOf('\n');
            if (newlineIndex >= 0) {
                str = str.substring(0, newlineIndex);
            }
        }*/

        // TODO figure out how to cache this effectively.
        // Base minimum height on one line (ignoring wrapping here).
        double s = labeled.getLineSpacing();
        final double textHeight = computeTextHeight(font, str, 0, s, text.getBoundsType());

        double h = textHeight;

        // Now we want to add on the graphic if necessary!
        if (!isIgnoreGraphic()) {
            final Node graphic = labeled.getGraphic();
            if (labeled.getContentDisplay() == TOP
                    || labeled.getContentDisplay() == BOTTOM) {
                h = graphic.minHeight(width) + labeled.getGraphicTextGap() + textHeight;
            } else {
                h = Math.max(textHeight, graphic.minHeight(width));
            }
        }

        return topInset + h + bottomInset + topLabelPadding() - bottomLabelPadding();
    }

    @Override protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        // Get the preferred width of the text
        final Labeled labeled = getSkinnable();
        final Font font = text.getFont();
        final String string = labeled.getText();
        boolean emptyText = string == null || string.isEmpty();
        double widthPadding = leftInset + leftLabelPadding() +
                rightInset + rightLabelPadding();

        double textWidth = emptyText ? 0 : computeTextWidth(font, string, 0);

        // Fix for RT-39889
        double graphicWidth = graphic == null ? 0.0 :
                Utils.boundedSize(graphic.prefWidth(-1), graphic.minWidth(-1), graphic.maxWidth(-1));

        // Now add on the graphic, gap, and padding as appropriate
        //final Node graphic = labeled.getGraphic();
        if (isIgnoreGraphic()) {
            return textWidth + widthPadding;
        } else if (isIgnoreText()) {
            return graphicWidth + widthPadding;
        } else if (labeled.getContentDisplay() == LEFT
                || labeled.getContentDisplay() == RIGHT) {
            return textWidth + labeled.getGraphicTextGap() + graphicWidth + widthPadding;
        } else {
            return Math.max(textWidth, graphicWidth) + widthPadding;
        }
    }

    @Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final Labeled labeled = getSkinnable();
        final Font font = text.getFont();
        final ContentDisplay contentDisplay = labeled.getContentDisplay();
        final double gap = labeled.getGraphicTextGap();
        width -= leftInset + leftLabelPadding() +
                rightInset + rightLabelPadding();

        String str = labeled.getText();
        if (str != null && str.endsWith("\n")) {
            // Strip ending newline so we don't count another row.
            str = str.substring(0, str.length() - 1);
        }

        double textWidth = width;
        if (!isIgnoreGraphic() &&
                (contentDisplay == LEFT || contentDisplay == RIGHT)) {
            textWidth -= (graphic.prefWidth(-1) + gap);
        }

        // TODO figure out how to cache this effectively.
        final double textHeight = computeTextHeight(font, str,
                labeled.isWrapText() ? textWidth : 0,
                labeled.getLineSpacing(), text.getBoundsType());

        // Now we want to add on the graphic if necessary!
        double h = textHeight;
        if (!isIgnoreGraphic()) {
            final Node graphic = labeled.getGraphic();
            if (contentDisplay == TOP || contentDisplay == BOTTOM) {
                h = graphic.prefHeight(width) + gap + textHeight;
            } else {
                h = Math.max(textHeight, graphic.prefHeight(width));
            }
        }

        return topInset + h + bottomInset + topLabelPadding() + bottomLabelPadding();
    }

    @Override protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefWidth(height);
    }

    @Override protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefHeight(width);
    }

    @Override public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        double textBaselineOffset = text.getBaselineOffset();
        double h = textBaselineOffset;
        final Labeled labeled = getSkinnable();
        final Node g = labeled.getGraphic();
        if (!isIgnoreGraphic()) {
            ContentDisplay contentDisplay = labeled.getContentDisplay();
            if (contentDisplay == TOP) {
                h = g.prefHeight(-1) + labeled.getGraphicTextGap() + textBaselineOffset;
            } else if (contentDisplay == LEFT || contentDisplay == RIGHT) {
                h = textBaselineOffset + (g.prefHeight(-1) - text.prefHeight(-1)) / 2;
            }
        }

        return topInset + topLabelPadding() + h;
    }

/*
    public TextBinding bindings;
    Line mnemonic_underscore;

    private boolean containsMnemonic = false;
    private Scene mnemonicScene = null;
    private KeyCombination mnemonicCode;
    // needs to be an object, as MenuItem isn't a node
    private Node labeledNode = null;
*/

    /**
     * The Layout algorithm works like this:
     *
     *  - Get the labeled w/h, graphic w/h, text w/h
     *  - Compute content w/h based on graphicVPos, graphicHPos,
     *    graphicTextGap, and graphic w/h and text w/h
     *  - (Note that the text content has been pre-truncated where
     *    necessary)
     *  - compute content x/y based on content w/h and labeled w/h
     *    and the labeled's hpos and vpos
     *  - position the graphic and text
     */
    @Override protected void layoutChildren(final double x, final double y,
                                            final double w, final double h) {
        layoutLabelInArea(x, y, w, h);
    }

    /**
     * Performs the actual layout of the label content within the area given.
     * This method is called by subclasses that override layoutChildren().
     *
     * @param x The x position of the label part of the control, inside padding
     *
     * @param y The y position of the label part of the control, inside padding
     *
     * @param w The width of the label part of the control, not including padding
     *
     * @param h The height of the label part of the control, not including padding
     */
    protected void layoutLabelInArea(double x, double y, double w, double h) {
        layoutLabelInArea(x, y, w, h, null);
    }

    /**
     * Performs the actual layout of the label content within the area given.
     * This method is called by subclasses that override layoutChildren().
     *
     * @param x The x position of the label part of the control, inside padding
     *
     * @param y The y position of the label part of the control, inside padding
     *
     * @param w The width of the label part of the control, not including padding
     *
     * @param h The height of the label part of the control, not including padding
     *
     * @param alignment The alignment of the label part of the control within the given area. If null, then the control's alignment will be used.
     */
    protected void layoutLabelInArea(double x, double y, double w, double h, Pos alignment) {
        // References to essential labeled state
        final Labeled labeled = getSkinnable();
        final ContentDisplay contentDisplay = labeled.getContentDisplay();

        if (alignment == null) {
            alignment = labeled.getAlignment();
        }

        final HPos hpos = alignment == null ? HPos.LEFT   : alignment.getHpos();
        final VPos vpos = alignment == null ? VPos.CENTER : alignment.getVpos();

        // Figure out whether we should ignore the Graphic, and/or
        // ignore the Text
        final boolean ignoreGraphic = isIgnoreGraphic();
        final boolean ignoreText = isIgnoreText();

        x += leftLabelPadding();
        y += topLabelPadding();
        w -= leftLabelPadding() + rightLabelPadding();
        h -= topLabelPadding() + bottomLabelPadding();

        // Compute some standard useful numbers for the graphic, text, and gap
        double graphicWidth;
        double graphicHeight;
        double textWidth;
        double textHeight;

        if (ignoreGraphic) {
            graphicWidth = graphicHeight = 0;
        } else if (ignoreText) {
            if (graphic.isResizable()) {
                Orientation contentBias = graphic.getContentBias();
                if (contentBias == Orientation.HORIZONTAL) {
                    graphicWidth  = Utils.boundedSize(w, graphic.minWidth(-1), graphic.maxWidth(-1));
                    graphicHeight = Utils.boundedSize(h, graphic.minHeight(graphicWidth), graphic.maxHeight(graphicWidth));
                } else if (contentBias == Orientation.VERTICAL) {
                    graphicHeight = Utils.boundedSize(h, graphic.minHeight(-1), graphic.maxHeight(-1));
                    graphicWidth  = Utils.boundedSize(w, graphic.minWidth(graphicHeight), graphic.maxWidth(graphicHeight));
                } else {
                    graphicWidth  = Utils.boundedSize(w, graphic.minWidth(-1), graphic.maxWidth(-1));
                    graphicHeight = Utils.boundedSize(h, graphic.minHeight(-1), graphic.maxHeight(-1));
                }
                graphic.resize(graphicWidth, graphicHeight);
            } else {
                graphicWidth = graphic.getLayoutBounds().getWidth();
                graphicHeight = graphic.getLayoutBounds().getHeight();
            }
        } else {
            graphic.autosize(); // We have to do this before getting metrics
            graphicWidth = graphic.getLayoutBounds().getWidth();
            graphicHeight = graphic.getLayoutBounds().getHeight();
        }

        if (ignoreText) {
            textWidth  = textHeight = 0;
            text.setText("");
        } else {
            updateDisplayedText(w, h); // Have to do this just in case it needs to be recomputed
            textWidth  = snapSize(Math.min(text.getLayoutBounds().getWidth(),  wrapWidth));
            textHeight = snapSize(Math.min(text.getLayoutBounds().getHeight(), wrapHeight));
        }

        final double gap = (ignoreText || ignoreGraphic) ? 0 : labeled.getGraphicTextGap();

        // Figure out the contentWidth and contentHeight. This is the width
        // and height of the Labeled and Graphic together, not the available
        // content area (which would be a different calculation).
        double contentWidth = Math.max(graphicWidth, textWidth);
        double contentHeight = Math.max(graphicHeight, textHeight);
        if (contentDisplay == TOP || contentDisplay == BOTTOM) {
            contentHeight = graphicHeight + gap + textHeight;
        } else if (contentDisplay == LEFT || contentDisplay == RIGHT) {
            contentWidth = graphicWidth + gap + textWidth;
        }

        // Now we want to compute the x/y location to place the content at.

        // Compute the contentX position based on hpos and the space available
        double contentX;
        if (hpos == HPos.LEFT) {
            contentX = x;
        } else if (hpos == HPos.RIGHT) {
            contentX = x + (w - contentWidth);
        } else {
            // TODO Baseline may not be handled correctly
            // may have been CENTER or null, treat as center
            contentX = (x + ((w - contentWidth) / 2.0));
        }

        // Compute the contentY position based on vpos and the space available
        double contentY;
        if (vpos == VPos.TOP) {
            contentY = y;
        } else if (vpos == VPos.BOTTOM) {
            contentY = (y + (h - contentHeight));
        } else {
            // TODO Baseline may not be handled correctly
            // may have been CENTER, BASELINE, or null, treat as center
            contentY = (y + ((h - contentHeight) / 2.0));
        }

/*
        double preMnemonicWidth = 0.0;
        double mnemonicWidth = 0.0;
        double mnemonicHeight = 0.0;
        if (containsMnemonic) {
            final Font font = text.getFont();
            String preSt = bindings.getText();
            preMnemonicWidth = Utils.computeTextWidth(font, preSt.substring(0,bindings.getMnemonicIndex()) , 0);
            mnemonicWidth = Utils.computeTextWidth(font, preSt.substring(bindings.getMnemonicIndex(),bindings.getMnemonicIndex()+1) , 0);
            mnemonicHeight = Utils.computeTextHeight(font, "_", 0, text.getBoundsType());
        }
*/


        // Now to position the graphic and text. At this point I know the
        // contentX and contentY locations (including the padding and whatnot
        // that was defined on the Labeled). I also know the content width and
        // height. So now I just need to lay out the graphic and text within
        // that content x/y/w/h area.
        if ((!ignoreGraphic || !ignoreText) && !text.isManaged()) {
            text.setManaged(true);
        }

        if (ignoreGraphic && ignoreText) {
            // There might be a text node as a child, or a graphic node as
            // a child. However we don't have to do anything for the graphic
            // node because the only way it can be a child and still have
            // ignoreGraphic true is if it is unmanaged. Text however might
            // be a child but still not matter, in which case we will just
            // stop managing it (although really I wish it just wasn't here
            // all all in that case)
            if (text.isManaged()) {
                text.setManaged(false);
            }
            text.relocate(snapPosition(contentX), snapPosition(contentY));
        } else if (ignoreGraphic) {
            // Since I only have to position the text, it goes at the
            // contentX/contentY location. Note that positionNode will
            // adjust the text based on the text's minX/minY so no need to
            // worry about that here
            text.relocate(snapPosition(contentX), snapPosition(contentY));
/*
            if (containsMnemonic) {
                mnemonic_underscore.setEndX(mnemonicWidth-2.0);
                mnemonic_underscore.relocate(contentX+preMnemonicWidth, contentY+mnemonicHeight-1);
            }
*/
        } else if (ignoreText) {
            // there isn't text to display, so we need to position it
            // such that it doesn't affect the content area (although when
            // there is a graphic, the text isn't even in the scene)
            text.relocate(snapPosition(contentX), snapPosition(contentY));
            graphic.relocate(snapPosition(contentX), snapPosition(contentY));
/*
            if (containsMnemonic) {
                mnemonic_underscore.setEndX(mnemonicWidth);
                mnemonic_underscore.setStrokeWidth(mnemonicHeight/10.0);
                mnemonic_underscore.relocate(contentX+preMnemonicWidth, contentY+mnemonicHeight-1);
            }
*/
        } else {
            // There is both text and a graphic, so I need to position them
            // relative to each other
            double graphicX = 0;
            double graphicY = 0;
            double textX = 0;
            double textY = 0;

            if (contentDisplay == TOP) {
                graphicX = contentX + ((contentWidth - graphicWidth) / 2.0);
                textX = contentX + ((contentWidth - textWidth) / 2.0);
                // The graphic is above the text, so it is positioned at
                // graphicY and the text below it.
                graphicY = contentY;
                textY = graphicY + graphicHeight + gap;
            } else if (contentDisplay == RIGHT) {
                // The graphic is to the right of the text
                textX = contentX;
                graphicX = textX + textWidth + gap;
                graphicY = contentY + ((contentHeight - graphicHeight) / 2.0);
                textY = contentY + ((contentHeight - textHeight) / 2.0);
            } else if (contentDisplay == BOTTOM) {
                graphicX = contentX + ((contentWidth - graphicWidth) / 2.0);
                textX = contentX + ((contentWidth - textWidth) / 2.0);
                // The graphic is below the text
                textY = contentY;
                graphicY = textY + textHeight + gap;
            } else if (contentDisplay == LEFT) {
                // The graphic is to the left of the text, so the graphicX is
                // simply the contentX and the textX is to the right of it.
                graphicX = contentX;
                textX = graphicX + graphicWidth + gap;
                graphicY = contentY + ((contentHeight - graphicHeight) / 2.0);
                textY = contentY + ((contentHeight - textHeight) / 2.0);
            } else if (contentDisplay == CENTER) {
                graphicX = contentX + ((contentWidth - graphicWidth) / 2.0);
                textX = contentX + ((contentWidth - textWidth) / 2.0);
                graphicY = contentY + ((contentHeight - graphicHeight) / 2.0);
                textY = contentY + ((contentHeight - textHeight) / 2.0);
            }
            text.relocate(snapPosition(textX), snapPosition(textY));
/*
            if (containsMnemonic) {
                mnemonic_underscore.setEndX(mnemonicWidth);
                mnemonic_underscore.setStrokeWidth(mnemonicHeight/10.0);
                mnemonic_underscore.relocate(snapPosition(textX+preMnemonicWidth), snapPosition(textY+mnemonicHeight-1));
            }
*/
            graphic.relocate(snapPosition(graphicX), snapPosition(graphicY));
        }

        /**
         * check if the label text overflows it's bounds.
         * If there's an overflow, and no text clip then
         * we'll clip it.
         * If there is no overflow, and the label text has a
         * clip, then remove it.
         */
        if ((text != null) && false && // Disabled in WebFX for now as wrapWidth & wrapHeight computation doesn't work well TODO: fix wrapWidth & wrapHeight computation
                ((text.getLayoutBounds().getHeight() > wrapHeight) ||
                        (text.getLayoutBounds().getWidth() > wrapWidth))) {

            if (textClip == null) {
                textClip = new Rectangle();
            }

            //if (labeled.getEffectiveNodeOrientation() == NodeOrientation.LEFT_TO_RIGHT) {
            textClip.setX(text.getLayoutBounds().getMinX());
/*
            } else {
                textClip.setX(text.getLayoutBounds().getMaxX() - wrapWidth);
            }
*/
            textClip.setY(text.getLayoutBounds().getMinY());
            textClip.setWidth(wrapWidth);
            textClip.setHeight(wrapHeight);
            if (text.getClip() == null) {
                text.setClip(textClip);
            }
        }
        else {
            /**
             * content fits inside bounds, no need
             * for a clip
             */
            if (text.getClip() != null) {
                text.setClip(null);
            }
        }
    }
/*
    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TEXT: {
                Labeled labeled = getSkinnable();
                String accText = labeled.getAccessibleText();
                if (accText != null && !accText.isEmpty()) return accText;

                *//* Use the text in the binding if available to handle mnemonics *//*
                if (bindings != null) {
                    String text = bindings.getText();
                    if (text != null && !text.isEmpty()) return text;
                }
                *//* Avoid the content in text.getText() as it can contain ellipses
     * for clipping
     *//*
                if (labeled != null) {
                    String text = labeled.getText();
                    if (text != null && !text.isEmpty()) return text;
                }
                *//* Use the graphic as last resource. Note that this implementation
     * does not attempt to combine the label and graphics if both
     * are being displayed
     *//*
                if (graphic != null) {
                    Object result = graphic.queryAccessibleAttribute(AccessibleAttribute.TEXT);
                    if (result != null) return result;
                }
                return null;
            }
            case MNEMONIC: {
                if (bindings != null) {
                    return bindings.getMnemonic();
                }
                return null;
            }
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }*/

    // WebFX addition for ellipsis display (the label must have the "ellipsis" style class to enable this feature).
    // This method set the line clamp property, which indicates how many lines of text should be displayed before
    // truncating with an ellipsis. This WebFX property is then managed by the peer (see HtmlTextPeer)
    private void updateLineClamp() {
        int lineClamp = 0;
        C skinnable = getSkinnable();
        if (text.getWrappingWidth() > 0 && skinnable.getStyleClass().contains("ellipsis")) {
            double lineHeight = noWrappingText.prefHeight(-1);
            double height = skinnable.getHeight();
            double lineSpacing = skinnable.getLineSpacing();
            lineClamp = (int) (height / (lineHeight + lineSpacing));
            //Console.log("lineHeight: " + lineHeight + ", lineSpacing: " + lineSpacing + ", height: " + height + ", lineClamp: " + lineClamp);
        }
        text.setLineClamp(lineClamp); // HtmlTextPeer will map this into CSS
    }
}

