package naga.fx.spi.javafx.fx.viewer;

import naga.fx.spi.javafx.util.FxPaints;
import naga.fx.spi.javafx.util.FxStrokes;
import naga.fx.geometry.Insets;
import naga.fx.geometry.Side;
import naga.fx.scene.image.Image;
import naga.fx.scene.layout.*;
import naga.fx.spi.viewer.base.RegionViewerBase;
import naga.fx.spi.viewer.base.RegionViewerMixin;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
abstract class FxRegionViewer
        <FxN extends javafx.scene.layout.Region, N extends Region, NB extends RegionViewerBase<N, NB, NM>, NM extends RegionViewerMixin<N, NB, NM>>

        extends FxNodeViewer<FxN, N, NB, NM>
        implements RegionViewerMixin<N, NB, NM> {


    FxRegionViewer(NB base) {
        super(base);
/*
        FxN region = getFxNode();
        fxNode.minWidthProperty().bind(region.minWidthProperty());
        fxNode.maxWidthProperty().bind(region.maxWidthProperty());
        fxNode.minHeightProperty().bind(region.minHeightProperty());
        fxNode.maxHeightProperty().bind(region.maxHeightProperty());
        fxNode.prefWidthProperty().bind(region.prefWidthProperty());
        fxNode.prefHeightProperty().bind(region.prefHeightProperty());
*/
    }

    @Override
    public void updateWidth(Double width) {
        if (width != getFxNode().getWidth())
            callRegionSetter("setWidth", width);
    }

    @Override
    public void updateHeight(Double height) {
        if (height != getFxNode().getHeight())
            callRegionSetter("setHeight", height);
    }

    @Override
    public void updateBackground(Background background) {
        if (background != null)
            getFxNode().setBackground(toFxBackground(background));
    }

    @Override
    public void updateBorder(Border border) {
        getFxNode().setBorder(toFxBorder(border));
    }

    private static javafx.scene.layout.Background toFxBackground(Background bg) {
        return bg == null ? null : new javafx.scene.layout.Background(toFxBackgroundFills(bg.getFills()), toFxBackgroundImages(bg.getImages()));
    }

    private static List<javafx.scene.layout.BackgroundFill> toFxBackgroundFills(List<BackgroundFill> fills) {
        return fills == null ? null : fills.stream().map(FxRegionViewer::toFxBackgroundFill).collect(Collectors.toList());
    }

    private static javafx.scene.layout.BackgroundFill toFxBackgroundFill(BackgroundFill fill) {
        return new javafx.scene.layout.BackgroundFill(FxPaints.toFxPaint(fill.getFill()), toFxRadii(fill.getRadii()), toFxInsets(fill.getInsets()));
    }

    private static javafx.scene.layout.CornerRadii toFxRadii(CornerRadii radii) {
        return radii == null ? null : new javafx.scene.layout.CornerRadii(
                radii.getTopLeftHorizontalRadius(),
                radii.getTopLeftVerticalRadius(),
                radii.getTopRightVerticalRadius(),
                radii.getTopRightHorizontalRadius(),
                radii.getBottomRightHorizontalRadius(),
                radii.getBottomRightVerticalRadius(),
                radii.getBottomLeftVerticalRadius(),
                radii.getBottomLeftHorizontalRadius(),
                radii.isTopLeftHorizontalRadiusAsPercentage(),
                radii.isTopLeftVerticalRadiusAsPercentage(),
                radii.isTopRightVerticalRadiusAsPercentage(),
                radii.isTopRightHorizontalRadiusAsPercentage(),
                radii.isBottomRightHorizontalRadiusAsPercentage(),
                radii.isBottomRightVerticalRadiusAsPercentage(),
                radii.isBottomLeftVerticalRadiusAsPercentage(),
                radii.isBottomLeftHorizontalRadiusAsPercentage()
        );
    }

    private static javafx.geometry.Insets toFxInsets(Insets insets) {
        return insets == null ? null : insets == Insets.EMPTY ? javafx.geometry.Insets.EMPTY : new javafx.geometry.Insets(insets.getTop(), insets.getRight(), insets.getBottom(), insets.getLeft());
    }

    private static List<javafx.scene.layout.BackgroundImage> toFxBackgroundImages(List<BackgroundImage> images) {
        return images == null ? null : images.stream().map(FxRegionViewer::toFxBackgroundImage).collect(Collectors.toList());
    }

    private static javafx.scene.layout.BackgroundImage toFxBackgroundImage(BackgroundImage bi) {
        return new javafx.scene.layout.BackgroundImage(toFxImage(bi.getImage()), toFxBackgroundRepeat(bi.getRepeatX()), toFxBackgroundRepeat(bi.getRepeatY()), toFxBackgroundPosition(bi.getPosition()), toFxBackgroundSize(bi.getSize()));
    }

    private static javafx.scene.image.Image toFxImage(Image image) {
        return image == null ? null : new javafx.scene.image.Image(image.getUrl(), image.getRequestedWidth(), image.getRequestedHeight(), image.isPreserveRatio(), image.isSmooth());
    }

    private static javafx.scene.layout.BackgroundRepeat toFxBackgroundRepeat(BackgroundRepeat br) {
        if (br != null)
            switch (br) {
                case NO_REPEAT: return javafx.scene.layout.BackgroundRepeat.NO_REPEAT;
                case REPEAT: return javafx.scene.layout.BackgroundRepeat.REPEAT;
                case ROUND: return javafx.scene.layout.BackgroundRepeat.ROUND;
                case SPACE: return javafx.scene.layout.BackgroundRepeat.SPACE;
            }
        return null;
    }

    private static javafx.scene.layout.BackgroundPosition toFxBackgroundPosition(BackgroundPosition bp) {
        return bp == null ? null : new javafx.scene.layout.BackgroundPosition(toFxSide(bp.getHorizontalSide()), bp.getHorizontalPosition(), bp.isHorizontalAsPercentage(), toFxSide(bp.getVerticalSide()), bp.getVerticalPosition(), bp.isVerticalAsPercentage());
    }

    private static javafx.geometry.Side toFxSide(Side side) {
        switch (side) {
            case BOTTOM: return javafx.geometry.Side.BOTTOM;
            case LEFT: return javafx.geometry.Side.LEFT;
            case RIGHT: return javafx.geometry.Side.RIGHT;
            case TOP: return javafx.geometry.Side.TOP;
        }
        return null;
    }

    private static javafx.scene.layout.BackgroundSize toFxBackgroundSize(BackgroundSize bs) {
        return bs == null ? null : new javafx.scene.layout.BackgroundSize(bs.getWidth(), bs.getHeight(), bs.isWidthAsPercentage(), bs.isHeightAsPercentage(), bs.isContain(), bs.isCover());
    }

    private static javafx.scene.layout.Border toFxBorder(Border border) {
        return border == null ? null : new javafx.scene.layout.Border(toFxBorderStrokes(border.getStrokes()), toFxBorderImages(border.getImages()));
    }

    private static List<javafx.scene.layout.BorderStroke> toFxBorderStrokes(List<BorderStroke> strokes) {
        return strokes == null ? null : strokes.stream().map(FxRegionViewer::toFxBorderStroke).collect(Collectors.toList());
    }

    private static javafx.scene.layout.BorderStroke toFxBorderStroke(BorderStroke stroke) {
        return new javafx.scene.layout.BorderStroke(FxPaints.toFxPaint(stroke.getTopStroke()), FxPaints.toFxPaint(stroke.getRightStroke()), FxPaints.toFxPaint(stroke.getBottomStroke()), FxPaints.toFxPaint(stroke.getLeftStroke()),
                toFxBorderStrokeStyle(stroke.getTopStyle()), toFxBorderStrokeStyle(stroke.getRightStyle()), toFxBorderStrokeStyle(stroke.getBottomStyle()), toFxBorderStrokeStyle(stroke.getLeftStyle()),
                toFxRadii(stroke.getRadii()), toFxBorderWidths(stroke.getWidths()), toFxInsets(stroke.getInsets()));
    }

    private static javafx.scene.layout.BorderStrokeStyle toFxBorderStrokeStyle(BorderStrokeStyle bss) {
        return bss == null ? null :
                bss == BorderStrokeStyle.NONE ? javafx.scene.layout.BorderStrokeStyle.NONE :
                bss == BorderStrokeStyle.DASHED ? javafx.scene.layout.BorderStrokeStyle.DASHED :
                bss == BorderStrokeStyle.DOTTED ? javafx.scene.layout.BorderStrokeStyle.DOTTED :
                bss == BorderStrokeStyle.SOLID ? javafx.scene.layout.BorderStrokeStyle.SOLID :
                new javafx.scene.layout.BorderStrokeStyle(FxStrokes.toFxStrokeType(bss.getType()), FxStrokes.toFxStrokeLineJoin(bss.getLineJoin()), FxStrokes.toFxStrokeLineCap(bss.getLineCap()), bss.getMiterLimit(), bss.getDashOffset(), bss.getDashArray());
    }

    private static javafx.scene.layout.BorderWidths toFxBorderWidths(BorderWidths bw) {
        return bw == null ? null : new javafx.scene.layout.BorderWidths(bw.getTop(), bw.getRight(), bw.getBottom(), bw.getLeft(), bw.isTopAsPercentage(), bw.isRightAsPercentage(), bw.isBottomAsPercentage(), bw.isLeftAsPercentage());
    }

    private static List<javafx.scene.layout.BorderImage> toFxBorderImages(List<BorderImage> images) {
        return images == null ? null : images.stream().map(FxRegionViewer::toFxBorderImage).collect(Collectors.toList());
    }

    private static javafx.scene.layout.BorderImage toFxBorderImage(BorderImage bi) {
        return bi == null ? null : new javafx.scene.layout.BorderImage(toFxImage(bi.getImage()), toFxBorderWidths(bi.getWidths()), toFxInsets(bi.getInsets()), toFxBorderWidths(bi.getSlices()), bi.isFilled(), toFxBorderRepeat(bi.getRepeatX()), toFxBorderRepeat(bi.getRepeatY()));
    }

    private static javafx.scene.layout.BorderRepeat toFxBorderRepeat(BorderRepeat br) {
        if (br != null)
            switch (br) {
                case REPEAT: return javafx.scene.layout.BorderRepeat.REPEAT;
                case ROUND: return javafx.scene.layout.BorderRepeat.ROUND;
                case SPACE: return javafx.scene.layout.BorderRepeat.SPACE;
                case STRETCH: return javafx.scene.layout.BorderRepeat.STRETCH;
            }
        return null;
    }

    private void callRegionSetter(String setterName, Double value) {
        try {
            Method regionSetter = javafx.scene.layout.Region.class.getDeclaredMethod(setterName, double.class);
            regionSetter.setAccessible(true);
            try {
                regionSetter.invoke(getFxNode(), value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
