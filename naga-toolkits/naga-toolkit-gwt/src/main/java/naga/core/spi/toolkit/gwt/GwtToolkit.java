package naga.core.spi.toolkit.gwt;

import com.google.gwt.user.cellview.client.CellTable;
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.charts.AreaChart;
import naga.core.spi.toolkit.charts.BarChart;
import naga.core.spi.toolkit.charts.LineChart;
import naga.core.spi.toolkit.charts.PieChart;
import naga.core.spi.toolkit.controls.*;
import naga.core.spi.toolkit.gwt.charts.GwtAreaChart;
import naga.core.spi.toolkit.gwt.charts.GwtBarChart;
import naga.core.spi.toolkit.gwt.charts.GwtLineChart;
import naga.core.spi.toolkit.gwt.charts.GwtPieChart;
import naga.core.spi.toolkit.gwt.controls.GwtButton;
import naga.core.spi.toolkit.gwt.controls.GwtCheckBox;
import naga.core.spi.toolkit.gwt.controls.GwtSearchBox;
import naga.core.spi.toolkit.gwt.controls.GwtTable;
import naga.core.spi.toolkit.gwt.layouts.GwtVBox;
import naga.core.spi.toolkit.gwt.layouts.GwtVPage;
import naga.core.spi.toolkit.gwt.layouts.GwtWindow;
import naga.core.spi.toolkit.layouts.VBox;
import naga.core.spi.toolkit.layouts.VPage;

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
        registerNodeFactory(PieChart.class, GwtPieChart::new);
    }
}