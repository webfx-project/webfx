package javafx.scene.canvas;

import javafx.geometry.VPos;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

/**
 * @author Bruno Salmon
 */
public interface GraphicsContext {

    /**
     * Gets the {@code Canvas} that the {@code GraphicsContext} is issuing draw
     * commands to. There is only ever one {@code Canvas} for a
     * {@code GraphicsContext}.
     *
     * @return Canvas the canvas that this {@code GraphicsContext} is issuing draw
     * commands to.
     */
    Canvas getCanvas();

    /**
     * Saves the following attributes onto a stack.
     * <ul>
     *     <li>Global Alpha</li>
     *     <li>Global Blend Operation</li>
     *     <li>Transform</li>
     *     <li>Fill Paint</li>
     *     <li>Stroke Paint</li>
     *     <li>Line Width</li>
     *     <li>Line Cap</li>
     *     <li>Line Join</li>
     *     <li>Miter Limit</li>
     *     <li>Clip</li>
     *     <li>Font</li>
     *     <li>Text Align</li>
     *     <li>Text Baseline</li>
     *     <li>Effect</li>
     *     <li>Fill Rule</li>
     * </ul>
     * This method does NOT alter the current state in any way. Also, note that
     * the current path is not saved.
     */
    void save();

    /**
     * Pops the state off of the stack, setting the following attributes to their
     * value at the time when that state was pushed onto the stack. If the stack
     * is empty then nothing is changed.
     *
     * <ul>
     *     <li>Global Alpha</li>
     *     <li>Global Blend Operation</li>
     *     <li>Transform</li>
     *     <li>Fill Paint</li>
     *     <li>Stroke Paint</li>
     *     <li>Line Width</li>
     *     <li>Line Cap</li>
     *     <li>Line Join</li>
     *     <li>Miter Limit</li>
     *     <li>Clip</li>
     *     <li>Font</li>
     *     <li>Text Align</li>
     *     <li>Text Baseline</li>
     *     <li>Effect</li>
     *     <li>Fill Rule</li>
     * </ul>
     * Note that the current path is not restored.
     */
    void restore();

    /**
     * Translates the current transform by x, y.
     * @param x value to translate along the x axis.
     * @param y value to translate along the y axis.
     */
    void translate(double x, double y);

    /**
     * Scales the current transform by x, y.
     * @param x value to scale in the x axis.
     * @param y value to scale in the y axis.
     */
    void scale(double x, double y);

    /**
     * Rotates the current transform in degrees.
     * @param degrees value in degrees to rotate the current transform.
     */
    void rotate(double degrees);

    /**
     * Concatenates the input with the current transform.
     *
     * @param mxx - the X coordinate scaling element of the 3x4 matrix
     * @param myx - the Y coordinate shearing element of the 3x4 matrix
     * @param mxy - the X coordinate shearing element of the 3x4 matrix
     * @param myy - the Y coordinate scaling element of the 3x4 matrix
     * @param mxt - the X coordinate translation element of the 3x4 matrix
     * @param myt - the Y coordinate translation element of the 3x4 matrix
     */
    void transform(double mxx, double myx,
                          double mxy, double myy,
                          double mxt, double myt);

    /**
     * Concatenates the input with the current transform. Only 2D transforms are
     * supported. The only values used are the X and Y scaling, translation, and
     * shearing components of a transform. A {@code null} value is treated as identity.
     *
     * @param xform The affine to be concatenated with the current transform or null.
     */
    default void transform(Affine xform) {
        transform(xform.getMxx(), xform.getMyx(), xform.getMxy(), xform.getMyy(), xform.getTx(), xform.getTy());
    }

    /**
     * Sets the current transform.
     * @param mxx - the X coordinate scaling element of the 3x4 matrix
     * @param myx - the Y coordinate shearing element of the 3x4 matrix
     * @param mxy - the X coordinate shearing element of the 3x4 matrix
     * @param myy - the Y coordinate scaling element of the 3x4 matrix
     * @param mxt - the X coordinate translation element of the 3x4 matrix
     * @param myt - the Y coordinate translation element of the 3x4 matrix
     */
    void setTransform(double mxx, double myx,
                             double mxy, double myy,
                             double mxt, double myt);

    /**
     * Sets the current transform. Only 2D transforms are supported. The only
     * values used are the X and Y scaling, translation, and shearing components
     * of a transform.
     *
     * @param xform The affine to be copied and used as the current transform.
     */
    default void setTransform(Affine xform) {
        setTransform(xform.getMxx(), xform.getMyx(), xform.getMxy(), xform.getMyy(), xform.getTx(), xform.getTy());
    }

