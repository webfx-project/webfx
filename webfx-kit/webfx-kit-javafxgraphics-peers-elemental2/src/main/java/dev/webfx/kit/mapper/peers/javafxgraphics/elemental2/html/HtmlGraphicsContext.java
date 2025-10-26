package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlFonts;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.util.Objects;
import dev.webfx.platform.util.elemental2.Elemental2Util;
import elemental2.core.JsArray;
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
import jsinterop.base.Js;

/**
 * @author Bruno Salmon
 */
public class HtmlGraphicsContext implements GraphicsContext {

    private final Canvas canvas;
    private final CanvasRenderingContext2D ctx;
    private boolean proportionalFillLinearGradient;

    // Main constructor (directly bound to a JavaFX canvas)
    public HtmlGraphicsContext(Canvas canvas, boolean willReadFrequently) {
        this.canvas = canvas;
        HTMLCanvasElement canvasElement = (HTMLCanvasElement) ((HtmlNodePeer) canvas.getOrCreateAndBindNodePeer()).getElement();
        ctx = Context2DHelper.getCanvasContext2D(canvasElement, willReadFrequently);
        // We apply a scale on HDPI screens
        double pixelDensity = getPixelDensity(); // Note: this call will create the pixelDensityProperty and immediately size the canvas (see GwtWebFxKitLauncherProvider.canvasPixelDensityProperty())
        ctx.scale(pixelDensity, pixelDensity);
    }

    // Alternative constructors used internally for some operations (not directly bound to a JavaFX canvas)
    HtmlGraphicsContext(HTMLCanvasElement canvasElement) {
        this(Context2DHelper.getCanvasContext2D(canvasElement));
    }

    HtmlGraphicsContext(CanvasRenderingContext2D ctx) {
        canvas = null;
        this.ctx = ctx;
    }

