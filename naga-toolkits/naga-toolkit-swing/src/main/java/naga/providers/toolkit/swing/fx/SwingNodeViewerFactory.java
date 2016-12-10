package naga.providers.toolkit.swing.fx;

import naga.providers.toolkit.swing.fx.viewer.*;
import naga.toolkit.fx.ext.chart.impl.LineChartImpl;
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
class SwingNodeViewerFactory extends NodeViewerFactoryImpl {

    final static SwingNodeViewerFactory SINGLETON = new SwingNodeViewerFactory();

    private SwingNodeViewerFactory() {
        registerNodeViewerFactory(RectangleImpl.class, SwingRectangleViewer::new);
        registerNodeViewerFactory(CircleImpl.class, SwingCircleViewer::new);
        registerNodeViewerFactory(TextImpl.class, SwingTextViewer::new);
        registerNodeViewerFactory(EmbedGuiNodeImpl.class, SwingEmbedGuiNodeViewer::new);
        registerNodeViewerFactory(GroupImpl.class, SwingGroupViewer::new);
        registerNodeViewerFactory(VBoxImpl.class, SwingLayoutViewer::new);
        registerNodeViewerFactory(HBoxImpl.class, SwingLayoutViewer::new);
        registerNodeViewerFactory(BorderPaneImpl.class, SwingLayoutViewer::new);
        registerNodeViewerFactory(FlowPaneImpl.class, SwingLayoutViewer::new);
        registerNodeViewerFactory(ButtonImpl.class, SwingButtonViewer::new);
        registerNodeViewerFactory(TextFieldImpl.class, SwingTextFieldViewer::new);
        registerNodeViewerFactory(CheckBoxImpl.class, SwingCheckBoxViewer::new);
        registerNodeViewerFactory(SliderImpl.class, SwingSliderViewer::new);
        registerNodeViewerFactory(ImageViewImpl.class, SwingImageViewViewer::new);
        registerNodeViewerFactory(DataGridImpl.class, SwingDataGridViewer::new);
        registerNodeViewerFactory(LineChartImpl.class, SwingLineChartViewer::new);
    }
}
