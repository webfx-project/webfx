package com.sun.javafx.scene.text;

import com.sun.javafx.geom.BaseBounds;

public interface TextLayout {

    /* Internal flags Flags */
    int FLAGS_LINES_VALID      = 1 << 0; /* unused */
    int FLAGS_ANALYSIS_VALID   = 1 << 1;
    int FLAGS_HAS_TABS         = 1 << 2;
    int FLAGS_HAS_BIDI         = 1 << 3;
    int FLAGS_HAS_COMPLEX      = 1 << 4;
    int FLAGS_HAS_EMBEDDED     = 1 << 5;
    int FLAGS_HAS_CJK          = 1 << 6;
    int FLAGS_WRAPPED          = 1 << 7;
    int FLAGS_RTL_BASE         = 1 << 8;
    int FLAGS_CACHED_UNDERLINE      = 1 << 9;
    int FLAGS_CACHED_STRIKETHROUGH  = 1 << 10;
    int FLAGS_LAST             = 1 << 11;

    int ANALYSIS_MASK = FLAGS_LAST - 1;

    /* Text Layout compact internal representation */
    int ALIGN_LEFT     = 1 << 18;
    int ALIGN_CENTER   = 1 << 19;
    int ALIGN_RIGHT    = 1 << 20;
    int ALIGN_JUSTIFY  = 1 << 21;

    int ALIGN_MASK = ALIGN_LEFT | ALIGN_CENTER |
            ALIGN_RIGHT | ALIGN_JUSTIFY;

    int DIRECTION_LTR          = 1 << 10;
    int DIRECTION_RTL          = 1 << 11;
    int DIRECTION_DEFAULT_LTR  = 1 << 12;
    int DIRECTION_DEFAULT_RTL  = 1 << 13;

    int DIRECTION_MASK = DIRECTION_LTR | DIRECTION_RTL |
            DIRECTION_DEFAULT_LTR |
            DIRECTION_DEFAULT_RTL;

    int BOUNDS_CENTER       = 1 << 14;
    int BOUNDS_MASK = BOUNDS_CENTER;

    int TYPE_TEXT           = 1 << 0;
    int TYPE_UNDERLINE      = 1 << 1;
    int TYPE_STRIKETHROUGH  = 1 << 2;
    int TYPE_BASELINE       = 1 << 3;
    int TYPE_TOP            = 1 << 4;
    int TYPE_BEARINGS       = 1 << 5;

    /**
     * Sets the content for the TextLayout. Supports multiple spans (rich text).
     *
     * @return returns true is the call modifies the layout internal state.
     */
    //boolean setContent(TextSpan[] spans);

    /**
     * Sets the content for the TextLayout. Shorthand for single span text
     * (no rich text).
     *
     * @return returns true is the call modifies the layout internal state.
     */
    boolean setContent(String string, Object font);

    /**
     * Sets the alignment for the TextLayout.
     *
     * @return returns true is the call modifies the layout internal state.
     */
    //boolean setAlignment(/*TextAlignment*/ int alignment);

    /**
     * Sets the wrap width for the TextLayout.
     *
     * @return returns true is the call modifies the layout internal state.
     */
    boolean setWrapWidth(float wrapWidth);

    /**
     * Sets the line spacing for the TextLayout.
     *
     * @return returns true is the call modifies the layout internal state.
     */
    boolean setLineSpacing(float spacing);

    /**
     * Sets the direction (bidi algorithm's) for the TextLayout.
     *
     * @return returns true is the call modifies the layout internal state.
     */
    //boolean setDirection(int direction);

    /**
     * Sets the bounds type for the TextLayout.
     *
     * @return returns true is the call modifies the layout internal state.
     */
    boolean setBoundsType(int type);

    /**
     * Returns the (logical) bounds of the layout
     * minX is always zero
     * minY is the ascent of the first line (negative)
     * width the width of the widest line
     * height the sum of all lines height
     *
     * Note that this width is different the wrapping width!
     *
     * @return the layout bounds
     */
    BaseBounds getBounds();

    //BaseBounds getBounds(TextSpan filter, BaseBounds bounds);

    /**
     * Returns the visual bounds of the layout using glyph bounding box
     *
     * @return the visual bounds
     */
    //BaseBounds getVisualBounds(int type);

    /**
     * Returns the lines of text layout.
     *
     * @return the text lines
     */
    //TextLine[] getLines();

    /**
     * Returns the GlyphList of text layout.
     * The runs are returned order visually (rendering order), starting
     * from the first line. 
     *
     * @return the runs
     */
    //GlyphList[] getRuns();

    /**
     * Returns the shape of the entire text layout relative to the baseline
     * of the first line.
     *
     * @param type the type of the shapes to include 
     * @return the shape
     */
/*
    Shape getShape(int type, TextSpan filter);

    HitInfo getHitInfo(float x, float y);
    PathElement[] getCaretShape(int offset, boolean isLeading,
                                       float x, float y);
    PathElement[] getRange(int start, int end, int type,
                                  float x, float y);
*/
}
