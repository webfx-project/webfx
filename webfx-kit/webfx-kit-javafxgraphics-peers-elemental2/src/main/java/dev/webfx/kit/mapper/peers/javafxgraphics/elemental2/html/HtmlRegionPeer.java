package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.HasNoChildrenPeers;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.DomType;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlRegionPeer
    <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

    extends HtmlNodePeer<N, NB, NM>
    implements RegionPeerMixin<N, NB, NM> {

    private final HTMLElement fxBackground;
    private final HTMLElement fxBorder;

    protected HtmlRegionPeer(NB base, HTMLElement element) {
        super(base, element);
        if (this instanceof HasNoChildrenPeers) {
            fxBackground = element;
            fxBorder = element;
        } else {
            fxBackground = createBehindElement("fx-background");
            fxBorder = createBehindElement("fx-border");
            fxBorder.style.boxSizing = "border-box";
            HTMLElement fxChildren = createBehindElement("fx-children");
            setChildrenContainer(fxChildren);
            HtmlUtil.setChildren(element, fxBackground, fxBorder, fxChildren);
        }
    }

    private static HTMLElement createBehindElement(String tag) {
        HTMLElement element = HtmlUtil.createElement(tag);
        CSSStyleDeclaration style = element.style;
        style.display = "block";
        style.width = CSSProperties.WidthUnionType.of("100%");
        style.height = CSSProperties.HeightUnionType.of("100%");
        style.position = "absolute";
        style.left = "0";
        style.top = "0";
        return element;
    }

    @Override
    public void updateWidth(Number width) {
        double w = width.doubleValue();
        if (w >= 0) {
            getElement().style.width = CSSProperties.WidthUnionType.of(toPx(w));
            clearLayoutCache();
        }
    }

    @Override
    public void updateHeight(Number height) {
        double h = height.doubleValue();
        if (h >= 0) {
            getElement().style.height = CSSProperties.HeightUnionType.of(toPx(h));
            clearLayoutCache();
        }
    }

    @Override
    public void updateBackground(Background background) {
        CSSStyleDeclaration style = getBackgroundElement().style;
        style.background = toCssBackground(background);
        CornerRadii radii = null;
        if (background != null) {
            // Note: for now, we support only one border that we take from the first background fill
            BackgroundFill firstFill = Collections.get(background.getFills(), 0);
            if (firstFill != null)
                radii = firstFill.getRadii();
        }
        if (radii == null)
            style.border = null;
        applyBorderRadii(radii, style);
    }

    protected HTMLElement getBackgroundElement() {
        return fxBackground;
    }

    protected HTMLElement getBorderElement() {
        return fxBorder;
    }

    @Override
    protected HTMLElement getEffectElement() {
        return getBackgroundElement();
    }

    @Override
    public void updateBorder(Border border) {
        CSSStyleDeclaration style = getBorderElement().style;
        // Note: for now, we support only one border that we take from the first border stroke
        BorderStroke firstStroke = border == null ? null : Collections.get(border.getStrokes(), 0);
        if (firstStroke == null) {
            style.border = style.borderLeft = style.borderTop = style.borderRight = style.borderBottom = null;
        } else if (firstStroke.isStrokeUniform()) { // this includes gradients
            BorderWidths widths = firstStroke.getWidths();
            style.borderLeft = style.borderTop = style.borderRight = style.borderBottom = null;
            // TODO: consider non-uniform border widths
            style.border = toCssBorder(firstStroke.getLeftStroke(), firstStroke.getLeftStyle(), widths.getLeft(), widths.isLeftAsPercentage());
            applyBorderRadii(firstStroke.getRadii(), style);
        } else { // Should be only plain colors (CSS style.borderLeft, etc... don't work with gradients)
            BorderWidths widths = firstStroke.getWidths();
            style.border = null;
            style.borderLeft = toCssBorder(firstStroke.getLeftStroke(), firstStroke.getLeftStyle(), widths.getLeft(), widths.isLeftAsPercentage());
            style.borderTop = toCssBorder(firstStroke.getTopStroke(), firstStroke.getTopStyle(), widths.getTop(), widths.isTopAsPercentage());
            style.borderRight = toCssBorder(firstStroke.getRightStroke(), firstStroke.getRightStyle(), widths.getRight(), widths.isRightAsPercentage());
            style.borderBottom = toCssBorder(firstStroke.getBottomStroke(), firstStroke.getBottomStyle(), widths.getBottom(), widths.isBottomAsPercentage());
            applyBorderRadii(firstStroke.getRadii(), style);
        }
    }

    @Override
    public void updatePadding(Insets padding) {
        // Note: this code should be executed only if the html content is managed by the peer, otherwise it should be
        // skipped (ex: css padding not needed for layout peers or controls with content provided by skin)
        /* Finally commented on 29/03/2024 while working on WebFX CSS. TODO: completely remove this method if no side effect
         getElement().style.padding = toCssPadding(padding);
         */
    }

    protected CSSProperties.PaddingUnionType toCssPadding(Insets padding) {
        if (padding == null)
            return null;
        String cssPadding = toPx(padding.getTop());
        return CSSProperties.PaddingUnionType.of(cssPadding);
    }

    private void applyBorderRadii(CornerRadii radii, CSSStyleDeclaration style) {
        if (radii == null)
            style.borderRadius = null;
        else {
            style.borderTopLeftRadius = CSSProperties.BorderTopLeftRadiusUnionType.of(toPx(Math.max(radii.getTopLeftHorizontalRadius(), radii.getTopLeftVerticalRadius())));
            style.borderTopRightRadius = CSSProperties.BorderTopRightRadiusUnionType.of(toPx(Math.max(radii.getTopRightHorizontalRadius(), radii.getTopRightVerticalRadius())));
            style.borderBottomRightRadius = CSSProperties.BorderBottomRightRadiusUnionType.of(toPx(Math.max(radii.getBottomRightHorizontalRadius(), radii.getBottomRightVerticalRadius())));
            style.borderBottomLeftRadius = CSSProperties.BorderBottomLeftRadiusUnionType.of(toPx(Math.max(radii.getBottomLeftHorizontalRadius(), radii.getBottomLeftVerticalRadius())));
        }
    }

    private static String toCssBorder(Paint stroke, BorderStrokeStyle style, double width, boolean isPercentage) {
        return toCssBorder(stroke, style, width, isPercentage, new StringBuilder()).toString();
    }

    private static StringBuilder toCssBorder(Paint stroke, BorderStrokeStyle style, double width, boolean isPercentage, StringBuilder sb) {
        return sb.append(width).append(isPercentage ? "% " : "px ").append(toCssBorderStyle(style)).append(' ').append(HtmlPaints.toCssPaint(stroke, DomType.HTML));
    }

    private static String toCssBorderStyle(BorderStrokeStyle style) {
        if (style == BorderStrokeStyle.SOLID)
            return "solid";
        if (style == BorderStrokeStyle.DOTTED)
            return "dotted";
        if (style == BorderStrokeStyle.DASHED)
            return "dashed";
        if (style == BorderStrokeStyle.NONE)
            return "none";
        return null;
    }

    private static String toCssBackground(Background bg) {
        return bg == null ? null : toCssBackground(bg, new StringBuilder()).toString();
    }

    private static StringBuilder toCssBackground(Background bg, StringBuilder sb) {
        List<BackgroundFill> fills = bg.getFills();
        List<BackgroundImage> images = bg.getImages();
        int n = Math.max(Collections.size(fills), Collections.size(images));
        for (int i = n - 1; i >= 0; i--) { // listed in reverse order in HTML/CSS
            if (i < n - 1)
                sb.append(',');
            BackgroundFill bf = Collections.get(fills, i);
            if (bf != null)
                sb.append(' ').append(HtmlPaints.toCssPaint(bf.getFill(), DomType.HTML));
            BackgroundImage bi = Collections.get(images, i);
            if (bi != null) {
                sb.append("url(").append(bi.getImage().getUrl()).append(")");
                toCssBackgroundPositionSize(bi.getPosition(), bi.getSize(), sb);
                toCssBackgroundRepeat(bi.getRepeatX(), bi.getRepeatY(), sb);
            }
        }
        return sb;
    }

    private static StringBuilder toCssBackgroundRepeat(BackgroundRepeat repeatX, BackgroundRepeat repeatY, StringBuilder sb) {
        if (repeatX == repeatY && repeatX != null) {
            switch (repeatX) {
                case NO_REPEAT:
                    return sb.append(" no-repeat");
                case ROUND:
                case SPACE:
                case REPEAT:
                    return sb.append(" repeat");
            }
        } else if (repeatX == BackgroundRepeat.REPEAT)
            return sb.append(" repeat-x");
        else if (repeatY == BackgroundRepeat.REPEAT)
            return sb.append(" repeat-y");
        return sb;
    }

    private static StringBuilder toCssBackgroundPositionSize(BackgroundPosition position, BackgroundSize size, StringBuilder sb) {
        boolean isContain = size != null && size.isContain();
        boolean isCover = size != null && size.isCover();
        if (position != null) {
            Side horizontalSide = position.getHorizontalSide();
            if (horizontalSide != null && isCover) {
                sb.append(' ').append(horizontalSide.toString().toLowerCase());
            } else {
                double hPos = position.getHorizontalPosition();
                boolean percent = position.isHorizontalAsPercentage();
                sb.append(' ').append(percent ? 100 * hPos : hPos).append(percent ? "%" : "px");
            }
            Side verticalSide = position.getVerticalSide();
            if (verticalSide != null && isCover) {
                sb.append(' ').append(verticalSide.toString().toLowerCase());
            } else {
                double vPos = position.getVerticalPosition();
                boolean percent = position.isVerticalAsPercentage();
                sb.append(' ').append(percent ? 100 * vPos : vPos).append(percent ? "%" : "px");
            }
        }
        if (isContain)
            sb.append(" / contain");
        else if (isCover) {
            sb.append(" / cover");
        } else if (size != null) {
            double width = size.getWidth();
            if (width >= 0) {
                boolean percent = size.isWidthAsPercentage();
                sb.append(" / ").append(percent ? 100 * width : width).append(percent ? "%" : "px");
            }
            double height = size.getHeight();
            if (height >= 0) {
                boolean percent = size.isHeightAsPercentage();
                sb.append(" / ").append(percent ? 100 * height : height).append(percent ? "%" : "px");
            }
        }
        return sb;
    }
}