    /**
     * Copies the current transform into the supplied object, creating
     * a new {@link Affine} object if it is null, and returns the object
     * containing the copy.
     *
     * @param xform A transform object that will be used to hold the result.
     * If xform is non null, then this method will copy the current transform
     * into that object. If xform is null a new transform object will be
     * constructed. In either case, the return value is a copy of the current
     * transform.
     *
     * @return A copy of the current transform.
     */
    Affine getTransform(Affine xform);

    /**
     * Returns a copy of the current transform.
     *
     * @return a copy of the transform of the current state.
     */
    default Affine getTransform() {
        return getTransform(null);
    }

    /**
     * Sets the global alpha of the current state.
     * The default value is {@code 1.0}.
     * Any valid double can be set, but only values in the range
     * {@code [0.0, 1.0]} are valid and the nearest value in that
     * range will be used for rendering.
     * The global alpha is a <a href="#comm-attr">common attribute</a>
     * used for nearly all rendering methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @param alpha the new alpha value, clamped to {@code [0.0, 1.0]}
     *              during actual use.
     */
    void setGlobalAlpha(double alpha);

    /**
     * Gets the current global alpha.
     * The default value is {@code 1.0}.
     * The global alpha is a <a href="#comm-attr">common attribute</a>
     * used for nearly all rendering methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return the current global alpha.
     */
    double getGlobalAlpha();

    /**
     * Sets the global blend mode.
     * The default value is {@link BlendMode#SRC_OVER SRC_OVER}.
     * A {@code null} value will be ignored and the current value will remain unchanged.
     * The blend mode is a <a href="#comm-attr">common attribute</a>
     * used for nearly all rendering methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @param op the {@code BlendMode} that will be set or null.
     */
    void setGlobalBlendMode(BlendMode op);

    /**
     * Gets the global blend mode.
     * The default value is {@link BlendMode#SRC_OVER SRC_OVER}.
     * The blend mode is a <a href="#comm-attr">common attribute</a>
     * used for nearly all rendering methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return the global {@code BlendMode} of the current state.
     */
    BlendMode getGlobalBlendMode();

    /**
     * Sets the current fill paint attribute.
     * The default value is {@link Color#BLACK BLACK}.
     * The fill paint is a <a href="#fill-attr">fill attribute</a>
     * used for any of the fill methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * A {@code null} value will be ignored and the current value will remain unchanged.
     *
     * @param p The {@code Paint} to be used as the fill {@code Paint} or null.
     */
    void setFill(Paint p);

    /**
     * Gets the current fill paint attribute.
     * The default value is {@link Color#BLACK BLACK}.
     * The fill paint is a <a href="#fill-attr">fill attribute</a>
     * used for any of the fill methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return p The {@code Paint} to be used as the fill {@code Paint}.
     */
    Paint getFill();

    /**
     * Sets the current stroke paint attribute.
     * The default value is {@link Color#BLACK BLACK}.
     * The stroke paint is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * A {@code null} value will be ignored and the current value will remain unchanged.
     *
     * @param p The Paint to be used as the stroke Paint or null.
     */
    void setStroke(Paint p);

    /**
     * Gets the current stroke.
     * The default value is {@link Color#BLACK BLACK}.
     * The stroke paint is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return the {@code Paint} to be used as the stroke {@code Paint}.
     */
    Paint getStroke();

    /**
     * Sets the current line width.
     * The default value is {@code 1.0}.
     * The line width is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * An infinite or non-positive value outside of the range {@code (0, +inf)}
     * will be ignored and the current value will remain unchanged.
     *
     * @param lw value in the range {0-positive infinity}, with any other value
     * being ignored and leaving the value unchanged.
     */
    void setLineWidth(double lw);

    /**
     * Gets the current line width.
     * The default value is {@code 1.0}.
     * The line width is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return value between 0 and infinity.
     */
    double getLineWidth();

    /**
     * Sets the current stroke line cap.
     * The default value is {@link StrokeLineCap#SQUARE SQUARE}.
     * The line cap is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * A {@code null} value will be ignored and the current value will remain unchanged.
     *
     * @param cap {@code StrokeLineCap} with a value of Butt, Round, or Square or null.
     */
    void setLineCap(StrokeLineCap cap);

    /**
     * Gets the current stroke line cap.
     * The default value is {@link StrokeLineCap#SQUARE SQUARE}.
     * The line cap is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return {@code StrokeLineCap} with a value of Butt, Round, or Square.
     */
    StrokeLineCap getLineCap();

    /**
     * Sets the current stroke line join.
     * The default value is {@link StrokeLineJoin#MITER}.
     * The line join is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * A {@code null} value will be ignored and the current value will remain unchanged.
     *
     * @param join {@code StrokeLineJoin} with a value of Miter, Bevel, or Round or null.
     */
    void setLineJoin(StrokeLineJoin join);

