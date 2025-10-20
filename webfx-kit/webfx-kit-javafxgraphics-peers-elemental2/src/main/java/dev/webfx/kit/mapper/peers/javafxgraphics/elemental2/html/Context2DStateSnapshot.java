package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import elemental2.dom.BaseRenderingContext2D;
import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.DOMMatrixReadOnly;
import jsinterop.base.Js;

/**
 * @author Bruno Salmon
 */
final class Context2DStateSnapshot {
    private final CanvasRenderingContext2D ctx;
    private final BaseRenderingContext2D.FillStyleUnionType fillStyle;
    private final BaseRenderingContext2D.StrokeStyleUnionType strokeStyle;
    private final double lineWidth;
    private final String lineCap1;
    private final String lineJoin;
    private final double miterLimit;
    private final double globalAlpha;
    private final String globalCompositeOperation;
    private final double shadowBlur;
    private final String shadowColor;
    private final double shadowOffsetX;
    private final double shadowOffsetY;
    private final String font1;
    private final String textAlign1;
    private final String textBaseline1;
    private final boolean imageSmoothingEnabled;
    private final DOMMatrixReadOnly jsTransform;

    public Context2DStateSnapshot(CanvasRenderingContext2D ctx) {
        this.ctx = Js.cast(ctx);
        fillStyle = ctx.fillStyle;
        strokeStyle = ctx.strokeStyle;
        lineWidth = ctx.lineWidth;
        lineCap1 = ctx.lineCap;
        lineJoin = ctx.lineJoin;
        miterLimit = ctx.miterLimit;
        globalAlpha = ctx.globalAlpha;
        globalCompositeOperation = ctx.globalCompositeOperation;
        shadowBlur = ctx.shadowBlur;
        shadowColor = ctx.shadowColor;
        shadowOffsetX = ctx.shadowOffsetX;
        shadowOffsetY = ctx.shadowOffsetY;
        font1 = ctx.font;
        textAlign1 = ctx.textAlign;
        textBaseline1 = ctx.textBaseline;
        imageSmoothingEnabled = ctx.imageSmoothingEnabled;
        jsTransform = ctx.getTransform();
    }

    void reapply() {
        ctx.fillStyle = fillStyle;
        ctx.strokeStyle = strokeStyle;
        ctx.lineWidth = lineWidth;
        ctx.lineCap = lineCap1;
        ctx.lineJoin = lineJoin;
        ctx.miterLimit = miterLimit;
        ctx.globalAlpha = globalAlpha;
        ctx.globalCompositeOperation = globalCompositeOperation;
        ctx.shadowBlur = shadowBlur;
        ctx.shadowColor = shadowColor;
        ctx.shadowOffsetX = shadowOffsetX;
        ctx.shadowOffsetY = shadowOffsetY;
        ctx.font = font1;
        ctx.textAlign = textAlign1;
        ctx.textBaseline = textBaseline1;
        ctx.imageSmoothingEnabled = imageSmoothingEnabled;
        ctx.setTransform(jsTransform);
    }

}
