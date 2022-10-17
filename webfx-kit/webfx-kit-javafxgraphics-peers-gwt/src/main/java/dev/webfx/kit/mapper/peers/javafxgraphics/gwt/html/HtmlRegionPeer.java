package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RegionPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.DomType;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import javafx.geometry.Insets;
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

    protected boolean subtractCssPaddingBorderWhenUpdatingSize;
    protected boolean subtractNodePaddingWhenUpdatingSize;
    protected boolean subtractNodeBorderWhenUpdatingSize;

    protected HtmlRegionPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateWidth(Number width) {
        double w = width.doubleValue();
        if (w >= 0) {
            if (subtractCssPaddingBorderWhenUpdatingSize)
                w -= computeCssPaddingBorderWidth();
            else {
                if (subtractNodePaddingWhenUpdatingSize)
                    w -= getNodePaddingWidth();
                if (subtractNodeBorderWhenUpdatingSize)
                    w -= getNodeBorderWidth();
            }
            getElement().style.width = CSSProperties.WidthUnionType.of(toPx(w));
            clearLayoutCache();
        }
    }

    private double computeCssPaddingBorderWidth() {
        CSSStyleDeclaration cs = HtmlUtil.getComputedStyle(getElement());
        return sumPx(cs.paddingLeft, cs.paddingRight, cs.borderLeftWidth, cs.borderRightWidth);
    }

    private static double sumPx(Object... values) {
        double result = 0;
        for (Object value : values) {
            String s = value.toString();
            int i = s.indexOf("px");
            if (i > 0)
                result += Double.parseDouble(s.substring(0, i));
        }
        return result;
    }

    private double getNodePaddingWidth() {
        N node = getNode();
        Insets padding = node.getPadding();
        return padding == null ? 0 : padding.getLeft() + padding.getRight();
    }

    private double getNodeBorderWidth() {
        N node = getNode();
        Border border = node.getBorder();
        if (border != null) {
            Insets insets = border.getInsets();
            if (insets != null)
                return insets.getLeft() + insets.getRight();
        }
        return 0;
    }

    @Override
    public void updateHeight(Number height) {
        double h = height.doubleValue();
        if (h >= 0) {
            if (subtractCssPaddingBorderWhenUpdatingSize)
                h -= computeCssPaddingBorderHeight();
            else {
                if (subtractNodePaddingWhenUpdatingSize)
                    h -= getNodePaddingHeight();
                if (subtractNodeBorderWhenUpdatingSize)
                    h -= getNodeBorderHeight();
            }
            getElement().style.height = CSSProperties.HeightUnionType.of(toPx(h));
            clearLayoutCache();
        }
    }

    private double computeCssPaddingBorderHeight() {
        CSSStyleDeclaration cs = HtmlUtil.getComputedStyle(getElement());
        return sumPx(cs.paddingTop, cs.paddingBottom, cs.borderTopWidth, cs.borderBottomWidth);
    }

    private double getNodePaddingHeight() {
        N node = getNode();
        Insets padding = node.getPadding();
        return padding == null ? 0 : padding.getTop() + padding.getBottom();
    }

    private double getNodeBorderHeight() {
        N node = getNode();
        Border border = node.getBorder();
        if (border != null) {
            Insets insets = border.getInsets();
            if (insets != null)
                return insets.getTop() + insets.getBottom();
        }
        return 0;
    }

    private void updateWidthAndHeight() {
        updateWidth(getNode().getWidth());
        updateHeight(getNode().getHeight());
    }

    @Override
    public void updateBackground(Background background) {
        CSSStyleDeclaration style = getElement().style;
        style.background = toCssBackground(background);
        // Temporary code for corner radii that considers only the first one
        if (getNode().getBorder() == null) { // Doesn't reset border from background if there is border property set
            BackgroundFill firstFill = background == null ? null : Collections.get(background.getFills(), 0);
            CornerRadii radii = firstFill == null ? null : firstFill.getRadii();
            if (radii == null)
                style.border = null;
            applyBorderRadii(radii);
        }
    }

    @Override
    public void updateBorder(Border border) {
        BorderStroke firstStroke = border == null ? null : Collections.get(border.getStrokes(), 0);
        CSSStyleDeclaration style = getElement().style;
        if (firstStroke != null) {
            BorderWidths widths = firstStroke.getWidths();
            style.borderLeft = toCssBorder(firstStroke.getLeftStroke(), firstStroke.getLeftStyle(), widths.getLeft(), widths.isLeftAsPercentage());
            style.borderTop = toCssBorder(firstStroke.getTopStroke(), firstStroke.getTopStyle(), widths.getTop(), widths.isTopAsPercentage());
            style.borderRight = toCssBorder(firstStroke.getRightStroke(), firstStroke.getRightStyle(), widths.getRight(), widths.isRightAsPercentage());
            style.borderBottom = toCssBorder(firstStroke.getBottomStroke(), firstStroke.getBottomStyle(), widths.getBottom(), widths.isBottomAsPercentage());
            applyBorderRadii(firstStroke.getRadii());
        } else if (getNode().getBackground() == null) { // Avoiding erasing the possible border associated with background
            style.borderLeft = style.borderTop = style.borderRight = style.borderBottom = null;
            applyBorderRadii(null);
        }
        if (subtractCssPaddingBorderWhenUpdatingSize || subtractNodeBorderWhenUpdatingSize)
            updateWidthAndHeight();
    }

    @Override
    public void updatePadding(Insets padding) {
        // Note: this code should be executed only if the html content is managed by the peer, otherwise it should be
        // skipped (ex: css padding not needed for layout peers or controls with content provided by skin)
        getElement().style.padding = toCssPadding(padding);
        if (subtractCssPaddingBorderWhenUpdatingSize || subtractNodePaddingWhenUpdatingSize)
            updateWidthAndHeight();
    }

    protected CSSProperties.PaddingUnionType toCssPadding(Insets padding) {
        if (padding == null)
            return null;
        String cssPadding = toPx(padding.getTop());
        return CSSProperties.PaddingUnionType.of(cssPadding);
    }

    private void applyBorderRadii(CornerRadii radii) {
        CSSStyleDeclaration style = getElement().style;
        if (radii != null) {
            style.borderTopLeftRadius = CSSProperties.BorderTopLeftRadiusUnionType.of(toPx(Math.max(radii.getTopLeftHorizontalRadius(), radii.getTopLeftVerticalRadius())));
            style.borderTopRightRadius = CSSProperties.BorderTopRightRadiusUnionType.of(toPx(Math.max(radii.getTopRightHorizontalRadius(), radii.getTopRightVerticalRadius())));
            style.borderBottomRightRadius = CSSProperties.BorderBottomRightRadiusUnionType.of(toPx(Math.max(radii.getBottomRightHorizontalRadius(), radii.getBottomRightVerticalRadius())));
            style.borderBottomLeftRadius = CSSProperties.BorderBottomLeftRadiusUnionType.of(toPx(Math.max(radii.getBottomLeftHorizontalRadius(), radii.getBottomLeftVerticalRadius())));
        } else
            style.borderRadius = null;
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
        for (int i = 0; i < n; i++) {
            if (i > 0)
                sb.append(',');
            BackgroundFill bf = Collections.get(fills, i);
            if (bf != null)
                sb.append(' ').append(HtmlPaints.toCssPaint(bf.getFill(), DomType.HTML));
            BackgroundImage bi = Collections.get(images, i);
            if (bi != null) {
                sb.append("url(").append(bi.getImage().getUrl()).append(")");
                toCssBackgroundRepeat(bi.getRepeatX(), bi.getRepeatY(), sb);
                toCssBackgroundPosition(bi.getPosition(), sb);
            }
        }
        return sb;
    }

    private static StringBuilder toCssBackgroundRepeat(BackgroundRepeat repeatX, BackgroundRepeat repeatY, StringBuilder sb) {
        if (repeatX == repeatY && repeatX != null) {
            switch (repeatX) {
                case NO_REPEAT: return sb.append(" no-repeat");
                case ROUND:
                case SPACE:
                case REPEAT: return sb.append(" repeat");
            }
        } else if (repeatX == BackgroundRepeat.REPEAT)
            return sb.append(" repeat-x");
        else if (repeatY == BackgroundRepeat.REPEAT)
            return sb.append(" repeat-y");
        return sb;
    }

    private static StringBuilder toCssBackgroundPosition(BackgroundPosition position, StringBuilder sb) {
        if (position != null) {
            sb.append(' ').append(position.getHorizontalPosition()).append(position.isHorizontalAsPercentage() ? "%" : "px");
            sb.append(' ').append(position.getVerticalPosition()).append(position.isVerticalAsPercentage() ? "%" : "px");
        }
        return sb;
    }
}
