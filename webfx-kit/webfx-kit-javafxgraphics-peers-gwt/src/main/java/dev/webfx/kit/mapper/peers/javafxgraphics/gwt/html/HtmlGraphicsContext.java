package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlFonts;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.util.Objects;
import elemental2.dom.*;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.CanvasPixelWriter;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.stage.Screen;

/**
 * @author Bruno Salmon
 */
public class HtmlGraphicsContext implements GraphicsContext {

    private final static boolean ENABLE_HDPI_CANVAS = true;
    // DPR stands for Device Pixel Ratio (which is the HTML terminology, while JavaFX calls that output scale)
    final static double DPR = ENABLE_HDPI_CANVAS ? Screen.getPrimary().getOutputScaleX() : 1;

    private final Canvas canvas;
    private final CanvasRenderingContext2D ctx;
    private boolean proportionalFillLinearGradient;

    // Main constructor (directly bound to a JavaFX canvas)

    public HtmlGraphicsContext(Canvas canvas, boolean willReadFrequently) {
        this.canvas = canvas;
        HTMLCanvasElement canvasElement = (HTMLCanvasElement) ((HtmlNodePeer) canvas.getOrCreateAndBindNodePeer()).getElement();
        ctx = Context2DHelper.getCanvasContext2D(canvasElement, willReadFrequently);
        // Applying an immediate mapping between the JavaFX and HTML canvas, otherwise the default behaviour of the
        // WebFX mapper (which is to postpone and process the mapping in the next animation frame) wouldn't work for
        // canvas. The application will indeed probably draw in the canvas just after it is initialized (and sized).
        // If we were to wait for the mapper to resize the canvas in the next animation frame, it would be too late.
        FXProperties.runNowAndOnPropertiesChange(() -> resizeCanvasElement(canvasElement), canvas.widthProperty(), canvas.heightProperty());
        // We apply a DPR scale on HDPI screens
        ctx.scale(DPR, DPR);
    }

    // Alternative constructors used internally for some operations (not directly bound to a JavaFX canvas)

    HtmlGraphicsContext(HTMLCanvasElement canvasElement) {
        this(Context2DHelper.getCanvasContext2D(canvasElement));
    }

    HtmlGraphicsContext(CanvasRenderingContext2D ctx) {
        canvas = null;
        this.ctx = ctx;
    }

    private void resizeCanvasElement(HTMLCanvasElement canvasElement) {
        updateCanvasElementWidth(canvasElement, canvas.getWidth());
        updateCanvasElementHeight(canvasElement, canvas.getHeight());
    }

    static void updateCanvasElementWidth(HTMLCanvasElement canvasElement, double fxCanvasWidth) {
        updateCanvasElementSize(canvasElement, fxCanvasWidth, true);
    }

    static void updateCanvasElementHeight(HTMLCanvasElement canvasElement, double fxCanvasHeight) {
        updateCanvasElementSize(canvasElement, fxCanvasHeight, false);
    }

