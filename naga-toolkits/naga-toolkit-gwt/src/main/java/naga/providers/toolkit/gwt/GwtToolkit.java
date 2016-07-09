package naga.providers.toolkit.gwt;

import com.google.gwt.user.cellview.client.CellTable;
import naga.platform.spi.Platform;
import naga.providers.toolkit.gwt.nodes.charts.*;
import naga.providers.toolkit.gwt.nodes.controls.GwtButton;
import naga.providers.toolkit.gwt.nodes.controls.GwtCheckBox;
import naga.providers.toolkit.gwt.nodes.controls.GwtSearchBox;
import naga.providers.toolkit.gwt.nodes.controls.GwtTable;
import naga.providers.toolkit.gwt.nodes.gauges.GwtGauge;
import naga.providers.toolkit.gwt.nodes.layouts.GwtVBox;
import naga.providers.toolkit.gwt.nodes.layouts.GwtVPage;
import naga.providers.toolkit.gwt.nodes.layouts.GwtWindow;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.charts.*;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class GwtToolkit extends Toolkit {

    public GwtToolkit() {
        super(/* TODO: remove this dependency to Platform */Platform.get().scheduler(), GwtWindow::new);
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