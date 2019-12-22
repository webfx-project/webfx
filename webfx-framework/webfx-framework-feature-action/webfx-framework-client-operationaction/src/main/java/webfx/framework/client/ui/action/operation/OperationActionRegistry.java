package webfx.framework.client.ui.action.operation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import webfx.framework.client.ui.action.Action;
import webfx.framework.client.ui.action.ActionBinder;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.services.authz.mixin.AuthorizationUtil;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.async.AsyncFunction;
import webfx.platform.shared.util.async.Future;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class is a registry for operation actions, more accurately, for their graphical properties (text, graphic,
 * disabled and visible properties). It allows a complete code separation between the action handling declaration from
 * one side and the graphical properties declaration from the other side.
 * For example, from one side the action handling can be declared typically as follow:
 *      OperationAction myOperationAction = newOperationAction(() -> new MyOperationRequest(myArguments));
 * This code just want an action executing myOperationRequest without telling how this action appear in the user interface.
 * From the other side (another part of the application, usually the initialization code), its graphical properties can
 * be declared and registered typically as follow:
 *      Action myGraphicalAction = newAction(...);
 *      OperationActionRegistry.getInstance().registerOperationGraphicalAction(MyOperationRequest.class, registerOperationGraphicalAction);
 * or if MyOperationRequest implements HasOperationCode:
 *      OperationActionRegistry.getInstance().registerOperationGraphicalAction(myOperationCode, registerOperationGraphicalAction)
 * In this second code, graphical properties can be read from a file or DB listing all operations and bound to I18n. In
 * this case, none of the graphical properties are hardcoded, they are completely dynamic.
 * When both sides have been executed, myOperationAction used in the first code is graphically displayed as myGraphicalAction.
 *
 * @author Bruno Salmon
 */
public final class OperationActionRegistry {

    private static OperationActionRegistry INSTANCE;

    private final Map<Object /* key = request class or operation code */, Action> graphicalActions = new HashMap<>();
    private Collection<OperationAction> notYetBoundOperationActions;
    private Map<Object, ObjectProperty<OperationAction>> operationActionProperties;
    private Runnable pendingBindRunnable;
    private Consumer<OperationAction> operationActionGraphicalPropertiesUpdater;

