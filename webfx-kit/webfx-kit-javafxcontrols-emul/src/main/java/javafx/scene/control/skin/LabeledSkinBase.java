package javafx.scene.control.skin;

import com.sun.javafx.scene.control.LabeledText;
import com.sun.javafx.scene.control.skin.Utils;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.util.Strings;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.Objects;

import static javafx.scene.control.ContentDisplay.*;
import static javafx.scene.control.OverrunStyle.CLIP;

public abstract class LabeledSkinBase<C extends Labeled> extends SkinBase<C> {

    /* *************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    /**
     *  The Text node used to display the text. This is package only
     *  for the sake of testing!
     */
    LabeledText text;
    // WebFX addition (to remove if possible)
    final Text noWrappingText = new Text(); { noWrappingText.setVisible(false); }
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

    /*private MnemonicInfo mnemonicInfo;
    private Line mnemonic_underscore;

    private boolean containsMnemonic = false;
    private Scene mnemonicScene = null;
    private KeyCombination mnemonicCode;
    // needs to be an object, as MenuItem isn't a node
    private Node labeledNode = null;*/

    /* *************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Constructor for LabeledSkinBase. The Labeled must be specified, and cannot be null.
     * At the conclusion of the constructor call, the skin will be marked as
     * needsLayout, and will be fully configured based on the current state of
     * the labeled. Any subsequent changes to the Labeled will be handled via
     * listeners and applied appropriately.
     *
     * @param labeled The labeled that this skin should be installed onto.
     */
    public LabeledSkinBase(final C labeled) {
        super(labeled);

        // Configure the Text node with all of the attributes from the
        // Labeled which apply to it.
        text = new LabeledText(labeled);

        // WebFX addition to tell HtmlTextPeer that those text are within label and should have 130% line-height
        text.getProperties().put("webfx-labeled-text", true);
        noWrappingText.getProperties().put("webfx-labeled-text", true);

        updateChildren();

        // Labels do not block the mouse by default, unlike most other UI Controls.
        //consumeMouseEvents(false);

        // Register listeners
        /*
         * There are basically 2 things to worry about in each of these handlers
         *  1) Update the Text node
         *  2) Have the text metrics changed?
         *
         * If the metrics have changed, we need to request a layout and invalidate
         * the text so that we recompute the display text on next read.
         */
        registerChangeListener(labeled.ellipsisStringProperty(), o -> {
            textMetricsChanged();
            invalidateWidths();
            ellipsisWidth = Double.NEGATIVE_INFINITY;
        });
        registerChangeListener(labeled.widthProperty(), o -> {
            updateWrappingWidth();
            invalidText = true;
            // No requestLayout() because Control will force a layout
        });
        registerChangeListener(labeled.heightProperty(), o -> {
            invalidText = true;
            // No requestLayout() because Control will force a layout
        });
        registerChangeListener(labeled.fontProperty(), o -> {
            textMetricsChanged();
            invalidateWidths();
            ellipsisWidth = Double.NEGATIVE_INFINITY;
        });
        registerChangeListener(labeled.graphicProperty(), o -> {
            updateChildren();
            textMetricsChanged();
        });
        registerChangeListener(labeled.contentDisplayProperty(), o -> {
            updateChildren();
            textMetricsChanged();
        });
        registerChangeListener(labeled.labelPaddingProperty(), o -> textMetricsChanged());
        registerChangeListener(labeled.graphicTextGapProperty(), o -> textMetricsChanged());
        registerChangeListener(labeled.alignmentProperty(), o -> {
            // Doesn't involve text metrics because if the text is too long, then
            // it will already have fit all available width and a change to hpos
            // has no effect. Or it is too short (i.e. it all fits) and we don't
            // have to worry about truncation. So just call request layout.
            // Doesn't involve text metrics because if the text is too long, then
            // it will already have fit all available height and a change to vpos
            // has no effect. Or it is too short (i.e. it all fits) and we don't
            // have to worry about truncation. So just call request layout.
            getSkinnable().requestLayout();
        });
        /*registerChangeListener(labeled.mnemonicParsingProperty(), o -> {
            containsMnemonic = false;
            textMetricsChanged();
        });*/
        registerChangeListener(labeled.textProperty(), o -> {
            updateChildren();
            textMetricsChanged();
            invalidateWidths();
        });
        registerChangeListener(labeled.textAlignmentProperty(), o -> { /* NO-OP */ });
        registerChangeListener(labeled.textOverrunProperty(), o -> textMetricsChanged());
        registerChangeListener(labeled.wrapTextProperty(), o -> {
            updateWrappingWidth();
            textMetricsChanged();
        });
        //registerChangeListener(labeled.underlineProperty(), o -> textMetricsChanged());
        registerChangeListener(labeled.lineSpacingProperty(), o -> textMetricsChanged());
        //registerChangeListener(labeled.sceneProperty(), o -> sceneChanged());
    }



