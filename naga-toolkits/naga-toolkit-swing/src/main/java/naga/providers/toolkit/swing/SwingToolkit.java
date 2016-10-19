package naga.providers.toolkit.swing;

import naga.providers.toolkit.swing.drawing.SwingDrawingNode;
import naga.providers.toolkit.swing.nodes.charts.SwingLineChart;
import naga.providers.toolkit.swing.nodes.controls.*;
import naga.providers.toolkit.swing.nodes.layouts.SwingHBox;
import naga.providers.toolkit.swing.nodes.layouts.SwingVBox;
import naga.providers.toolkit.swing.nodes.layouts.SwingVPage;
import naga.providers.toolkit.swing.nodes.layouts.SwingWindow;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.charts.LineChart;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingToolkit extends Toolkit {

    static {
        try {
            UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ignored) {}
    }

    public SwingToolkit() {
        super(SwingScheduler.SINGLETON, SwingWindow::new);
        registerNodeFactory(VPage.class, SwingVPage::new);
        registerNodeFactory(Table.class, SwingTable::new);
        registerNodeFactory(CheckBox.class, SwingCheckBox::new);
        registerNodeFactory(RadioButton.class, SwingRadioButton::new);
        registerNodeFactory(ToggleSwitch.class, SwingCheckBox::new);
        registerNodeFactory(TextView.class, SwingTextView::new);
        registerNodeFactory(SearchBox.class, SwingSearchBox::new);
        registerNodeFactory(Image.class, SwingImage::new);
        registerNodeFactoryAndWrapper(Button.class, SwingButton::new, JButton.class, SwingButton::new);
        registerNodeFactory(VBox.class, SwingVBox::new);
        registerNodeFactory(HBox.class, SwingHBox::new);
        registerNodeFactory(Slider.class, SwingSlider::new);
        registerNodeFactory(LineChart.class, SwingLineChart::new);
        registerNodeFactory(DrawingNode.class, SwingDrawingNode::new);
    }
}
