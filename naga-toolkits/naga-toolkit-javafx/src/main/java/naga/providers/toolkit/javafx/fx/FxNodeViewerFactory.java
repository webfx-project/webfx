package naga.providers.toolkit.javafx.fx;

import naga.providers.toolkit.javafx.fx.viewer.*;
import naga.toolkit.fx.ext.chart.impl.*;
import naga.toolkit.fx.ext.impl.DataGridImpl;
import naga.toolkit.fx.scene.control.impl.ButtonImpl;
import naga.toolkit.fx.scene.control.impl.CheckBoxImpl;
import naga.toolkit.fx.scene.control.impl.SliderImpl;
import naga.toolkit.fx.scene.control.impl.TextFieldImpl;
import naga.toolkit.fx.scene.image.impl.ImageViewImpl;
import naga.toolkit.fx.scene.impl.EmbedGuiNodeImpl;
import naga.toolkit.fx.scene.impl.GroupImpl;
import naga.toolkit.fx.scene.layout.impl.BorderPaneImpl;
import naga.toolkit.fx.scene.layout.impl.FlowPaneImpl;
import naga.toolkit.fx.scene.layout.impl.HBoxImpl;
import naga.toolkit.fx.scene.layout.impl.VBoxImpl;
import naga.toolkit.fx.scene.shape.impl.CircleImpl;
import naga.toolkit.fx.scene.shape.impl.RectangleImpl;
import naga.toolkit.fx.scene.text.impl.TextImpl;
import naga.toolkit.fx.spi.impl.NodeViewerFactoryImpl;

/**
 * @author Bruno Salmon
 */
class FxNodeViewerFactory extends NodeViewerFactoryImpl {

    final static FxNodeViewerFactory SINGLETON = new FxNodeViewerFactory();

    private FxNodeViewerFactory() {
        registerNodeViewerFactory(RectangleImpl.class, FxRectangleViewer::new);
        registerNodeViewerFactory(CircleImpl.class, FxCircleViewer::new);
        registerNodeViewerFactory(TextImpl.class, FxTextViewer::new);
        registerNodeViewerFactory(EmbedGuiNodeImpl.class, FxEmbedGuiNodeViewer::new);
        registerNodeViewerFactory(GroupImpl.class, FxGroupViewer::new);
        registerNodeViewerFactory(VBoxImpl.class, FxLayoutViewer::new);
        registerNodeViewerFactory(HBoxImpl.class, FxLayoutViewer::new);
        registerNodeViewerFactory(BorderPaneImpl.class, FxLayoutViewer::new);
        registerNodeViewerFactory(FlowPaneImpl.class, FxLayoutViewer::new);
        registerNodeViewerFactory(ButtonImpl.class, FxButtonViewer::new);
        registerNodeViewerFactory(CheckBoxImpl.class, FxCheckBoxViewer::new);
        registerNodeViewerFactory(TextFieldImpl.class, FxTextFieldViewer::new);
        registerNodeViewerFactory(ImageViewImpl.class, FxImageViewViewer::new);
        registerNodeViewerFactory(SliderImpl.class, FxSliderViewer::new);
        registerNodeViewerFactory(DataGridImpl.class, FxDataGridViewer::new);
        registerNodeViewerFactory(AreaChartImpl.class, FxAreaChartViewer::new);
        registerNodeViewerFactory(BarChartImpl.class, FxBarChartViewer::new);
        registerNodeViewerFactory(LineChartImpl.class, FxLineChartViewer::new);
        registerNodeViewerFactory(PieChartImpl.class, FxPieChartViewer::new);
        registerNodeViewerFactory(ScatterChartImpl.class, FxScatterChartViewer::new);
    }
}
