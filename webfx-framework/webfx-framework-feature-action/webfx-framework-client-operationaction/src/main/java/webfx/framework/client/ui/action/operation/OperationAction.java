package webfx.framework.client.ui.action.operation;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import webfx.framework.client.ui.action.impl.WritableAction;
import webfx.framework.shared.operation.OperationUtil;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.async.AsyncFunction;
import webfx.platform.shared.util.function.Factory;

import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public final class OperationAction<Rq, Rs> extends WritableAction {

    private final Function<ActionEvent, Rq> operationRequestFactory;
    private OperationActionRegistry operationActionRegistry = OperationActionRegistry.getInstance();

    public OperationAction(Factory<Rq> operationRequestFactory, AsyncFunction<Rq, Rs> topOperationExecutor, ObservableValue... graphicalDependencyProperties) {
        this(actionEvent -> operationRequestFactory.create(), topOperationExecutor, graphicalDependencyProperties);
    }

    public OperationAction(Function<ActionEvent, Rq> operationRequestFactory, AsyncFunction<Rq, Rs> topOperationExecutor, ObservableValue... graphicalDependencies) {
        super(actionEvent -> {
            Rq operationRequest = operationRequestFactory.apply(actionEvent);
            Logger.log("Executing " + operationRequest);
            long t0 = System.currentTimeMillis();
            OperationUtil.executeOperation(operationRequest, topOperationExecutor).setHandler(ar -> {
                if (ar.failed())
                    Logger.log("Error while executing " + operationRequest, ar.cause());
                else
                    Logger.log("Executed " + operationRequest + " in " + (System.currentTimeMillis() - t0) + "ms");
            });
        });
        this.operationRequestFactory = operationRequestFactory;
        OperationActionRegistry registry = getOperationActionRegistry();
        registry.bindOperationActionGraphicalProperties(this);
        // Also if some graphical dependencies are passed, we update the graphical properties when they change
        if (graphicalDependencies.length > 0)
            Properties.runNowAndOnPropertiesChange(() -> registry.updateOperationActionGraphicalProperties(this), graphicalDependencies);
    }

    public OperationActionRegistry getOperationActionRegistry() {
        return operationActionRegistry;
    }

    public void setOperationActionRegistry(OperationActionRegistry operationActionRegistry) {
        this.operationActionRegistry = operationActionRegistry;
    }

    public Function<ActionEvent, Rq> getOperationRequestFactory() {
        return operationRequestFactory;
    }
}