    private static void updateCanvasElementSize(HTMLCanvasElement canvasElement, double fxCanvasSize, boolean isWidth) {
        boolean isWebGL = canvasElement.getAttribute("webgl") != null;
        CanvasRenderingContext2D ctx = isWebGL ? null : Context2DHelper.getCanvasContext2D(canvasElement);
        // While HTML canvas and JavaFX canvas have an identical size in low-res screens, they differ in HDPI screens
        // because JavaFX automatically apply the pixel conversion, while HTML doesn't.
        int htmlSize = (int) (fxCanvasSize * DPR); // So we apply the DPR factor to get the hi-res number of pixels.
        // Note: the JavaFX canvas size might be 0 initially, but we set a minimal size of 1px for the HTML canvas, the
        // reason is that transforms applied on zero-sized canvas are ignored on Chromium browsers (for example applying
        // the DPR scale on a zero-sized canvas doesn't change the canvas transform), which would make our canvas state
        // snapshot technique below fail.
        if (htmlSize == 0)
            htmlSize = 1;
        // It's very important to prevent changing the canvas size when not necessary, because resetting an HTML canvas
        // size has these 2 serious consequences (even with identical value):
        // 1) the canvas is erased
        // 2) the context state is reset (including transforms, such as the initial DPR scale on HDPI screens)
        boolean htmlSizeHasChanged = isWidth ? canvasElement.width != htmlSize : canvasElement.height != htmlSize;
        if (htmlSizeHasChanged) {
            // We don't want to lose the context state when resizing the canvas, so we take a snapshot of it before
            // resizing, so we can restore it after that.
            Context2DStateSnapshot ctxStateSnapshot = ctx == null ? null : new Context2DStateSnapshot(ctx);
            // Now we can change the canvas size, as we are prepared
            if (isWidth)
                canvasElement.width = htmlSize;  // => erases canvas & reset context sate
            else
                canvasElement.height = htmlSize; // => erases canvas & reset context sate
            // We restore the context state that we have stored in the snapshot (this includes the initial DPR scale)
            if (ctxStateSnapshot != null)
                ctxStateSnapshot.reapply();
            // On HDPI screens, we must also set the CSS size, otherwise the CSS size will be taken from the canvas
            // size by default, which is not what we want because the CSS size is expressed in low-res and not in HDPI
            // pixels like the canvas size, so this would make the canvas appear much too big on the screen.
            if (DPR != 1) { // Scaling down the canvas size with CSS size on HDPI screens
                if (isWidth)
                    canvasElement.style.width = CSSProperties.WidthUnionType.of(fxCanvasSize + "px");
                else
                    canvasElement.style.height = CSSProperties.HeightUnionType.of(fxCanvasSize + "px");
            }
        }
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    // Temporary workaround to fix a mysterious behavior : textAlign can be wiped out
    private String textAlignToSave; // So we keep the value here again and apply it when saving the context

    @Override
    public void save() {
        if (textAlignToSave != null)
            ctx.textAlign = textAlignToSave;
        setTextBaseline(textBaseline);
        ctx.save();
    }

    @Override
    public void restore() {
        ctx.restore();
        textAlignToSave = null;
    }

    @Override
    public void translate(double x, double y) {
        ctx.translate(x, y);
    }

    @Override
    public void scale(double x, double y) {
        ctx.scale(x, y);
    }

    @Override
    public void rotate(double degrees) {
        ctx.rotate(Math.toRadians(degrees));
    }

    @Override
    public void transform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        ctx.transform(mxx, myx, mxy, myy, mxt, myt);
    }

