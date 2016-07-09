package naga.platform.activity;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.platform.spi.Platform;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public class ApplicationContext extends ActivityContextImpl {

    /**
     *  Global static instance of the application context that any activity can access if needed.
     *  In addition, this static instance is the root object of the application and it is necessary
     *  to keep a reference to it to avoid garbage collection.
     */
    private static ApplicationContext instance;
    public static ApplicationContext get() {
        return instance;
    }

    private String[] mainArgs;

    ApplicationContext(String[] mainArgs) {
        super(null);
        this.mainArgs = mainArgs;
        setHistory(Platform.get().getBrowserHistory());
        nodeProperty().addListener(new ChangeListener<GuiNode>() {
            @Override
            public void changed(ObservableValue<? extends GuiNode> observable, GuiNode oldValue, GuiNode newValue) {
                observable.removeListener(this);
                //Platform.log("Binding application window node property");
                Toolkit.get().getApplicationWindow().nodeProperty().bind(observable);
            }
        });
        instance = this;
    }

    public String[] getMainArgs() {
        return mainArgs;
    }
}
