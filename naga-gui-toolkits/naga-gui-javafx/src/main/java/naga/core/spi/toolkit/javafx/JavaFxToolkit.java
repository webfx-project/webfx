package naga.core.spi.toolkit.javafx;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.javafx.nodes.*;
import naga.core.spi.toolkit.nodes.*;
import naga.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class JavaFxToolkit extends Toolkit {

    public JavaFxToolkit() {
        this(() -> FxApplication.applicationWindow = new FxWindow(FxApplication.primaryStage));
    }

    protected JavaFxToolkit(Factory<Window> windowFactory) {
        super(FxScheduler.SINGLETON, windowFactory);
        new Thread(() -> Application.launch(FxApplication.class)).start();
        registerNodeFactoryAndWrapper(Table.class, FxTable::new, TableView.class, FxTable::new);
        registerNodeFactoryAndWrapper(CheckBox.class, FxCheckBox::new, javafx.scene.control.CheckBox.class, FxCheckBox::new);
        registerNodeFactoryAndWrapper(ToggleSwitch.class, FxToggleSwitch::new, naga.core.spi.toolkit.javafx.controlsfx.ToggleSwitch.class, FxToggleSwitch::new);
        registerNodeFactoryAndWrapper(TextField.class, FxTextField::new, javafx.scene.control.TextField.class, FxTextField::new);
        registerNodeFactoryAndWrapper(Button.class, FxButton::new, javafx.scene.control.Button.class, FxButton::new);
        registerNodeFactory(SearchBox.class, FxSearchBox::new);
        registerNodeFactory(BorderPane.class, FxBorderPane::new);
        registerNodeFactory(VBox.class, FxVBox::new);
        registerNodeFactory(HBox.class, FxHBox::new);
    }

    @Override
    public DisplayResultSet transformDisplayResultForGui(DisplayResultSet displayResultSet) {
        Object[] values = displayResultSet.getValues();
        ObjectProperty[] fxProperties = new ObjectProperty[values.length];
        for (int i = 0; i < values.length; i++)
            fxProperties[i] = new SimpleObjectProperty<>(values[i]);
        return new DisplayResultSet(displayResultSet.getRowCount(), fxProperties, displayResultSet.getColumns());
    }

    public static class FxApplication extends Application {
        public static Stage primaryStage;
        public static FxWindow applicationWindow;
        @Override
        public void start(Stage primaryStage) throws Exception {
            FxApplication.primaryStage = primaryStage;
            primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
            if (applicationWindow != null)
                applicationWindow.setStage(primaryStage);
        }
    }

}