    // HDPI management
    private double getPixelDensity() {
        return WebFxKitLauncher.getCanvasPixelDensity(canvas);
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    // Temporary workaround to fix a mysterious behavior: textAlign can be wiped out
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
        double dpr = getPixelDensity();
        if (dpr == 1)
            ctx.setTransform(mxx, myx, mxy, myy, mxt, myt);
        else {
            ctx.setTransform(1, 0, 0, 1, 0, 0); // Same as ctx.resetTransform() (but not provided by Elemental2)
            ctx.scale(dpr, dpr);
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
        if (op != null) {
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
        CanvasPattern peerPattern = (CanvasPattern) imagePattern.getPeerPattern(); // Getting the cache version if available
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
        return imageElement.complete // Indicates that the image loading has finished (but not if it was successful or not).
               // There is no specific HTML attribute to report if there was an error (such as HTTP 404 image not found),
               // there is only an onerror handler, but it's too late to use here. So we use naturalWidth for the test,
               // because when the image is successfully loaded, the browser sets the naturalWidth value (assuming the
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

    private Font javafxFont;
    private String htmlFont;

    @Override
    public void setFont(Font f) {
        if (f != javafxFont) {
            javafxFont = f; // Memorizing the value for getFont()
            htmlFont = HtmlFonts.getHtmlFontDefinition(f);
        }
        ctx.setFont(htmlFont);
    }

    @Override
    public Font getFont() {
        return javafxFont;
    }

    private TextAlignment textAlign;
    @Override
    public void setTextAlign(TextAlignment align) {
        textAlign = align;
        // Assigning ctx.textAlign to null causes a warning in the browser console, so we prevent that
        if (align == null)
            textAlignToSave = null;
        else
            textAlignToSave = ctx.textAlign = HtmlNodePeer.toCssTextAlignment(align);
    }

    @Override
    public TextAlignment getTextAlign() {
        return textAlign;
    }

    private VPos textBaseline = VPos.BASELINE; // Default value in JavaFX
    @Override
    public void setTextBaseline(VPos baseline) {
        textBaseline = baseline;
        String cssBaseLine = toCssBaseLine(baseline);
        // Assigning ctx.textBaseAlign to null causes a warning in the browser console, so we prevent that
        if (cssBaseLine != null)
            ctx.setTextBaseline(cssBaseLine);
    }

    private static String toCssBaseLine(VPos baseline) {
        if (baseline != null) {
            return switch (baseline) {
                case TOP -> "top";
                case CENTER -> "middle";
                case BASELINE -> "alphabetic";
                case BOTTOM -> "bottom";
            };
        }
        return null; // Shouldn't be used for HTML canvas text baseline as this causes a warning in the browser console
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
            dy = switch (tbl) {
                case CENTER -> height * 0.5;
                case BASELINE -> height * 0.7; // Quick dirty approximation for now
                case BOTTOM -> height;
                default -> dy;
            };
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
        roundRect(x, y, w, h, arcWidth / 2, arcHeight / 2);
        ctx.closePath();
        ctx.fill();
    }

    @Override
    public void strokeRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight) {
        ctx.beginPath();
        roundRect(x, y, w, h, arcWidth / 2, arcHeight / 2);
        ctx.closePath();
        ctx.stroke();
    }

    private static Boolean BROWSER_SUPPORTS_ROUND_RECT = null;

    private void roundRect(double x, double y, double w, double h, double arcWidth, double arcHeight) {
        if (BROWSER_SUPPORTS_ROUND_RECT == null) {
            BROWSER_SUPPORTS_ROUND_RECT = Js.asPropertyMap(ctx).get("roundRect") != null;
            if (!BROWSER_SUPPORTS_ROUND_RECT) {
                Console.log("WARNING: canvas roundRect() function is not supported by this browser - WebFX will use rect() instead");
            }
        }
        if (BROWSER_SUPPORTS_ROUND_RECT)
            ctx.roundRect(x, y, w, h, CanvasPathMethods.RoundRectRadiiUnionType.of(JsArray.of(arcWidth, arcHeight)));
        else
            ctx.rect(x, y, w, h);
    }

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
                ctx.translate(-x, -y); // Moving back to the original position after flipping
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
                ctx.translate(-dx, -dy); // Moving back to the original position after flipping
                sw = Math.abs(sw);
                sh = Math.abs(sh);
                dw = Math.abs(dw);
                dh = Math.abs(dh);
            }
            boolean loadImage = img.getUrl() != null;
            ImageData imageData = loadImage ? null : ImageDataHelper.getImageDataAssociatedWithImage(img);
            boolean useSafariTiling = Elemental2Util.isSafari() && isLargeImageOperation(sw, sh, dw, dh);
            if (imageData != null) {
                HTMLCanvasElement canvasElement = CanvasElementHelper.getCanvasElementReadyToRenderImage(img);
                if (useSafariTiling)
                    drawImageTiled(canvasElement, sx, sy, sw, sh, dx, dy, dw, dh);
                else
                    ctx.drawImage(canvasElement, sx, sy, sw, sh, dx, dy, dw, dh);
            } else {
                HTMLCanvasElement canvasElement = loadImage ? null : CanvasElementHelper.getCanvasElementAssociatedWithImage(img);
                if (canvasElement != null) {
                    if (useSafariTiling)
                        drawImageTiled(canvasElement, sx, sy, sw, sh, dx, dy, dw, dh);
                    else
                        ctx.drawImage(canvasElement, sx, sy, sw, sh, dx, dy, dw, dh);
                } else {
                    HTMLImageElement imageElement = getHTMLImageElement(img);
                    if (isImageLoadedWithoutError(imageElement)) { // Prevents uncaught exception with unloaded images or with error
                        // This scaleX/Y computation was necessary to make SpaceFX work
                        // (perhaps it's because this method behaves differently between HTML and JavaFX?)
                        double scaleX = imageElement.width / img.getWidth();
                        double scaleY = imageElement.height / img.getHeight();
                        double ssx = sx * scaleX, ssy = sy * scaleY, ssw = sw * scaleX, ssh = sh * scaleY;
                        if (useSafariTiling)
                            drawImageTiled(imageElement, ssx, ssy, ssw, ssh, dx, dy, dw, dh);
                        else
                            ctx.drawImage(imageElement, ssx, ssy, ssw, ssh, dx, dy, dw, dh);
                    } else
                        drawUnloadedImage(dx, dy, dw, dh);
                }
            }
            if (flip)
                ctx.restore();
        }
    }

    // Workaround for Safari canvas drawImage distortion with large images/rectangles
    private static final int SAFARI_TILE_SIZE = 1024; // conservative tile size

    private static boolean isLargeImageOperation(double sw, double sh, double dw, double dh) {
        double asw = Math.abs(sw), ash = Math.abs(sh), adw = Math.abs(dw), adh = Math.abs(dh);
        return asw > SAFARI_TILE_SIZE || ash > SAFARI_TILE_SIZE || adw > 2 * SAFARI_TILE_SIZE || adh > 2 * SAFARI_TILE_SIZE;
    }

    private void drawImageTiled(Object source, double sx, double sy, double sw, double sh, double dx, double dy, double dw, double dh) {
        double scaleX = dw / sw;
        double scaleY = dh / sh;
        for (double ty = 0; ty < sh; ty += SAFARI_TILE_SIZE) {
            double th = Math.min(SAFARI_TILE_SIZE, sh - ty);
            for (double tx = 0; tx < sw; tx += SAFARI_TILE_SIZE) {
                double tw = Math.min(SAFARI_TILE_SIZE, sw - tx);
                double sxi = sx + tx, syi = sy + ty;
                double dxi = dx + tx * scaleX, dyi = dy + ty * scaleY;
                double dwi = tw * scaleX, dhi = th * scaleY;
                if (source instanceof HTMLImageElement)
                    ctx.drawImage((HTMLImageElement) source, sxi, syi, tw, th, dxi, dyi, dwi, dhi);
                else
                    ctx.drawImage((HTMLCanvasElement) source, sxi, syi, tw, th, dxi, dyi, dwi, dhi);
            }
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
        if (e instanceof DropShadow dropShadow) {
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

    @Override
    public void setImageSmoothing(boolean imageSmoothing) {
        ctx.imageSmoothingEnabled = imageSmoothing;
    }

    @Override
    public boolean isImageSmoothing() {
        return ctx.imageSmoothingEnabled;
    }
}