    /**
     * Gets the current stroke line join.
     * The default value is {@link StrokeLineJoin#MITER}.
     * The line join is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return {@code StrokeLineJoin} with a value of Miter, Bevel, or Round.
     */
    StrokeLineJoin getLineJoin();

    /**
     * Sets the current miter limit.
     * The default value is {@code 10.0}.
     * The miter limit is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * An infinite or non-positive value outside of the range {@code (0, +inf)}
     * will be ignored and the current value will remain unchanged.
     *
     * @param ml miter limit value between 0 and positive infinity with
     * any other value being ignored and leaving the value unchanged.
     */
    void setMiterLimit(double ml);

    /**
     * Gets the current miter limit.
     * The default value is {@code 10.0}.
     * The miter limit is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return the miter limit value in the range {@code 0.0-positive infinity}
     */
    double getMiterLimit();

    /**
     * Sets the current stroke line dash pattern to a normalized copy of
     * the argument.
     * The default value is {@code null}.
     * The line dash array is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * If the array is {@code null} or empty or contains all {@code 0} elements
     * then dashing will be disabled and the current dash array will be set
     * to {@code null}.
     * If any of the elements of the array are a negative, infinite, or NaN
     * value outside the range {@code [0, +inf)} then the entire array will
     * be ignored and the current dash array will remain unchanged.
     * If the array is an odd length then it will be treated as if it
     * were two copies of the array appended to each other.
     *
     * @param dashes the array of finite non-negative dash lengths
     * @since JavaFX 8u40
     */
    void setLineDashes(double... dashes);

    /**
     * Gets a copy of the current line dash array.
     * The default value is {@code null}.
     * The array may be normalized by the validation tests in the
     * {@link #setLineDashes(double...)} method.
     * The line dash array is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return a copy of the current line dash array.
     * @since JavaFX 8u40
     */
    double[] getLineDashes();

    /**
     * Sets the line dash offset.
     * The default value is {@code 0.0}.
     * The line dash offset is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * An infinite or NaN value outside of the range {@code (-inf, +inf)}
     * will be ignored and the current value will remain unchanged.
     *
     * @param dashOffset the line dash offset in the range {@code (-inf, +inf)}
     * @since JavaFX 8u40
     */
    void setLineDashOffset(double dashOffset);

    /**
     * Gets the current line dash offset.
     * The default value is {@code 0.0}.
     * The line dash offset is a <a href="#strk-attr">stroke attribute</a>
     * used for any of the stroke methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return the line dash offset in the range {@code (-inf, +inf)}
     * @since JavaFX 8u40
     */
    double getLineDashOffset();

    /**
     * Sets the current Font.
     * The default value is specified by {@link Font#getDefault()}.
     * The font is a <a href="#text-attr">text attribute</a>
     * used for any of the text methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * A {@code null} value will be ignored and the current value will remain unchanged.
     *
     * @param f the Font or null.
     */
    void setFont(Font f);

    /**
     * Gets the current Font.
     * The default value is specified by {@link Font#getDefault()}.
     * The font is a <a href="#text-attr">text attribute</a>
     * used for any of the text methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return the Font
     */
    Font getFont();

    /**
     * Sets the current Font Smoothing Type.
     * The default value is {@link FontSmoothingType#GRAY GRAY}.
     * The font smoothing type is a <a href="#text-attr">text attribute</a>
     * used for any of the text methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * A {@code null} value will be ignored and the current value will remain unchanged.
     * <p>
     * <b>Note</b> that the {@code FontSmoothingType} value of
     * {@link FontSmoothingType#LCD LCD} is only supported over an opaque
     * background.  {@code LCD} text will generally appear as {@code GRAY}
     * text over transparent or partially transparent pixels, and in some
     * implementations it may not be supported at all on a {@link Canvas}
     * because the required support does not exist for surfaces which contain
     * an alpha channel as all {@code Canvas} objects do.
     *
     * @param fontsmoothing the {@link FontSmoothingType} or null
     * @since JavaFX 8u40
     */
    //void setFontSmoothingType(FontSmoothingType fontsmoothing);

    /**
     * Gets the current Font Smoothing Type.
     * The default value is {@link FontSmoothingType#GRAY GRAY}.
     * The font smoothing type is a <a href="#text-attr">text attribute</a>
     * used for any of the text methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return the {@link FontSmoothingType}
     * @since JavaFX 8u40
     */
    //FontSmoothingType getFontSmoothingType();