    /* *************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /*@Override
    public void dispose() {
        if (graphic != null) {
            graphic.layoutBoundsProperty().removeListener(graphicPropertyChangedListener);
            graphic = null;
        }
        super.dispose();
    }*/

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
        /*if (graphic != null) {
            graphic.layoutBoundsProperty().removeListener(graphicPropertyChangedListener);
        }*/
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
        }
    }

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

    /** {@inheritDoc} */
    @Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computeMinLabeledPartHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    /** {@inheritDoc} */
    @Override protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        // Get the preferred width of the text
        final Labeled labeled = getSkinnable();

        boolean isIgnoreText = isIgnoreText();
        double widthPadding = leftInset + rightInset;

        double txWidth;
        if (isIgnoreText) {
            txWidth = 0.0;
        } else {
            widthPadding += (leftLabelPadding() + rightLabelPadding());

            String cleanText = getCleanText();
            boolean emptyText = cleanText == null || cleanText.isEmpty();
            txWidth = emptyText ? 0.0 : /*Utils.*/computeTextWidth(text.getFont(), cleanText, 0);
        }

        double width;
        if (isIgnoreGraphic()) {
            width = txWidth;
        } else {
            // Fix for RT-39889
            double graphicWidth = graphic == null ? 0.0 :
                Utils.boundedSize(graphic.prefWidth(-1), graphic.minWidth(-1), graphic.maxWidth(-1));

            if (isIgnoreText) {
                width = graphicWidth;
            } else {
                ContentDisplay contentDisplay = labeled.getContentDisplay();
                if (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT) {
                    width = txWidth + labeled.getGraphicTextGap() + graphicWidth;
                } else {
                    width = Math.max(txWidth, graphicWidth);
                }
            }
        }

        return width + widthPadding;
    }

    /** {@inheritDoc} */
    @Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final Labeled labeled = getSkinnable();
        final Font font = text.getFont();
        final ContentDisplay contentDisplay = labeled.getContentDisplay();
        final double gap = labeled.getGraphicTextGap();
        boolean isIgnoreText = isIgnoreText();
        boolean isIgnoreGraphic = isIgnoreGraphic();

        width -= leftInset + rightInset;
        double padding = topInset + bottomInset;
        if (!isIgnoreText) {
            width -= leftLabelPadding() + rightLabelPadding();
            padding += topLabelPadding() + bottomLabelPadding();
        }

        String cleanText = getCleanText();
        if (cleanText != null && cleanText.endsWith("\n")) {
            // Strip ending newline so we don't count another row.
            cleanText = cleanText.substring(0, cleanText.length() - 1);
        }

        double txWidth = width;
        if (!isIgnoreGraphic &&
            (contentDisplay == LEFT || contentDisplay == RIGHT)) {
            txWidth -= (graphic.prefWidth(-1) + gap);
        }

        // TODO figure out how to cache this effectively.
        final double textHeight = /*Utils.*/computeTextHeight(font, cleanText,
            labeled.isWrapText() ? txWidth : 0,
            labeled.getLineSpacing(), text.getBoundsType());

        double height;
        if (isIgnoreGraphic) {
            height = textHeight;
        } else {
            // Calculate the graphic height and use based on contentDisplay value
            double graphicHeight = graphic == null ? 0.0 :
                Utils.boundedSize(graphic.prefHeight(width), graphic.minHeight(width), graphic.maxHeight(width));

            // Add the graphic, gap, and padding as appropriate
            if (isIgnoreText) {
                height = graphicHeight;
            } else if (contentDisplay == TOP || contentDisplay == BOTTOM) {
                height = graphicHeight + gap + textHeight;
            } else {
                height = Math.max(textHeight, graphicHeight);
            }
        }

        return  height + padding;
    }

    /** {@inheritDoc} */
    @Override protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefWidth(height);
    }

    /** {@inheritDoc} */
    @Override protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefHeight(width);
    }

    /** {@inheritDoc} */
    @Override public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        double textBaselineOffset = text.getBaselineOffset();
        double h = textBaselineOffset;
        final Labeled labeled = getSkinnable();
        final Node g = labeled.getGraphic();
        if (!isIgnoreGraphic()) {
            double graphicHeight = g.prefHeight(-1);
            double textHeight = text.prefHeight(-1);
            ContentDisplay contentDisplay = labeled.getContentDisplay();
            if (contentDisplay == ContentDisplay.TOP) {
                h = graphicHeight + labeled.getGraphicTextGap() + textBaselineOffset;
            } else if ((contentDisplay == ContentDisplay.LEFT || contentDisplay == RIGHT)
                       && (graphicHeight > textHeight)) {
                h = textBaselineOffset + (graphicHeight - textHeight) / 2;
            }
        }

        double offset = topInset + h;
        if (!isIgnoreText()) {
            offset += topLabelPadding();
        }
        return offset;
    }

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

        if (!ignoreText) {
            x += leftLabelPadding();
            y += topLabelPadding();
            w -= leftLabelPadding() + rightLabelPadding();
            h -= topLabelPadding() + bottomLabelPadding();
        }

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
        Point2D mnemonicPos = null;
        double mnemonicWidth = 0.0;
        double mnemonicHeight = 0.0;
        if (containsMnemonic) {
            final Font font = text.getFont();
            String preSt = mnemonicInfo.getText();
            boolean isRTL = (labeledNode.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT);
            mnemonicPos = Utils.computeMnemonicPosition(font, preSt, mnemonicInfo.getMnemonicIndex(), this.wrapWidth, labeled.getLineSpacing(), isRTL);
            mnemonicWidth = Utils.computeTextWidth(font, preSt.substring(mnemonicInfo.getMnemonicIndex(), mnemonicInfo.getMnemonicIndex() + 1), 0);
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
            text.relocate(snapPositionX(contentX), snapPositionY(contentY));
        } else if (ignoreGraphic) {
            // Since I only have to position the text, it goes at the
            // contentX/contentY location. Note that positionNode will
            // adjust the text based on the text's minX/minY so no need to
            // worry about that here
            text.relocate(snapPositionX(contentX), snapPositionY(contentY));
/*
            if (containsMnemonic && (mnemonicPos != null)) {
                mnemonic_underscore.setEndX(mnemonicWidth-2.0);
                mnemonic_underscore.relocate(snapPositionX(contentX + mnemonicPos.getX()),
                                             snapPositionY(contentY + mnemonicPos.getY()));
            }
*/
        } else if (ignoreText) {
            // there isn't text to display, so we need to position it
            // such that it doesn't affect the content area (although when
            // there is a graphic, the text isn't even in the scene)
            text.relocate(snapPositionX(contentX), snapPositionY(contentY));
            graphic.relocate(snapPositionX(contentX), snapPositionY(contentY));
/*
            if (containsMnemonic && (mnemonicPos != null)) {
                mnemonic_underscore.setEndX(mnemonicWidth);
                mnemonic_underscore.setStrokeWidth(mnemonicHeight/10.0);
                mnemonic_underscore.relocate(snapPositionX(contentX + mnemonicPos.getX()),
                                             snapPositionY(contentY + mnemonicPos.getY()));
            }
*/
        } else {
            // There is both text and a graphic, so I need to position them
            // relative to each other
            double graphicX = 0;
            double graphicY = 0;
            double textX = 0;
            double textY = 0;

            if (contentDisplay == ContentDisplay.TOP) {
                graphicX = contentX + ((contentWidth - graphicWidth) / 2.0);
                textX = contentX + ((contentWidth - textWidth) / 2.0);
                // The graphic is above the text, so it is positioned at
                // graphicY and the text below it.
                graphicY = contentY;
                textY = graphicY + graphicHeight + gap;
            } else if (contentDisplay == ContentDisplay.RIGHT) {
                // The graphic is to the right of the text
                textX = contentX;
                graphicX = textX + textWidth + gap;
                graphicY = contentY + ((contentHeight - graphicHeight) / 2.0);
                textY = contentY + ((contentHeight - textHeight) / 2.0);
            } else if (contentDisplay == ContentDisplay.BOTTOM) {
                graphicX = contentX + ((contentWidth - graphicWidth) / 2.0);
                textX = contentX + ((contentWidth - textWidth) / 2.0);
                // The graphic is below the text
                textY = contentY;
                graphicY = textY + textHeight + gap;
            } else if (contentDisplay == ContentDisplay.LEFT) {
                // The graphic is to the left of the text, so the graphicX is
                // simply the contentX and the textX is to the right of it.
                graphicX = contentX;
                textX = graphicX + graphicWidth + gap;
                graphicY = contentY + ((contentHeight - graphicHeight) / 2.0);
                textY = contentY + ((contentHeight - textHeight) / 2.0);
            } else if (contentDisplay == ContentDisplay.CENTER) {
                graphicX = contentX + ((contentWidth - graphicWidth) / 2.0);
                textX = contentX + ((contentWidth - textWidth) / 2.0);
                graphicY = contentY + ((contentHeight - graphicHeight) / 2.0);
                textY = contentY + ((contentHeight - textHeight) / 2.0);
            }
            text.relocate(snapPositionX(textX), snapPositionY(textY));
/*
            if (containsMnemonic && (mnemonicPos != null)) {
                mnemonic_underscore.setEndX(mnemonicWidth);
                mnemonic_underscore.setStrokeWidth(mnemonicHeight/10.0);
                mnemonic_underscore.relocate(snapPositionX(textX + mnemonicPos.getX()),
                                             snapPositionY(textY + mnemonicPos.getY()));
            }
*/
            graphic.relocate(snapPositionX(graphicX), snapPositionY(graphicY));
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

    /** {@inheritDoc} */
/*    @Override protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TEXT: {
                Labeled labeled = getSkinnable();
                String accText = labeled.getAccessibleText();
                if (accText != null && !accText.isEmpty()) return accText;

                *//* Use the clean text, in which mnemonic symbols have been removed. *//*
                String cleanText = getCleanText();
                if (cleanText != null && !cleanText.isEmpty()) {
                    return cleanText;
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
                if (mnemonicInfo != null) {
                    return mnemonicInfo.getMnemonic();
                }
                return null;
            }
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }*/



    /* *************************************************************************
     *                                                                         *
     * Private implementation                                                  *
     *                                                                         *
     **************************************************************************/

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

    private double computeMinLabeledPartHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final Labeled labeled = getSkinnable();
        final Font font = text.getFont();

        String cleanText = getCleanText();
        /*if (cleanText != null && cleanText.length() > 0) { // Commented in WebFX because changing the text was introducing an infinite loop back between layout and html mapping (text change => size changed callback => layout)
            int newlineIndex = cleanText.indexOf('\n');
            if (newlineIndex >= 0) {
                cleanText = cleanText.substring(0, newlineIndex);
            }
        }*/

        // TODO figure out how to cache this effectively.
        // Base minimum height on one line (ignoring wrapping here).
        double s = labeled.getLineSpacing();
        final double textHeight = computeTextHeight(font, cleanText, 0, s, text.getBoundsType());

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

        double padding = topInset + bottomInset;
        if (!isIgnoreText()) {
            padding += topLabelPadding() - bottomLabelPadding();
        }
        return h + padding;
    }

    double topLabelPadding() {
        return snapSizeY(getSkinnable().getLabelPadding().getTop());
    }

    double bottomLabelPadding() {
        return snapSizeY(getSkinnable().getLabelPadding().getBottom());
    }

    double leftLabelPadding() {
        return snapSizeX(getSkinnable().getLabelPadding().getLeft());
    }

    double rightLabelPadding() {
        return snapSizeX(getSkinnable().getLabelPadding().getRight());
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
            String cleanText = getCleanText();
            String result = cleanText;
/*            int mnemonicIndex = -1;

            if (cleanText != null && cleanText.length() > 0
                && mnemonicInfo != null
                && !com.sun.javafx.PlatformUtil.isMac()
                && getSkinnable().isMnemonicParsing()) {
                *//*
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
                mnemonicIndex = mnemonicInfo.getMnemonicIndex() ;
            }

            *//*
             ** we were previously a mnemonic
             *//*
            if (containsMnemonic) {
                *//*
                 ** are we no longer a mnemonic, or have we changed code?
                 *//*
                if (mnemonicScene != null) {
                    if (mnemonicIndex == -1 ||
                        (mnemonicInfo != null && !mnemonicInfo.getMnemonicKeyCombination().equals(mnemonicCode))) {
                        removeMnemonic();
                        containsMnemonic = false;
                    }
                }
            }
            else {
                *//*
                 ** this can happen if mnemonic parsing is
                 ** disabled on a previously valid mnemonic
                 *//*
                removeMnemonic();
            }

            *//*
             ** check we have a labeled
             *//*
            if (cleanText != null && cleanText.length() > 0
                && mnemonicIndex >= 0 && !containsMnemonic) {
                containsMnemonic = true;
                mnemonicCode = mnemonicInfo.getMnemonicKeyCombination();
                addMnemonic();
            }

            if (containsMnemonic) {
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
            } else if (mnemonic_underscore != null && getChildren().contains(mnemonic_underscore)) {
                Platform.runLater(() -> {
                    getChildren().remove(mnemonic_underscore);
                    mnemonic_underscore = null;
                });
            }

            int len = cleanText != null ? cleanText.length() : 0;
            boolean multiline = false;

            if (cleanText != null && len > 0) {
                int i = cleanText.indexOf('\n');
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

            double availableWidth = labeled.getWidth() -
                                    snappedLeftInset() - snappedRightInset();

            if (!isIgnoreText()) {
                availableWidth -= leftLabelPadding() + rightLabelPadding();
            }
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

            double availableHeight = labeled.getHeight() -
                                     snappedTopInset() - snappedBottomInset();

            if (!isIgnoreText()) {
                availableHeight -= topLabelPadding() + bottomLabelPadding();
            }
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
                result = Utils.computeClippedWrappedText(font, cleanText, wrapWidth, wrapHeight, labeled.getLineSpacing(), truncationStyle, ellipsisString, text.getBoundsType());
            } else if (multiline) {
                StringBuilder sb = new StringBuilder();

                String[] splits = cleanText.split("\n");
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
                result = Utils.computeClippedText(font, cleanText, wrapWidth, truncationStyle, ellipsisString);
            }

            if (result != null && result.endsWith("\n")) {
                // Strip ending newline so we don't display another row.
                result = result.substring(0, result.length() - 1);
            }*/
            wrapWidth = w;
            wrapHeight = h;

            text.setText(result);
            updateWrappingWidth();
            invalidText = false;
        }
    }

    /**
     * Gets the clean text, which is the source text after mnemonic symbols have been removed.
     */
    private String getCleanText() {
        Labeled labeled = getSkinnable();
        String sourceText = labeled.getText();

        /*if (sourceText != null && labeled.isMnemonicParsing()) {
            if (mnemonicInfo == null) {
                mnemonicInfo = new MnemonicInfo(sourceText);
            } else {
                mnemonicInfo.update(sourceText);
            }

            return mnemonicInfo.getText();
        }*/

        return sourceText;
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
     * WebFX additions                                                         *
     *                                                                         *
     **************************************************************************/

    protected double computeTextWidth(Font font, String text, double wrappingWidth) { // Note: always called with wrappingWidth = 0 for some reason
        //return Utils.computeTextWidth(font, text, wrappingWidth); // Not supported by WebFX
        // Alternative WebFX code:
        if (Strings.isEmpty(text))
            return 0;
        /*if (wrappingWidth <= 0) // Commented because it doesn't include the extra space around the text like in the html node
            return WebFxKitLauncher.measureText(text, font).getWidth();*/
        return measureText(font, text, wrappingWidth, true);
    }

    protected double computeTextHeight(Font font, String text, double wrappingWidth, double lineSpacing, TextBoundsType boundsType) {
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