    public static OperationActionRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new OperationActionRegistry();
        return INSTANCE;
    }

    /**
     * This method should be used when creating a graphical action for an operation that is not public but requires an
     * authorization. It will return an observable boolean value indicating if the operation is authorized or not
     * (reacting to the user principal change). Needs to be considered when setting up the disabled and visible properties.
     * @param operationCode
     * @param userPrincipalProperty
     * @param authorizationFunction
     * @return
     */

    public ObservableBooleanValue authorizedOperationActionProperty(Object operationCode, ObservableValue userPrincipalProperty, AsyncFunction<Object, Boolean> authorizationFunction) {
        // Note: it's possible we don't know yet what operation action we are talking about at this stage, because this
        // method can (and usually is) called before the operation action associated with that code is registered
        Function<OperationAction, Object> operationRequestFactory = this::newOperationActionRequest; // Will return null until the operation action with that code is registered
        // We embed the authorization function to handle the special case where the request is null
        AsyncFunction<Object, Boolean> embedAuthorizationFunction = new AsyncFunction<Object, Boolean>() { // Using a lambda expression here causes a wrong GWT code factorization which lead to an application crash! Keeping the Java 7 style solves the problem.
            @Override
            public Future<Boolean> apply(Object request) {
                if (request != null)
                    return authorizationFunction.apply(request);
                // If the request is null, this is because no operation action with that code has yet been registered, so we
                // don't know what operation it is yet, so we return not authorized by default (if this action is shown in a
                // button, the button will be invisible (or at least disabled) until the operation action is registered
                return Future.succeededFuture(false);
            }
        };
        return AuthorizationUtil.authorizedOperationProperty(
                operationRequestFactory
                , embedAuthorizationFunction
                , operationActionProperty(operationCode) // reactive property (will change when operation action will be registered, causing a new authorization evaluation)
                , userPrincipalProperty);
    }

    public <A> OperationActionRegistry registerOperationGraphicalAction(Class<A> operationRequestClass, Action graphicalAction) {
        graphicalActions.put(operationRequestClass, graphicalAction);
        return checkPendingOperationActionGraphicalBindings();
    }

    public OperationActionRegistry registerOperationGraphicalAction(Object operationCode, Action graphicalAction) {
        graphicalActions.put(operationCode, graphicalAction);
        return checkPendingOperationActionGraphicalBindings();
    }

    private OperationActionRegistry checkPendingOperationActionGraphicalBindings() {
        if (notYetBoundOperationActions != null && pendingBindRunnable == null) {
            UiScheduler.scheduleDeferred(pendingBindRunnable = () -> {
                Collection<OperationAction> operationActionsToBind =  notYetBoundOperationActions;
                pendingBindRunnable = null;
                notYetBoundOperationActions = null;
                for (OperationAction operationAction : operationActionsToBind)
                    bindOperationActionGraphicalProperties(operationAction);
            });
        }
        return this;
    }

    <A, R> void bindOperationActionGraphicalProperties(OperationAction<A, R> operationAction) {
        if (bindOperationActionGraphicalPropertiesNow(operationAction))
            return;
        if (notYetBoundOperationActions == null)
            notYetBoundOperationActions = new ArrayList<>();
        notYetBoundOperationActions.add(operationAction);
    }

    private <A, R> boolean bindOperationActionGraphicalPropertiesNow(OperationAction<A, R> operationAction) {
        // The binding is possible only if a graphical action has been registered for that operation
        // Instantiating an operation request just to have the request class or operation code
        A operationRequest = newOperationActionRequest(operationAction);
        // Then getting the graphical action from it
        Action graphicalAction = getGraphicalActionFromOperationRequest(operationRequest);
        // If this is not the case, we return false (can't do the binding now)
        if (graphicalAction == null)
            return false;
        if (operationActionProperties != null) {
            Object code = getOperationCodeFromGraphicalAction(graphicalAction);
            if (code != null) {
                ObjectProperty<OperationAction> operationActionProperty = operationActionProperties.remove(code);
                if (operationActionProperty != null)
                    operationActionProperty.set(operationAction);
                if (operationActionProperties.isEmpty())
                    operationActionProperties = null;
            }
        }
        ActionBinder.bindWritableActionToAction(operationAction, graphicalAction);
        return true;
    }

    public <A, R> Action getGraphicalActionFromOperationAction(OperationAction<A, R> operationAction) {
        // Instantiating an operation request just to have the request class or operation code
        A operationRequest = newOperationActionRequest(operationAction);
        // Then getting the graphical action from it
        return getGraphicalActionFromOperationRequest(operationRequest);
    }

    public Action getGraphicalActionFromOperationRequest(Object operationRequest) {
        // Trying to get the operation action registered with the operation request class or code.
        Action graphicalAction = getGraphicalActionFromOperationRequestClass(operationRequest.getClass());
        if (graphicalAction == null && operationRequest instanceof HasOperationCode)
            graphicalAction = getGraphicalActionFromOperationCode(((HasOperationCode) operationRequest).getOperationCode());
        return graphicalAction;
    }

    private Action getGraphicalActionFromOperationRequestClass(Class operationRequestClass) {
        return graphicalActions.get(operationRequestClass);
    }

    private Action getGraphicalActionFromOperationCode(Object operationCode) {
        return graphicalActions.get(operationCode);
    }

    private Object getOperationCodeFromGraphicalAction(Action graphicalAction) {
        for (Map.Entry<Object, Action> entry : graphicalActions.entrySet()) {
            if (entry.getValue() == graphicalAction)
                return entry.getKey();
        }
        return null;
    }

    public void setOperationActionGraphicalPropertiesUpdater(Consumer<OperationAction> operationActionGraphicalPropertiesUpdater) {
        this.operationActionGraphicalPropertiesUpdater = operationActionGraphicalPropertiesUpdater;
    }

    <A, R> void updateOperationActionGraphicalProperties(OperationAction<A, R> operationAction) {
        if (operationActionGraphicalPropertiesUpdater != null)
            operationActionGraphicalPropertiesUpdater.accept(operationAction);
    }

    private ObservableValue<OperationAction> operationActionProperty(Object operationCode) {
        ObjectProperty<OperationAction> operationActionProperty = new SimpleObjectProperty<>();
        if (operationActionProperties == null)
            operationActionProperties = new HashMap<>();
        operationActionProperties.put(operationCode, operationActionProperty);
        return operationActionProperty;
    }

    public <A, R> A newOperationActionRequest(OperationAction<A, R> operationAction) {
        if (operationAction == null)
            return null;
        Function<ActionEvent, A> operationRequestFactory = operationAction.getOperationRequestFactory();
        if (operationRequestFactory != null)
            return operationRequestFactory.apply(new ActionEvent());
        return null;
    }

}