    /**
     * Defines horizontal text alignment, relative to the text {@code x} origin.
     * The default value is {@link TextAlignment#LEFT LEFT}.
     * The text alignment is a <a href="#text-attr">text attribute</a>
     * used for any of the text methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * <p>
     * Let horizontal bounds represent the logical width of a single line of
     * text. Where each line of text has a separate horizontal bounds.
     * <p>
     * Then TextAlignment is specified as:
     * <ul>
     * <li>Left: the left edge of the horizontal bounds will be at {@code x}.
     * <li>Center: the center, halfway between left and right edge, of the
     * horizontal bounds will be at {@code x}.
     * <li>Right: the right edge of the horizontal bounds will be at {@code x}.
     * </ul>
     * <p>
     *
     * Note: Canvas does not support line wrapping, therefore the text
     * alignment Justify is identical to left aligned text.
     * <p>
     * A {@code null} value will be ignored and the current value will remain unchanged.
     *
     * @param align {@code TextAlignment} with values of Left, Center, Right or null.
     */
    void setTextAlign(TextAlignment align);

    /**
     * Gets the current {@code TextAlignment}.
     * The default value is {@link TextAlignment#LEFT LEFT}.
     * The text alignment is a <a href="#text-attr">text attribute</a>
     * used for any of the text methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return {@code TextAlignment} with values of Left, Center, Right, or
     * Justify.
     */
    TextAlignment getTextAlign();

    /**
     * Sets the current Text Baseline.
     * The default value is {@link VPos#BASELINE BASELINE}.
     * The text baseline is a <a href="#text-attr">text attribute</a>
     * used for any of the text methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * A {@code null} value will be ignored and the current value will remain unchanged.
     *
     * @param baseline {@code VPos} with values of Top, Center, Baseline, or Bottom or null.
     */
    void setTextBaseline(VPos baseline);

    /**
     * Gets the current Text Baseline.
     * The default value is {@link VPos#BASELINE BASELINE}.
     * The text baseline is a <a href="#text-attr">text attribute</a>
     * used for any of the text methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return {@code VPos} with values of Top, Center, Baseline, or Bottom
     */
    VPos getTextBaseline();

    /**
     * Fills the given string of text at position x, y
     * with the current fill paint attribute.
     * A {@code null} text value will be ignored.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>,
     * <a href="#fill-attr">fill</a>,
     * or <a href="#text-attr">text</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param text the string of text or null.
     * @param x position on the x axis.
     * @param y position on the y axis.
     */
    void fillText(String text, double x, double y);

    /**
     * Draws the given string of text at position x, y
     * with the current stroke paint attribute.
     * A {@code null} text value will be ignored.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>,
     * <a href="#strk-attr">stroke</a>,
     * or <a href="#text-attr">text</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param text the string of text or null.
     * @param x position on the x axis.
     * @param y position on the y axis.
     */
    void strokeText(String text, double x, double y);

    /**
     * Fills text and includes a maximum width of the string.
     * If the width of the text extends past max width, then it will be sized
     * to fit.
     * A {@code null} text value will be ignored.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>,
     * <a href="#fill-attr">fill</a>,
     * or <a href="#text-attr">text</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param text the string of text or null.
     * @param x position on the x axis.
     * @param y position on the y axis.
     * @param maxWidth  maximum width the text string can have.
     */
    void fillText(String text, double x, double y, double maxWidth);

    /**
     * Draws text with stroke paint and includes a maximum width of the string.
     * If the width of the text extends past max width, then it will be sized
     * to fit.
     * A {@code null} text value will be ignored.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>,
     * <a href="#strk-attr">stroke</a>,
     * or <a href="#text-attr">text</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param text the string of text or null.
     * @param x position on the x axis.
     * @param y position on the y axis.
     * @param maxWidth  maximum width the text string can have.
     */
    void strokeText(String text, double x, double y, double maxWidth);


    /**
     * Set the filling rule attribute for determining the interior of paths
     * in fill or clip operations.
     * The default value is {@code FillRule.NON_ZERO}.
     * A {@code null} value will be ignored and the current value will remain unchanged.
     * The fill rule is a <a href="#path-attr">path attribute</a>
     * used for any of the fill or clip path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @param fillRule {@code FillRule} with a value of  Even_odd or Non_zero or null.
     */
    //void setFillRule(FillRule fillRule);

    /**
     * Get the filling rule attribute for determining the interior of paths
     * in fill and clip operations.
     * The default value is {@code FillRule.NON_ZERO}.
     * The fill rule is a <a href="#path-attr">path attribute</a>
     * used for any of the fill or clip path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @return current fill rule.
     */
    //FillRule getFillRule();

    /**
     * Resets the current path to empty.
     * The default path is empty.
     * The current path is a <a href="#path-attr">path attribute</a>
     * used for any of the path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>
     * and <b>is not affected</b> by the {@link #save()} and
     * {@link #restore()} operations.
     */
    void beginPath();

    /**
     * Issues a move command for the current path to the given x,y coordinate.
     * The coordinates are transformed by the current transform as they are
     * added to the path and unaffected by subsequent changes to the transform.
     * The current path is a <a href="#path-attr">path attribute</a>
     * used for any of the path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>
     * and <b>is not affected</b> by the {@link #save()} and
     * {@link #restore()} operations.
     *
     * @param x0 the X position for the move to command.
     * @param y0 the Y position for the move to command.
     */
    void moveTo(double x0, double y0);

