package naga.fx.spi.gwt.html.peer;

import elemental2.CSSStyleDeclaration;
import elemental2.HTMLElement;
import naga.commons.util.collection.Collections;
import naga.fx.spi.gwt.util.DomType;
import naga.fx.spi.gwt.util.HtmlPaints;
import emul.javafx.scene.layout.*;
import emul.javafx.scene.paint.Paint;
import naga.fx.spi.peer.base.RegionPeerBase;
import naga.fx.spi.peer.base.RegionPeerMixin;

import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class HtmlRegionPeer
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends HtmlNodePeer<N, NB, NM>
        implements RegionPeerMixin<N, NB, NM> {

    HtmlRegionPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateWidth(Double width) {
        getElement().style.width = toPx(width);
    }

    @Override
    public void updateHeight(Double height) {
        getElement().style.height = toPx(height);
    }

    @Override
    public void updateBackground(Background background) {
        CSSStyleDeclaration style = getElement().style;
        style.background = toCssBackground(background);
        // Temporary code for corner radii that considers only the first one
        BackgroundFill firstFill = background == null ? null : Collections.get(background.getFills(), 0);
        CornerRadii radii = firstFill == null ? null : firstFill.getRadii();
        if (radii == null)
            style.border = null;
        else
            applyBorderRadii(radii);
    }

    @Override
    public void updateBorder(Border border) {
        BorderStroke firstStroke = border == null ? null : Collections.get(border.getStrokes(), 0);
        if (firstStroke != null) {
            CSSStyleDeclaration style = getElement().style;
            BorderWidths widths = firstStroke.getWidths();
            style.borderLeft = toCssBorder(firstStroke.getLeftStroke(), firstStroke.getLeftStyle(), widths.getLeft(), widths.isLeftAsPercentage());
            style.borderTop = toCssBorder(firstStroke.getTopStroke(), firstStroke.getTopStyle(), widths.getTop(), widths.isTopAsPercentage());
            style.borderRight = toCssBorder(firstStroke.getRightStroke(), firstStroke.getRightStyle(), widths.getRight(), widths.isRightAsPercentage());
            style.borderBottom = toCssBorder(firstStroke.getBottomStroke(), firstStroke.getBottomStyle(), widths.getBottom(), widths.isBottomAsPercentage());
            CornerRadii radii = firstStroke.getRadii();
            if (radii != null)
                applyBorderRadii(radii);
        }
    }

    private void applyBorderRadii(CornerRadii radii) {
        CSSStyleDeclaration style = getElement().style;
        style.borderTopLeftRadius = toPx(Math.max(radii.getTopLeftHorizontalRadius(), radii.getTopLeftVerticalRadius()));
        style.borderTopRightRadius = toPx(Math.max(radii.getTopRightHorizontalRadius(), radii.getTopRightVerticalRadius()));
        style.borderBottomRightRadius = toPx(Math.max(radii.getBottomRightHorizontalRadius(), radii.getBottomRightVerticalRadius()));
        style.borderBottomLeftRadius = toPx(Math.max(radii.getBottomLeftHorizontalRadius(), radii.getBottomLeftVerticalRadius()));
    }

    private static String toCssBorder(Paint stroke, BorderStrokeStyle style, double width, boolean isPercentage) {
        return toCssBorder(stroke, style, width, isPercentage, new StringBuilder()).toString();
    }

    private static StringBuilder toCssBorder(Paint stroke, BorderStrokeStyle style, double width, boolean isPercentage, StringBuilder sb) {
        return sb.append(width).append(isPercentage ? "% " : "px ").append("solid ").append(HtmlPaints.toCssPaint(stroke, DomType.HTML));
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
                sb.append("url(" + bi.getImage().getUrl() + ")");
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
