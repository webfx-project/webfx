package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlFonts;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.platform.console.Console;
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
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class HtmlGraphicsContext implements GraphicsContext {

    private final Canvas canvas;
    private CanvasRenderingContext2D ctx;
    private boolean proportionalFillLinearGradient;

    HtmlGraphicsContext(HTMLCanvasElement canvasElement) {
        this(getCanvasRenderingContext2D(canvasElement));
    }

    HtmlGraphicsContext(CanvasRenderingContext2D ctx) {
        canvas = null;
        this.ctx = ctx;
    }

    public HtmlGraphicsContext(Canvas canvas) {
        this.canvas = canvas;
        HTMLCanvasElement canvasElement = (HTMLCanvasElement) ((HtmlNodePeer) this.canvas.getOrCreateAndBindNodePeer()).getElement();
        // Setting the canvas size now because if done later (by the WebFX mapping), this will erase the canvas!
        canvasElement.width =  (int) this.canvas.getWidth();  // Won't be touched by the mapper (see HtmlCanvasPeer.updateWidth())
        canvasElement.height = (int) this.canvas.getHeight(); // Won't be touched by the mapper (see HtmlCanvasPeer.updateHeight())
        ctx = getCanvasRenderingContext2D(canvasElement);
    }

    private static CanvasRenderingContext2D getCanvasRenderingContext2D(HTMLCanvasElement canvasElement) {
        return (CanvasRenderingContext2D) (Object) canvasElement.getContext("2d");
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    // Temporary workaround to fix a mysterious behavior : textAlign can be wipe out
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
        ctx.rotate(degreesToRadiant(degrees));
    }

    @Override
    public void transform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        ctx.transform(mxx, myx, mxy, myy, mxt, myt);
    }

    @Override
    public void setTransform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        ctx.setTransform(mxx, myx, mxy, myy, mxt, myt);
    }

    @Override
    public Affine getTransform(Affine xform) {
        Console.log("HtmlGraphicsContext.getTransform() not implemented");
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
        Console.log("HtmlGraphicsContext.setLineJoin() not implemented");
    }

    @Override
    public StrokeLineJoin getLineJoin() {
        Console.log("HtmlGraphicsContext.getLineJoin() not implemented");
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
        applyProportionalFillLinearGradiantForTextIfApplicable(text, x, y);
        ctx.fillText(text, x, y);
    }

    private void applyProportionalFillLinearGradiantForTextIfApplicable(String text, double x, double y) {
        if (proportionalFillLinearGradient) {
            double width = ctx.measureText(text).width;
            // Pb: measureText() doesn't return height nor any information about the font (should change in the future)
            double height = ctx.measureText("M").width; // Quick dirty approximation for now
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
        ctx.arc(centerX, centerY, radiusX, - degreesToRadiant(startAngle), - degreesToRadiant(startAngle + length));
    }

    @Override
    public void rect(double x, double y, double w, double h) {
        ctx.rect(x, y, w, h);
    }

    @Override
    public void appendSVGPath(String svgpath) {
        Console.log("HtmlGraphicsContext.appendSVGPath() not implemented");
    }

    @Override
    public void closePath() {
        ctx.closePath();
    }

    @Override
    public void fill() {
        ctx.fill();
    }

    @Override
    public void stroke() {
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
        ctx.arc(x + w / 2, y + h / 2, w / 2, degreesToRadiant(Math.min(startAngle, endAngle)), degreesToRadiant(Math.max(startAngle, endAngle)));
        if (closure == ArcType.ROUND)
            ctx.lineTo(x + w / 2, y + h / 2);
    }

        @Override
    public void fillRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight) {
        Console.log("HtmlGraphicsContext.fillRoundRect() not implemented");
    }

    @Override
    public void strokeRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight) {
        Console.log("HtmlGraphicsContext.strokeRoundRect() not implemented");
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
            ImageData imageData = HtmlCanvasPeer.getPeerImageData(img);
            if (imageData != null) {
                // Note: putImageData() is faster, but it doesn't stretch the image (as opposed to drawImage())
                if (false /* DOESN'T HANDLE TRANSPARENCY */ && imageData.width == w && imageData.height == h) // If no stretch is required
                    ctx.putImageData(imageData, (int) x, (int) y);  // We use putImageData()
                else { // Otherwise we use the Image canvas instead
                    HTMLCanvasElement canvasElement = HtmlCanvasPeer.getRenderingCanvas(img);
                    ctx.drawImage(canvasElement, x, y, w, h);
                }
            } else {
                HTMLCanvasElement canvasElement = HtmlCanvasPeer.getImageCanvasElement(img);
                if (canvasElement != null)
                    ctx.drawImage(canvasElement, x, y, w, h);
                else {
                    HTMLImageElement imageElement = getHTMLImageElement(img.getUrl());
                    ctx.drawImage(imageElement, x, y, w, h);
                    if (!imageElement.complete) {
                        drawUnloadedImage(x, y, w, h, "#C0C0C0C0");
                        imageElement.onload = e -> {
                            HtmlImageViewPeer.onHTMLImageLoaded(imageElement, img);
                            return null;
                        };
                    } else
                        HtmlImageViewPeer.onHTMLImageLoaded(imageElement, img);
                }
            }
        }
    }

    @Override
    public void drawImage(Image img, double sx, double sy, double sw, double sh, double dx, double dy, double dw, double dh) {
        if (img != null) {
            HTMLCanvasElement canvasElement = HtmlCanvasPeer.getImageCanvasElement(img);
            if (canvasElement != null)
                ctx.drawImage(canvasElement, sx, sy, sw, sh, dx, dy, dw, dh);
            else {
                HTMLImageElement imageElement = getHTMLImageElement(img.getUrl());
                // This scaleX/Y computation was necessary to make SpaceFX work
                // (perhaps it's because this method behaves differently between html and JavaFX?)
                double scaleX = imageElement.width / img.getWidth();
                double scaleY = imageElement.height / img.getHeight();
                ctx.drawImage(imageElement, sx * scaleX, sy * scaleY, sw * scaleX, sh * scaleY, dx, dy, dw, dh);
                if (!imageElement.complete) {
                    drawUnloadedImage(dx, dy, dw, dh, "#C0C0C0C0");
                    imageElement.onload = e -> {
                        HtmlImageViewPeer.onHTMLImageLoaded(imageElement, img);
                        return null;
                    };
                } else
                    HtmlImageViewPeer.onHTMLImageLoaded(imageElement, img);
            }
        }
    }

    private void drawUnloadedImage(double x, double y, double w, double h, String strokeStyle) {
        if (w > 0 && h > 0) {
            ctx.save();
            ctx.beginPath();
            double cx = x + w / 2;
            double cy = y + h / 2;
            double r = Math.min(w, h) / 2;
            ctx.arc(cx, cy, r, 0, 2 * Math.PI);
            ctx.strokeStyle = BaseRenderingContext2D.StrokeStyleUnionType.of(strokeStyle);
            ctx.stroke();
            if (r > 20)
                ctx.strokeRect(x + 5, cy - 5, w - 10, 10);
            ctx.closePath();
            ctx.restore();
        }
    }

    private static final Map<String, HTMLImageElement> htmlImageElementMap = new HashMap<>();

    private HTMLImageElement getHTMLImageElement(String url) {
        HTMLImageElement imageElement = htmlImageElementMap.get(url);
        if (imageElement == null) {
            htmlImageElementMap.put(url, imageElement = HtmlUtil.createImageElement());
            imageElement.src = url;
        }
        return imageElement;
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
            Console.log("HtmlGraphicsContext.setEffect() not implemented for effect = " + e);
        }
    }

    @Override
    public Effect getEffect(Effect e) {
        return effect;
    }

    @Override
    public void applyEffect(Effect e) {
        Console.log("HtmlGraphicsContext.applyEffect() not implemented");
    }

    private double degreesToRadiant(double degree) {
        return degree * Math.PI / 180;
    }
}
