package naga.toolkit.providers.gwt;

import com.google.gwt.user.cellview.client.CellTable;
import naga.platform.spi.Platform;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.charts.*;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.providers.gwt.nodes.charts.*;
import naga.toolkit.providers.gwt.nodes.controls.GwtButton;
import naga.toolkit.providers.gwt.nodes.controls.GwtSearchBox;
import naga.toolkit.providers.gwt.nodes.controls.GwtTable;
import naga.toolkit.providers.gwt.nodes.gauges.GwtGauge;
import naga.toolkit.providers.gwt.nodes.layouts.GwtVBox;
import naga.toolkit.providers.gwt.nodes.controls.GwtCheckBox;
import naga.toolkit.providers.gwt.nodes.layouts.GwtVPage;
import naga.toolkit.providers.gwt.nodes.layouts.GwtWindow;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class GwtToolkit extends Toolkit {

    public GwtToolkit() {
        super(Platform.get().scheduler(), GwtWindow::new);
        registerNodeFactory(VPage.class, GwtVPage::new);
        registerNodeFactory(VBox.class, GwtVBox::new);
        registerNodeFactoryAndWrapper(Table.class, GwtTable::new, CellTable.class, GwtTable::new);
        registerNodeFactory(CheckBox.class, GwtCheckBox::new);
        registerNodeFactory(Button.class, GwtButton::new);
        registerNodeFactory(ToggleSwitch.class, GwtCheckBox::new);
        registerNodeFactory(SearchBox.class, GwtSearchBox::new);
        registerNodeFactory(LineChart.class, GwtLineChart::new);
        registerNodeFactory(AreaChart.class, GwtAreaChart::new);
        registerNodeFactory(BarChart.class, GwtBarChart::new);
        registerNodeFactory(ScatterChart.class, GwtScatterChart::new);
        registerNodeFactory(PieChart.class, GwtPieChart::new);
        registerNodeFactory(Gauge.class, GwtGauge::new);
    }
}