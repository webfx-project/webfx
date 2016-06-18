package naga.core.activity;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.core.orm.domainmodel.DataSourceModel;
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.Toolkit;

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

    ApplicationContext(String[] mainArgs, DataSourceModel dataSourceModel) {
        super(null);
        this.mainArgs = mainArgs;
        setDataSourceModel(dataSourceModel);
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