    /**
     * Adds segments to the current path to make a line to the given x,y
     * coordinate.
     * The coordinates are transformed by the current transform as they are
     * added to the path and unaffected by subsequent changes to the transform.
     * The current path is a <a href="#path-attr">path attribute</a>
     * used for any of the path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>
     * and <b>is not affected</b> by the {@link #save()} and
     * {@link #restore()} operations.
     *
     * @param x1 the X coordinate of the ending point of the line.
     * @param y1 the Y coordinate of the ending point of the line.
     */
    void lineTo(double x1, double y1);

    /**
     * Adds segments to the current path to make a quadratic Bezier curve.
     * The coordinates are transformed by the current transform as they are
     * added to the path and unaffected by subsequent changes to the transform.
     * The current path is a <a href="#path-attr">path attribute</a>
     * used for any of the path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>
     * and <b>is not affected</b> by the {@link #save()} and
     * {@link #restore()} operations.
     *
     * @param xc the X coordinate of the control point
     * @param yc the Y coordinate of the control point
     * @param x1 the X coordinate of the end point
     * @param y1 the Y coordinate of the end point
     */
    void quadraticCurveTo(double xc, double yc, double x1, double y1);

    /**
     * Adds segments to the current path to make a cubic Bezier curve.
     * The coordinates are transformed by the current transform as they are
     * added to the path and unaffected by subsequent changes to the transform.
     * The current path is a <a href="#path-attr">path attribute</a>
     * used for any of the path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>
     * and <b>is not affected</b> by the {@link #save()} and
     * {@link #restore()} operations.
     *
     * @param xc1 the X coordinate of first Bezier control point.
     * @param yc1 the Y coordinate of the first Bezier control point.
     * @param xc2 the X coordinate of the second Bezier control point.
     * @param yc2 the Y coordinate of the second Bezier control point.
     * @param x1  the X coordinate of the end point.
     * @param y1  the Y coordinate of the end point.
     */
    void bezierCurveTo(double xc1, double yc1, double xc2, double yc2, double x1, double y1);

    /**
     * Adds segments to the current path to make an arc.
     * The coordinates are transformed by the current transform as they are
     * added to the path and unaffected by subsequent changes to the transform.
     * The current path is a <a href="#path-attr">path attribute</a>
     * used for any of the path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>
     * and <b>is not affected</b> by the {@link #save()} and
     * {@link #restore()} operations.
     * <p>
     * If {@code p0} is the current point in the path and {@code p1} is the
     * point specified by {@code (x1, y1)} and {@code p2} is the point
     * specified by {@code (x2, y2)}, then the arc segments appended will
     * be segments along the circumference of a circle of the specified
     * radius touching and inscribed into the convex (interior) side of
     * {@code p0->p1->p2}.  The path will contain a line segment (if
     * needed) to the tangent point between that circle and {@code p0->p1}
     * followed by circular arc segments to reach the tangent point between
     * the circle and {@code p1->p2} and will end with the current point at
     * that tangent point (not at {@code p2}).
     * Note that the radius and circularity of the arc segments will be
     * measured or considered relative to the current transform, but the
     * resulting segments that are computed from those untransformed
     * points will then be transformed when they are added to the path.
     * Since all computation is done in untransformed space, but the
     * pre-existing path segments are all transformed, the ability to
     * correctly perform the computation may implicitly depend on being
     * able to inverse transform the current end of the current path back
     * into untransformed coordinates.
     * </p>
     * <p>
     * If there is no way to compute and inscribe the indicated circle
     * for any reason then the entire operation will simply append segments
     * to force a line to point {@code p1}.  Possible reasons that the
     * computation may fail include:
     * <ul>
     * <li>The current path is empty.</li>
     * <li>The segments {@code p0->p1->p2} are colinear.</li>
     * <li>the current transform is non-invertible so that the current end
     * point of the current path cannot be untransformed for computation.</li>
     * </ul>
     *
     * @param x1 the X coordinate of the first point of the arc.
     * @param y1 the Y coordinate of the first point of the arc.
     * @param x2 the X coordinate of the second point of the arc.
     * @param y2 the Y coordinate of the second point of the arc.
     * @param radius the radius of the arc in the range {0.0-positive infinity}.
     */
    void arcTo(double x1, double y1, double x2, double y2, double radius);