    @Override
    public void setTransform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        if (DPR == 1)
            ctx.setTransform(mxx, myx, mxy, myy, mxt, myt);
        else {
            ctx.setTransform(1, 0, 0, 1, 0, 0); // Same as ctx.resetTransform() (but not provided by Elemental2)
            ctx.scale(DPR, DPR);
            ctx.transform(mxx, myx, mxy, myy, mxt, myt);
        }
    }

    @Override
    public Affine getTransform(Affine xform) {
        Console.log("WARNING: HtmlGraphicsContext.getTransform() not implemented");
        return null;
    }

    @Override
    public void setGlobalAlpha(double alpha) {
        ctx.globalAlpha = Math.max(0, Math.min(alpha, 1));
    }

    @Override
    public double getGlobalAlpha() {
        return ctx.globalAlpha;
    }

    private BlendMode blendMode;
    @Override
    public void setGlobalBlendMode(BlendMode op) {
        blendMode = op;
        ctx.globalCompositeOperation = toCompositeOperation(op);
    }

    private static String toCompositeOperation(BlendMode op) {
        if (op != null)
            switch (op) {
                case DARKEN: return "darken";
                case SCREEN: return "screen";
                case OVERLAY: return "overlay";
                case MULTIPLY: return "multiply";
                case SRC_ATOP: return "source-atop";
                case SRC_OVER: return "source-over";
                case EXCLUSION: return "exclusion";
                case COLOR_BURN: return "color-burn";
                case DIFFERENCE: return "difference";
                case HARD_LIGHT: return "hard-light";
                case SOFT_LIGHT: return "soft-light";
                case COLOR_DODGE: return "color-dodge";
                case LIGHTEN: return "lighten";
                case ADD: // ??
                case RED: // ??
                case BLUE: // ??
                case GREEN: // ??
            }
        return null;
    }

    @Override
    public BlendMode getGlobalBlendMode() {
        return blendMode;
    }

    private Paint fill;
    @Override
    public void setFill(Paint p) {
        fill = p; // Memorizing the value for getFill()
        proportionalFillLinearGradient = false;
        ctx.fillStyle = CanvasRenderingContext2D.FillStyleUnionType.of(toCanvasPaint(p));
    }

    @Override
    public Paint getFill() {
        return fill;
    }

    private Paint stroke;

    @Override
    public void setStroke(Paint p) {
        stroke = p; // Memorizing the value for getStroke()
        ctx.strokeStyle = CanvasRenderingContext2D.StrokeStyleUnionType.of(toCanvasPaint(p));
    }

    @Override
    public Paint getStroke() {
        return stroke;
    }

    private Object toCanvasPaint(Paint paint) {
        if (paint instanceof LinearGradient)
            return toCanvasLinearGradient((LinearGradient) paint, 0, 0, 1, 1);
        if (paint instanceof ImagePattern)
            return toCanvasPattern((ImagePattern) paint);
        return HtmlPaints.toHtmlCssPaint(paint);
    }

    private CanvasGradient toCanvasLinearGradient(LinearGradient lg, double x, double y, double width, double height) {
        proportionalFillLinearGradient = lg.isProportional();
        if (!proportionalFillLinearGradient)
            width = height = 1;
        CanvasGradient clg = ctx.createLinearGradient(x + lg.getStartX() * width, y + lg.getStartY() * height, x + lg.getEndX() * width, y + lg.getEndY() * height);
        lg.getStops().forEach(s -> clg.addColorStop(s.getOffset(), HtmlPaints.toCssColor(s.getColor())));
        return clg;
    }

    private CanvasPattern toCanvasPattern(ImagePattern imagePattern) {
        CanvasPattern peerPattern = (CanvasPattern) imagePattern.getPeerPattern(); // Getting cache version if available
        if (peerPattern == null) {
            Image img = imagePattern.getImage();
            if (img != null) {
                HTMLImageElement imageElement = getHTMLImageElement(img);
                if (isImageLoadedWithoutError(imageElement)) { // Prevents uncaught exception with unloaded images or with error
                    peerPattern = ctx.createPattern(imageElement, "repeat");
                    imagePattern.setPeerPattern(peerPattern);
                }
            }
        }
        return peerPattern;
    }

    private static boolean isImageLoadedWithoutError(HTMLImageElement imageElement) {
        return imageElement.complete // indicates that the image loading has finished (but not if it was successful or not)
                // There is no specific HTML attribute to report if there was an error (such as HTTP 404 image not found),
                // there is only an onerror handler, but it's too late to use here. So we use naturalWidth for the test,
                // because when the image is successfully loaded the browser sets the naturalWidth value (assuming the
                // image is not zero-sized), while the browser leaves that value to 0 on error.
                && imageElement.naturalWidth != 0;
    }

    private void applyProportionalFillLinearGradiant(double x, double y, double width, double height) {
        // setStroke(Color.CYAN); ctx.strokeRect(x, y, width, height); // For visual debugging
        ctx.fillStyle = CanvasRenderingContext2D.FillStyleUnionType.of(toCanvasLinearGradient((LinearGradient) fill, x, y, width, height));
    }

    @Override
    public void setLineWidth(double lw) {
        ctx.lineWidth = lw;
    }

    @Override
    public double getLineWidth() {
        return ctx.lineWidth;
    }

    private StrokeLineCap lineCap;
    @Override
    public void setLineCap(StrokeLineCap cap) {
        lineCap = cap; // Memorizing the value for getLineCap()
        ctx.lineCap = cap == StrokeLineCap.BUTT ? "butt" : cap == StrokeLineCap.SQUARE ? "square" : "round";
    }

    @Override
    public StrokeLineCap getLineCap() {
        return lineCap;
    }

    @Override
    public void setLineJoin(StrokeLineJoin join) {
        Console.log("WARNING: HtmlGraphicsContext.setLineJoin() not implemented");
    }

    @Override
    public StrokeLineJoin getLineJoin() {
        Console.log("WARNING: HtmlGraphicsContext.getLineJoin() not implemented");
        return null;
    }

    @Override
    public void setMiterLimit(double ml) {
        ctx.miterLimit = ml;
    }

    @Override
    public double getMiterLimit() {
        return ctx.miterLimit;
    }

    private double[] dashes;
    @Override
    public void setLineDashes(double... dashes) {
        this.dashes = dashes;
        ctx.setLineDash(dashes);
    }

    @Override
    public double[] getLineDashes() {
        return dashes;
    }

    @Override
    public void setLineDashOffset(double dashOffset) {
        ctx.lineDashOffset = dashOffset;
    }

    @Override
    public double getLineDashOffset() {
        return ctx.lineDashOffset;
    }

    private Font font;
    @Override
    public void setFont(Font f) {
        font = f; // Memorizing the value for getFont()
        ctx.setFont(HtmlFonts.getHtmlFontDefinition(f));
    }

    @Override
    public Font getFont() {
        return font;
    }

    private TextAlignment textAlign;
    @Override
    public void setTextAlign(TextAlignment align) {
        textAlign = align;
        textAlignToSave = ctx.textAlign = HtmlNodePeer.toCssTextAlignment(align);
    }

    @Override
    public TextAlignment getTextAlign() {
        return textAlign;
    }

    private VPos textBaseline;
    @Override
    public void setTextBaseline(VPos baseline) {
        textBaseline = baseline;
        ctx.setTextBaseline(toCssBaseLine(baseline));
    }

    private static String toCssBaseLine(VPos baseline) {
        if (baseline != null)
            switch (baseline) {
                case TOP: return "top";
                case CENTER: return "middle";
                case BASELINE: return "alphabetic";
                case BOTTOM: return "bottom";
            }
        return null;
    }

    @Override
    public VPos getTextBaseline() {
        return textBaseline;
    }

    @Override
    public void fillText(String text, double x, double y) {
        // Multiline management (as opposed to HTML, JavaFX fillText() supports multiline
        if (!text.contains("\n")) { // General case = single line
            fillTextSingleLine(text, x, y);
        } else { // Multiline case
            double lineHeight = measureTextHeight() * 1.7; // empirical formula that works for Food Dice demo
            for (String line : text.split("\n")) {
                fillTextSingleLine(line, x, y);
                y += lineHeight;
            }
        }
    }

    private void fillTextSingleLine(String text, double x, double y) {
        applyProportionalFillLinearGradiantForTextIfApplicable(text, x, y);
        ctx.fillText(text, x, y);
    }

    private double measureTextWidth(String text) {
        return ctx.measureText(text).width;
    }

    private double measureTextHeight() {
        // Pb: measureText() doesn't return height nor any information about the font (should change in the future)
        return ctx.measureText("M").width; // Quick dirty approximation for now
    }

    private void applyProportionalFillLinearGradiantForTextIfApplicable(String text, double x, double y) {
        if (proportionalFillLinearGradient) {
            double width = measureTextWidth(text);
            double height = measureTextHeight();
            double dy = 0;
            VPos tbl = textBaseline;
            if (tbl == null)
                tbl = VPos.BASELINE;
            switch (tbl) {
                case CENTER:   dy = height * 0.5; break;
                case BASELINE: dy = height * 0.7; break; // Quick dirty approximation for now
                case BOTTOM:   dy = height; break;
            }
            //Logger.log("Text height = " + height + ", y = " + y + ", dy = " + y + ", new y = " + (y - dy));
            applyProportionalFillLinearGradiant(x, y - dy, width, height);
        }
    }

    @Override
    public void strokeText(String text, double x, double y) {
        ctx.strokeText(text, x, y);
    }

    @Override
    public void fillText(String text, double x, double y, double maxWidth) {
        applyProportionalFillLinearGradiantForTextIfApplicable(text, x, y);
        ctx.fillText(text, x, y, maxWidth);
    }

    @Override
    public void strokeText(String text, double x, double y, double maxWidth) {
        ctx.strokeText(text, x, y, maxWidth);
    }

    @Override
    public void beginPath() {
        ctx.beginPath();
    }

    @Override
    public void moveTo(double x0, double y0) {
        ctx.moveTo(x0, y0);
    }

    @Override
    public void lineTo(double x1, double y1) {
        ctx.lineTo(x1, y1);
    }

    @Override
    public void quadraticCurveTo(double xc, double yc, double x1, double y1) {
        ctx.quadraticCurveTo(xc, yc, x1, y1);
    }

    @Override
    public void bezierCurveTo(double xc1, double yc1, double xc2, double yc2, double x1, double y1) {
        ctx.bezierCurveTo(xc1, yc1, xc2, yc2, x1, y1);
    }

    @Override
    public void arcTo(double x1, double y1, double x2, double y2, double radius) {
        ctx.arcTo(x1, y1, x2, y2, radius);
    }

    @Override
    public void arc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length) {
        ctx.arc(centerX, centerY, radiusX, -Math.toRadians(startAngle), -Math.toRadians(startAngle + length));
    }

    @Override
    public void rect(double x, double y, double w, double h) {
        ctx.rect(x, y, w, h);
    }

    private Path2D path2D;
    @Override
    public void appendSVGPath(String svgPath) {
        Path2D p2D = new Path2D(svgPath);
        if (path2D == null)
            path2D = p2D;
        else
            path2D.addPath(p2D);
    }

    @Override
    public void closePath() {
        ctx.closePath();
    }

    @Override
    public void fill() {
        if (path2D != null) {
            ctx.fill(path2D);
            path2D = null;
        } else
            ctx.fill();
    }

    @Override
    public void stroke() {
        if (path2D != null) {
            ctx.stroke(path2D);
            path2D = null;
        } else
            ctx.stroke();
    }

    @Override
    public void clip() {
        ctx.clip();
    }

    @Override
    public boolean isPointInPath(double x, double y) {
        return ctx.isPointInPath(x, y);
    }

    @Override
    public void clearRect(double x, double y, double w, double h) {
        ctx.clearRect(x, y, w, h);
    }

    @Override
    public void fillRect(double x, double y, double w, double h) {
        ctx.fillRect(x, y, w, h);
    }

    @Override
    public void strokeRect(double x, double y, double w, double h) {
        ctx.strokeRect(x, y, w, h);
    }

    @Override
    public void fillArc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure) {
        beginPath();
        arc(x, y, w, h, startAngle, arcExtent, closure);
        fill();
    }

    @Override
    public void strokeArc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure) {
        beginPath();
        arc(x, y, w, h, startAngle, arcExtent, closure);
        stroke();
    }

    private void arc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure) {
        if (proportionalFillLinearGradient)
            applyProportionalFillLinearGradiant(x, y, w, h);
        // Inverting angles because HTML is clockwise whereas JavaFX is anticlockwise
        startAngle = -startAngle;
        double endAngle = startAngle - arcExtent;
        ctx.arc(x + w / 2, y + h / 2, w / 2, Math.toRadians(Math.min(startAngle, endAngle)), Math.toRadians(Math.max(startAngle, endAngle)));
        if (closure == ArcType.ROUND)
            ctx.lineTo(x + w / 2, y + h / 2);
    }

    @Override
    public void fillRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight) {
        ctx.beginPath();
        roundRect(ctx, x, y, w, h, arcWidth / 2, arcHeight / 2);
        ctx.closePath();
        ctx.fill();
    }

    @Override
    public void strokeRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight) {
        ctx.beginPath();
        roundRect(ctx, x, y, w, h, arcWidth / 2, arcHeight / 2);
        ctx.closePath();
        ctx.stroke();
    }

    private static Boolean BROWSER_SUPPORTS_ROUND_RECT = null;
    private static void roundRect(CanvasRenderingContext2D ctx, double x, double y, double w, double h, double arcWidth, double arcHeight) {
        if (BROWSER_SUPPORTS_ROUND_RECT == null) {
            BROWSER_SUPPORTS_ROUND_RECT = checkRoundRectNativeSupport(ctx);
            if (!BROWSER_SUPPORTS_ROUND_RECT) {
                Console.log("WARNING: canvas roundRect() function is not supported by this browser - WebFX will use rect() instead");
            }
        }
        if (BROWSER_SUPPORTS_ROUND_RECT)
            roundRectNative(ctx, x, y, w, h, arcWidth, arcHeight);
        else
            ctx.rect(x, y, w, h);
    }

    private static native boolean checkRoundRectNativeSupport(CanvasRenderingContext2D ctx) /*-{
        return typeof ctx.roundRect === 'function';
    }-*/ ;

    private static native void roundRectNative(CanvasRenderingContext2D ctx, double x, double y, double w, double h, double arcWidth, double arcHeight) /*-{
       ctx.roundRect(x, y, w, h, [arcWidth, arcHeight]);
    }-*/ ;

    @Override
    public void strokeLine(double x1, double y1, double x2, double y2) {
        beginPath();
        moveTo(x1, y1);
        lineTo(x2, y2);
        stroke();
    }

    @Override
    public void fillPolygon(double[] xPoints, double[] yPoints, int nPoints) {
        if (drawLines(xPoints, yPoints, nPoints, true))
            fill();
    }

    @Override
    public void strokePolygon(double[] xPoints, double[] yPoints, int nPoints) {
        if (drawLines(xPoints, yPoints, nPoints, true))
            stroke();
    }

    @Override
    public void strokePolyline(double[] xPoints, double[] yPoints, int nPoints) {
        if (drawLines(xPoints, yPoints, nPoints, false))
            stroke();
    }

    private boolean drawLines(double[] xPoints, double[] yPoints, int nPoints, boolean close) {
        if (nPoints < 2)
            return false;
        beginPath();
        moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < nPoints; i++)
            lineTo(xPoints[i], yPoints[i]);
        if (close)
            lineTo(xPoints[0], yPoints[0]);
        return true;
    }

    @Override
    public void drawImage(Image img, double x, double y) {
        drawImage(img, x, y, Math.max(img.getWidth(), img.getRequestedWidth()), Math.max(img.getHeight(), img.getRequestedHeight()));
    }

    @Override
    public void drawImage(Image img, double x, double y, double w, double h) {
        if (img != null) {
            // Flipping management: JavaFX flips images when passing negative values for w or h. As this feature is not
            // natively supported in HTML, we need to add the necessary canvas operations to simulate it.
            boolean flipX = w < 0;
            boolean flipY = h < 0;
            boolean flip = flipX || flipY;
            if (flip) {
                ctx.save();
                ctx.translate(x, y); // Moving to pivot before flipping
                ctx.scale(flipX ? -1 : 1, flipY ? -1 : 1); // flipping the canvas
                ctx.translate(-x, -y); // Moving back to original position after flipping
                w = Math.abs(w);
                h = Math.abs(h);
            }
            boolean loadImage = img.getUrl() != null;
            ImageData imageData = loadImage ? null : ImageDataHelper.getImageDataAssociatedWithImage(img);
            if (imageData != null) {
                HTMLCanvasElement canvasElement = CanvasElementHelper.getCanvasElementReadyToRenderImage(img);
                ctx.drawImage(canvasElement, x, y, w, h);
            } else {
                HTMLCanvasElement canvasElement = loadImage ? null : CanvasElementHelper.getCanvasElementAssociatedWithImage(img);
                if (canvasElement != null)
                    ctx.drawImage(canvasElement, x, y, w, h);
                else {
                    HTMLImageElement imageElement = getHTMLImageElement(img);
                    if (isImageLoadedWithoutError(imageElement)) // Prevents uncaught exception with unloaded images or with error
                        ctx.drawImage(imageElement, x, y, w, h);
                    else
                        drawUnloadedImage(x, y, w, h);
                }
            }
            if (flip)
                ctx.restore();
        }
    }

    @Override
    public void drawImage(Image img, double sx, double sy, double sw, double sh, double dx, double dy, double dw, double dh) {
        if (img != null) {
            // Flipping management: JavaFX flips images when passing negative values for w or h. As this feature is not
            // natively supported in HTML, we need to add the necessary canvas operations to simulate it.
            boolean flipX = sw * dw < 0;
            boolean flipY = sh * dh < 0;
            boolean flip = flipX || flipY;
            if (flip) {
                ctx.save();
                ctx.translate(dx, dy); // Moving to pivot before flipping
                ctx.scale(flipX ? -1 : 1, flipY ? -1 : 1); // flipping the canvas
                ctx.translate(-dx, -dy); // Moving back to original position after flipping
                sw = Math.abs(sw);
                sh = Math.abs(sh);
                dw = Math.abs(dw);
                dh = Math.abs(dh);
            }
            boolean loadImage = img.getUrl() != null;
            ImageData imageData = loadImage ? null : ImageDataHelper.getImageDataAssociatedWithImage(img);
            if (imageData != null) {
                HTMLCanvasElement canvasElement = CanvasElementHelper.getCanvasElementReadyToRenderImage(img);
                // Note: wrong Elemental2 signature. Correct signature = drawImage(image, sx, sy, sw, sh, dx, dy, dw, dh)
                ctx.drawImage(canvasElement, sx, sy, sw, sh, dx, dy, dw, dh);
            } else {
                HTMLCanvasElement canvasElement = loadImage ? null : CanvasElementHelper.getCanvasElementAssociatedWithImage(img);
                if (canvasElement != null)
                    ctx.drawImage(canvasElement, sx, sy, sw, sh, dx, dy, dw, dh);
                else {
                    HTMLImageElement imageElement = getHTMLImageElement(img);
                    if (isImageLoadedWithoutError(imageElement)) { // Prevents uncaught exception with unloaded images or with error
                        // This scaleX/Y computation was necessary to make SpaceFX work
                        // (perhaps it's because this method behaves differently between html and JavaFX?)
                        double scaleX = imageElement.width / img.getWidth();
                        double scaleY = imageElement.height / img.getHeight();
                        ctx.drawImage(imageElement, sx * scaleX, sy * scaleY, sw * scaleX, sh * scaleY, dx, dy, dw, dh);
                    } else
                        drawUnloadedImage(dx, dy, dw, dh);
                }
            }
            if (flip)
                ctx.restore();
        }
    }

    public static HTMLImageElement getHTMLImageElement(Image image) {
        HTMLImageElement imageElement;
        Object peerImage = image.getPeerImage();
        if (peerImage instanceof HTMLImageElement)
            imageElement = (HTMLImageElement) peerImage;
        else
            image.setPeerImage(imageElement = HtmlUtil.createImageElement());
        String url = image.getUrl();
        if (!Objects.areEquals(url, image.getPeerUrl())) {
            image.setPeerUrl(url);
            imageElement.src = url;
            imageElement.onload = e -> {
                HtmlImageViewPeer.onHTMLImageLoaded(imageElement, image);
                return null;
            };
        }
        return imageElement;
    }

    private void drawUnloadedImage(double x, double y, double w, double h) {
        if (w > 0 && h > 0) {
            ctx.save();
            ctx.beginPath();
            double cx = x + w / 2;
            double cy = y + h / 2;
            double r = Math.min(w, h) / 2;
            ctx.arc(cx, cy, r, 0, 2 * Math.PI);
            ctx.strokeStyle = BaseRenderingContext2D.StrokeStyleUnionType.of("#C0C0C0C0");
            ctx.stroke();
            if (r > 20)
                ctx.strokeRect(x + 5, cy - 5, w - 10, 10);
            ctx.closePath();
            ctx.restore();
        }
    }

    private PixelWriter pixelWriter;
    @Override
    public PixelWriter getPixelWriter() {
        if (pixelWriter == null)
            pixelWriter = new CanvasPixelWriter(canvas);
        return pixelWriter;
    }

    private Effect effect;
    @Override
    public void setEffect(Effect e) {
        effect = e;
        if (e instanceof DropShadow) {
            DropShadow dropShadow = (DropShadow) e;
            ctx.shadowBlur = dropShadow.getRadius();
            ctx.shadowOffsetX = dropShadow.getOffsetX();
            ctx.shadowOffsetY = dropShadow.getOffsetY();
            ctx.shadowColor = HtmlPaints.toCssColor(dropShadow.getColor());
        } else {
            ctx.shadowBlur = 0;
            Console.log("WARNING: HtmlGraphicsContext.setEffect() not implemented for effect = " + e);
        }
    }

    @Override
    public Effect getEffect(Effect e) {
        return effect;
    }

    @Override
    public void applyEffect(Effect e) {
        Console.log("WARNING: HtmlGraphicsContext.applyEffect() not implemented");
    }

}
