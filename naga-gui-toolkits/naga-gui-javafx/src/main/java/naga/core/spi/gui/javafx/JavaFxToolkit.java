package naga.core.spi.gui.javafx;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.javafx.nodes.*;
import naga.core.spi.gui.nodes.*;
import naga.core.spi.platform.Scheduler;

/**
 * @author Bruno Salmon
 */
public class JavaFxToolkit extends GuiToolkit {

    public JavaFxToolkit() {
        new Thread(() -> Application.launch(FxApplication.class)).start();
        registerNodeFactory(Table.class, FxTable::new);
        registerNodeFactory(CheckBox.class, FxCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, FxToggleSwitch::new);
        registerNodeFactory(BorderPane.class, FxBorderPane::new);
        registerNodeFactory(TextField.class, FxTextField::new);
        registerNodeFactory(SearchBox.class, FxSearchBox::new);
    }

    private FxWindow applicationWindow;
    @Override
    public Window getApplicationWindow() {
        if (applicationWindow == null)
            applicationWindow = FxApplication.applicationWindow = new FxWindow(FxApplication.primaryStage);
        return applicationWindow;
    }

    @Override
    public Scheduler scheduler() {
        return FxScheduler.SINGLETON;
    }

    @Override
    public DisplayResultSet transformDisplayResultForGui(DisplayResultSet displayResultSet) {
        Object[] values = displayResultSet.getValues();
        ObjectProperty[] fxProperties = new ObjectProperty[values.length];
        for (int i = 0; i < values.length; i++)
            fxProperties[i] = new SimpleObjectProperty<>(values[i]);
        return new DisplayResultSet(displayResultSet.getRowCount(), fxProperties, displayResultSet.getColumnTypes(), displayResultSet.getHeaderValues(), displayResultSet.getHeaderType());
    }

    public static class FxApplication extends Application {
        static Stage primaryStage;
        static FxWindow applicationWindow;
        @Override
        public void start(Stage primaryStage) throws Exception {
            FxApplication.primaryStage = primaryStage;
            primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
            if (applicationWindow != null)
                applicationWindow.setStage(primaryStage);
        }
    }

}