    /**
     * Adds path elements to the current path to make an arc that uses Euclidean
     * degrees. This Euclidean orientation sweeps from East to North, then West,
     * then South, then back to East.
     * The coordinates are transformed by the current transform as they are
     * added to the path and unaffected by subsequent changes to the transform.
     * The current path is a <a href="#path-attr">path attribute</a>
     * used for any of the path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>
     * and <b>is not affected</b> by the {@link #save()} and
     * {@link #restore()} operations.
     *
     * @param centerX the center x position of the arc.
     * @param centerY the center y position of the arc.
     * @param radiusX the x radius of the arc.
     * @param radiusY the y radius of the arc.
     * @param startAngle the starting angle of the arc in the range {@code 0-360.0}
     * @param length  the length of the baseline of the arc.
     */
    void arc(double centerX, double centerY,
                    double radiusX, double radiusY,
                    double startAngle, double length);

    /**
     * Adds path elements to the current path to make a rectangle.
     * The coordinates are transformed by the current transform as they are
     * added to the path and unaffected by subsequent changes to the transform.
     * The current path is a <a href="#path-attr">path attribute</a>
     * used for any of the path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>
     * and <b>is not affected</b> by the {@link #save()} and
     * {@link #restore()} operations.
     *
     * @param x x position of the upper left corner of the rectangle.
     * @param y y position of the upper left corner of the rectangle.
     * @param w width of the rectangle.
     * @param h height of the rectangle.
     */
    void rect(double x, double y, double w, double h);

    /**
     * Appends an SVG Path string to the current path. If there is no current
     * path the string must then start with either type of move command.
     * A {@code null} value or incorrect SVG path will be ignored.
     * The coordinates are transformed by the current transform as they are
     * added to the path and unaffected by subsequent changes to the transform.
     * The current path is a <a href="#path-attr">path attribute</a>
     * used for any of the path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>
     * and <b>is not affected</b> by the {@link #save()} and
     * {@link #restore()} operations.
     *
     * @param svgpath the SVG Path string.
     */
    void appendSVGPath(String svgpath);

    /**
     * Closes the path.
     * The current path is a <a href="#path-attr">path attribute</a>
     * used for any of the path methods as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>
     * and <b>is not affected</b> by the {@link #save()} and
     * {@link #restore()} operations.
     */
    void closePath();

    /**
     * Fills the path with the current fill paint.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>,
     * <a href="#fill-attr">fill</a>,
     * or <a href="#path-attr">path</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * Note that the path segments were transformed as they were originally
     * added to the current path so the current transform will not affect
     * those path segments again, but it may affect other attributes in
     * affect at the time of the {@code fill()} operation.
     * </p>
     */
    void fill();

    /**
     * Strokes the path with the current stroke paint.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>,
     * <a href="#strk-attr">stroke</a>,
     * or <a href="#path-attr">path</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * Note that the path segments were transformed as they were originally
     * added to the current path so the current transform will not affect
     * those path segments again, but it may affect other attributes in
     * affect at the time of the {@code stroke()} operation.
     * </p>
     */
    void stroke();

    /**
     * Intersects the current clip with the current path and applies it to
     * subsequent rendering operation as an anti-aliased mask.
     * The current clip is a <a href="#comm-attr">common attribute</a>
     * used for nearly all rendering operations as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * <p>
     * This method will itself be affected only by the
     * <a href="#path-attr">path</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * Note that the path segments were transformed as they were originally
     * added to the current path so the current transform will not affect
     * those path segments again, but it may affect other attributes in
     * affect at the time of the {@code stroke()} operation.
     * </p>
     */
    void clip();

    /**
     * Returns true if the the given x,y point is inside the path.
     *
     * @param x the X coordinate to use for the check.
     * @param y the Y coordinate to use for the check.
     * @return true if the point given is inside the path, false
     * otherwise.
     */
    boolean isPointInPath(double x, double y);

    /**
     * Clears a portion of the canvas with a transparent color value.
     * <p>
     * This method will be affected only by the current transform, clip,
     * and effect.
     * </p>
     *
     * @param x X position of the upper left corner of the rectangle.
     * @param y Y position of the upper left corner of the rectangle.
     * @param w width of the rectangle.
     * @param h height of the rectangle.
     */
    void clearRect(double x, double y, double w, double h);

    /**
     * Fills a rectangle using the current fill paint.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#fill-attr">fill</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param x the X position of the upper left corner of the rectangle.
     * @param y the Y position of the upper left corner of the rectangle.
     * @param w the width of the rectangle.
     * @param h the height of the rectangle.
     */
    void fillRect(double x, double y, double w, double h);

    /**
     * Strokes a rectangle using the current stroke paint.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#strk-attr">stroke</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param x the X position of the upper left corner of the rectangle.
     * @param y the Y position of the upper left corner of the rectangle.
     * @param w the width of the rectangle.
     * @param h the height of the rectangle.
     */
    void strokeRect(double x, double y, double w, double h);
    /**
     * Fills an oval using the current fill paint.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#fill-attr">fill</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param x the X coordinate of the upper left bound of the oval.
     * @param y the Y coordinate of the upper left bound of the oval.
     * @param w the width at the center of the oval.
     * @param h the height at the center of the oval.
     */
    default void fillOval(double x, double y, double w, double h) {
        fillArc(x, y, w, h, 0, 360, null);
    }

