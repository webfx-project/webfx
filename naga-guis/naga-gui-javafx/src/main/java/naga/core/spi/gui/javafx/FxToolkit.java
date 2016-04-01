package naga.core.spi.gui.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.javafx.nodes.*;
import naga.core.spi.gui.nodes.*;
import naga.core.spi.platform.Scheduler;

/**
 * @author Bruno Salmon
 */
public class FxToolkit extends GuiToolkit {

    protected Stage primaryStage;

    public FxToolkit() {
        registerNodeFactory(Table.class, FxTable::new);
        registerNodeFactory(CheckBox.class, FxCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, FxToggleSwitch::new);
        registerNodeFactory(BorderPane.class, FxBorderPane::new);
        registerNodeFactory(TextField.class, FxTextField::new);
        registerNodeFactory(SearchBox.class, FxSearchBox::new);
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
    }

    private GuiNode dontGarbageRootNode; // keeping reference to avoid garbage collection
    @Override
    public void displayRootNode(GuiNode rootNode) {
        this.dontGarbageRootNode = rootNode;
        Scene scene = createScene((Parent) rootNode.unwrapToToolkitNode(), 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    protected Scene createScene(Parent root, double width, double height) {
        return new Scene(root, width, height); //, Color.valueOf("#2F343A"));
    }

    public static FxToolkit get() {
        return (FxToolkit) GuiToolkit.get();
    }

    @Override
    public Scheduler scheduler() {
        return FxScheduler.SINGLETON;
    }


    @Override
    public DisplayResult transformDisplayResultForGui(DisplayResult displayResult) {
        Object[] values = displayResult.getValues();
        ObjectProperty[] fxProperties = new ObjectProperty[values.length];
        for (int i = 0; i < values.length; i++)
            fxProperties[i] = new SimpleObjectProperty<>(values[i]);
        return new DisplayResult(displayResult.getRowCount(), fxProperties, displayResult.getColumnTypes(), displayResult.getHeaderValues(), displayResult.getHeaderType());
    }
}
