package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.HTMLCanvasElement;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlFonts;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.services.log.Logger;

/**
 * @author Bruno Salmon
 */
public class HtmlGraphicsContext implements GraphicsContext {
    
    private final Canvas canvas;
    private CanvasRenderingContext2D ctx;

    public HtmlGraphicsContext(Canvas canvas) {
        this.canvas = canvas;
        Properties.onPropertySet(canvas.sceneProperty(), scene -> ctx = (CanvasRenderingContext2D) (Object) ((HTMLCanvasElement) ((HtmlNodePeer) canvas.getOrCreateAndBindNodePeer()).getElement()).getContext("2d"));
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void save() {
        ctx.save();
    }

    @Override
    public void restore() {
        ctx.restore();
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
        Logger.log("HtmlGraphicsContext.getTransform() not implemented");
        return null;
    }

    @Override
    public void setGlobalAlpha(double alpha) {
        ctx.globalAlpha = alpha;
    }

    @Override
    public double getGlobalAlpha() {
        return ctx.globalAlpha;
    }

    @Override
    public void setGlobalBlendMode(BlendMode op) {
        Logger.log("HtmlGraphicsContext.setGlobalBlendMode() not implemented");
    }

    @Override
    public BlendMode getGlobalBlendMode() {
        Logger.log("HtmlGraphicsContext.getGlobalBlendMode() not implemented");
        return null;
    }

    private Paint fill;
    @Override
    public void setFill(Paint p) {
        fill = p; // Memorizing the value for getFill()
        ctx.fillStyle = CanvasRenderingContext2D.FillStyleUnionType.of(HtmlPaints.toHtmlCssPaint(fill));
    }

    @Override
    public Paint getFill() {
        return fill;
    }

    private Paint stroke;
    @Override
    public void setStroke(Paint p) {
        stroke = p; // Memorizing the value for getStroke()
        ctx.strokeStyle =  CanvasRenderingContext2D.StrokeStyleUnionType.of(HtmlPaints.toHtmlCssPaint(stroke));
    }

    @Override
    public Paint getStroke() {
        return stroke;
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
        Logger.log("HtmlGraphicsContext.setLineJoin() not implemented");
    }

    @Override
    public StrokeLineJoin getLineJoin() {
        Logger.log("HtmlGraphicsContext.getLineJoin() not implemented");
        return null;
    }

    @Override
    public void setMiterLimit(double ml) {
        Logger.log("HtmlGraphicsContext.setMiterLimit() not implemented");
    }

    @Override
    public double getMiterLimit() {
        Logger.log("HtmlGraphicsContext.getMiterLimit() not implemented");
        return 0;
    }

    @Override
    public void setLineDashes(double... dashes) {
        Logger.log("HtmlGraphicsContext.setLineDashes() not implemented");
    }

    @Override
    public double[] getLineDashes() {
        Logger.log("HtmlGraphicsContext.getLineDashes() not implemented");
        return new double[0];
    }

    @Override
    public void setLineDashOffset(double dashOffset) {
        Logger.log("HtmlGraphicsContext.setLineDashOffset() not implemented");
    }

    @Override
    public double getLineDashOffset() {
        Logger.log("HtmlGraphicsContext.getLineDashOffset() not implemented");
        return 0;
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

    @Override
    public void setTextAlign(TextAlignment align) {
        ctx.textAlign = HtmlNodePeer.toCssTextAlignment(align);
    }

    @Override
    public TextAlignment getTextAlign() {
        Logger.log("HtmlGraphicsContext.getTextAlign() not implemented");
        return null;
    }

    private VPos textBaseline;
    @Override
    public void setTextBaseline(VPos baseline) {
        textBaseline = baseline;
        ctx.setTextBaseline(toCssBaseLine(baseline));
    }

    private static String toCssBaseLine(VPos baseline) {
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
        ctx.fillText(text, x, y);
    }

    @Override
    public void strokeText(String text, double x, double y) {
        ctx.strokeText(text, x, y);
    }

    @Override
    public void fillText(String text, double x, double y, double maxWidth) {
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
        Logger.log("HtmlGraphicsContext.appendSVGPath() not implemented");
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
        ctx.arc(x + w / 2, y + h / 2, w / 2, - degreesToRadiant(startAngle), - degreesToRadiant(startAngle + arcExtent));
    }

        @Override
    public void fillRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight) {
        Logger.log("HtmlGraphicsContext.fillRoundRect() not implemented");
    }

    @Override
    public void strokeRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight) {
        Logger.log("HtmlGraphicsContext.strokeRoundRect() not implemented");
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
        Logger.log("HtmlGraphicsContext.fillPolygon() not implemented");
    }

    @Override
    public void strokePolygon(double[] xPoints, double[] yPoints, int nPoints) {
        Logger.log("HtmlGraphicsContext.strokePolygon() not implemented");
    }

    @Override
    public void strokePolyline(double[] xPoints, double[] yPoints, int nPoints) {
        Logger.log("HtmlGraphicsContext.strokePolyline() not implemented");
    }

    @Override
    public void drawImage(Image img, double x, double y) {
        if (img instanceof HtmlCanvasImage)
            ctx.drawImage((HTMLCanvasElement) ((HtmlCanvasImage) img).getHtmlCanvasPeer().getElement(), x, y);
        else
            Logger.log("HtmlGraphicsContext.drawImage() not implemented for img = " + img);
    }

    @Override
    public void drawImage(Image img, double x, double y, double w, double h) {
        Logger.log("HtmlGraphicsContext.drawImage() not implemented");
    }

    @Override
    public void drawImage(Image img, double sx, double sy, double sw, double sh, double dx, double dy, double dw, double dh) {
        Logger.log("HtmlGraphicsContext.drawImage() not implemented");
    }

    @Override
    public PixelWriter getPixelWriter() {
        Logger.log("HtmlGraphicsContext.getPixelWriter() not implemented");
        return null;
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
            Logger.log("HtmlGraphicsContext.setEffect() not implemented for effect = " + e);
        }
    }

    @Override
    public Effect getEffect(Effect e) {
        return effect;
    }

    @Override
    public void applyEffect(Effect e) {
        Logger.log("HtmlGraphicsContext.applyEffect() not implemented");
    }

    private double degreesToRadiant(double degree) {
        return degree * Math.PI / 180;
    }
}