    /**
     * Strokes an oval using the current stroke paint.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#strk-attr">stroke</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param x the X coordinate of the upper left bound of the oval.
     * @param y the Y coordinate of the upper left bound of the oval.
     * @param w the width at the center of the oval.
     * @param h the height at the center of the oval.
     */
    default void strokeOval(double x, double y, double w, double h) {
        strokeArc(x, y, w, h, 0, 360, null);
    }

    /**
     * Fills an arc using the current fill paint. A {@code null} ArcType or
     * non positive width or height will cause the render command to be ignored.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#fill-attr">fill</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param x the X coordinate of the arc.
     * @param y the Y coordinate of the arc.
     * @param w the width of the arc.
     * @param h the height of the arc.
     * @param startAngle the starting angle of the arc in degrees.
     * @param arcExtent the angular extent of the arc in degrees.
     * @param closure closure type (Round, Chord, Open) or null.
     */
    void fillArc(double x, double y, double w, double h,
                        double startAngle, double arcExtent, ArcType closure);

    /**
     * Strokes an Arc using the current stroke paint. A {@code null} ArcType or
     * non positive width or height will cause the render command to be ignored.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#strk-attr">stroke</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param x the X coordinate of the arc.
     * @param y the Y coordinate of the arc.
     * @param w the width of the arc.
     * @param h the height of the arc.
     * @param startAngle the starting angle of the arc in degrees.
     * @param arcExtent arcExtent the angular extent of the arc in degrees.
     * @param closure closure type (Round, Chord, Open) or null
     */
    void strokeArc(double x, double y, double w, double h,
                          double startAngle, double arcExtent, ArcType closure);

    /**
     * Fills a rounded rectangle using the current fill paint.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#fill-attr">fill</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param x the X coordinate of the upper left bound of the oval.
     * @param y the Y coordinate of the upper left bound of the oval.
     * @param w the width at the center of the oval.
     * @param h the height at the center of the oval.
     * @param arcWidth the arc width of the rectangle corners.
     * @param arcHeight the arc height of the rectangle corners.
     */
    void fillRoundRect(double x, double y, double w, double h,
                              double arcWidth, double arcHeight);

    /**
     * Strokes a rounded rectangle using the current stroke paint.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#strk-attr">stroke</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param x the X coordinate of the upper left bound of the oval.
     * @param y the Y coordinate of the upper left bound of the oval.
     * @param w the width at the center of the oval.
     * @param h the height at the center of the oval.
     * @param arcWidth the arc width of the rectangle corners.
     * @param arcHeight the arc height of the rectangle corners.
     */
    void strokeRoundRect(double x, double y, double w, double h,
                                double arcWidth, double arcHeight);

    /**
     * Strokes a line using the current stroke paint.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#strk-attr">stroke</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param x1 the X coordinate of the starting point of the line.
     * @param y1 the Y coordinate of the starting point of the line.
     * @param x2 the X coordinate of the ending point of the line.
     * @param y2 the Y coordinate of the ending point of the line.
     */
    void strokeLine(double x1, double y1, double x2, double y2);

    /**
     * Fills a polygon with the given points using the currently set fill paint.
     * A {@code null} value for any of the arrays will be ignored and nothing will be drawn.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>,
     * <a href="#fill-attr">fill</a>,
     * or <a href="#path-attr">Fill Rule</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param xPoints array containing the x coordinates of the polygon's points or null.
     * @param yPoints array containing the y coordinates of the polygon's points or null.
     * @param nPoints the number of points that make the polygon.
     */
    void fillPolygon(double xPoints[], double yPoints[], int nPoints);

    /**
     * Strokes a polygon with the given points using the currently set stroke paint.
     * A {@code null} value for any of the arrays will be ignored and nothing will be drawn.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#strk-attr">stroke</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param xPoints array containing the x coordinates of the polygon's points or null.
     * @param yPoints array containing the y coordinates of the polygon's points or null.
     * @param nPoints the number of points that make the polygon.
     */
    void strokePolygon(double xPoints[], double yPoints[], int nPoints);

    /**
     * Strokes a polyline with the given points using the currently set stroke
     * paint attribute.
     * A {@code null} value for any of the arrays will be ignored and nothing will be drawn.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * or <a href="#strk-attr">stroke</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param xPoints array containing the x coordinates of the polyline's points or null.
     * @param yPoints array containing the y coordinates of the polyline's points or null.
     * @param nPoints the number of points that make the polyline.
     */
    void strokePolyline(double xPoints[], double yPoints[], int nPoints);

    /**
     * Draws an image at the given x, y position using the width
     * and height of the given image.
     * A {@code null} image value or an image still in progress will be ignored.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param img the image to be drawn or null.
     * @param x the X coordinate on the destination for the upper left of the image.
     * @param y the Y coordinate on the destination for the upper left of the image.
     */
    void drawImage(Image img, double x, double y);

    /**
     * Draws an image into the given destination rectangle of the canvas. The
     * Image is scaled to fit into the destination rectangle.
     * A {@code null} image value or an image still in progress will be ignored.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param img the image to be drawn or null.
     * @param x the X coordinate on the destination for the upper left of the image.
     * @param y the Y coordinate on the destination for the upper left of the image.
     * @param w the width of the destination rectangle.
     * @param h the height of the destination rectangle.
     */
    void drawImage(Image img, double x, double y, double w, double h);

    /**
     * Draws the specified source rectangle of the given image to the given
     * destination rectangle of the Canvas.
     * A {@code null} image value or an image still in progress will be ignored.
     * <p>
     * This method will be affected by any of the
     * <a href="#comm-attr">global common</a>
     * attributes as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     * </p>
     *
     * @param img the image to be drawn or null.
     * @param sx the source rectangle's X coordinate position.
     * @param sy the source rectangle's Y coordinate position.
     * @param sw the source rectangle's width.
     * @param sh the source rectangle's height.
     * @param dx the destination rectangle's X coordinate position.
     * @param dy the destination rectangle's Y coordinate position.
     * @param dw the destination rectangle's width.
     * @param dh the destination rectangle's height.
     */
    void drawImage(Image img,
                          double sx, double sy, double sw, double sh,
                          double dx, double dy, double dw, double dh);

    /**
     * Returns a {@link PixelWriter} object that can be used to modify
     * the pixels of the {@link Canvas} associated with this
     * {@code GraphicsContext}.
     * All coordinates in the {@code PixelWriter} methods on the returned
     * object will be in device space since they refer directly to pixels
     * and no other rendering attributes will be applied when modifying
     * pixels using this object.
     *
     * @return the {@code PixelWriter} for modifying the pixels of this
     *         {@code Canvas}
     */
    PixelWriter getPixelWriter();

    /**
     * Sets the effect to be applied after the next draw call, or null to
     * disable effects.
     * The current effect is a <a href="#comm-attr">common attribute</a>
     * used for nearly all rendering operations as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @param e the effect to use, or null to disable effects
     */
    void setEffect(Effect e);

    /**
     * Gets a copy of the effect to be applied after the next draw call.
     * A null return value means that no effect will be applied after subsequent
     * rendering calls.
     * The current effect is a <a href="#comm-attr">common attribute</a>
     * used for nearly all rendering operations as specified in the
     * <a href="#attr-ops-table">Rendering Attributes Table</a>.
     *
     * @param e an {@code Effect} object that may be used to store the
     *        copy of the current effect, if it is of a compatible type
     * @return the current effect used for all rendering calls,
     *         or null if there is no current effect
     */
    Effect getEffect(Effect e);

    /**
     * Applies the given effect to the entire bounds of the canvas and stores
     * the result back into the same canvas.
     * A {@code null} value will be ignored.
     * The effect will be applied without any other rendering attributes and
     * under an Identity coordinate transform.
     * Since the effect is applied to the entire bounds of the canvas, some
     * effects may have a confusing result, such as a Reflection effect
     * that will apply its reflection off of the bottom of the canvas even if
     * only a portion of the canvas has been rendered to and will not be
     * visible unless a negative offset is used to bring the reflection back
     * into view.
     *
     * @param e the effect to apply onto the entire destination or null.
     */
    void applyEffect(Effect e);

    /**
     * Sets the image smoothing state.
     * Image smoothing is an <a href="#image-attr">Image attribute</a>
     * used to enable or disable image smoothing for
     * {@link #drawImage(javafx.scene.image.Image, double, double) drawImage(all forms)}
     * as specified in the <a href="#attr-ops-table">Rendering Attributes Table</a>.<br>
     * If image smoothing is {@code true}, images will be scaled using a higher
     * quality filtering when transforming or scaling the source image to fit
     * in the destination rectangle.<br>
     * If image smoothing is {@code false}, images will be scaled without filtering
     * (or by using a lower quality filtering) when transforming or scaling the
     * source image to fit in the destination rectangle.
     *
     * @defaultValue {@code true}
     * @param imageSmoothing {@code true} to enable or {@code false} to disable smoothing
     * @since 12
     */
    void setImageSmoothing(boolean imageSmoothing);

    /**
     * Gets the current image smoothing state.
     *
     * @defaultValue {@code true}
     * @return image smoothing state
     * @since 12
     */
    boolean isImageSmoothing();

}
