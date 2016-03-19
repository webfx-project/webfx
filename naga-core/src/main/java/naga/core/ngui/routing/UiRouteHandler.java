package naga.core.ngui.routing;

import naga.core.ngui.presentationmodel.PresentationModel;
import naga.core.ngui.presentationmodel.PresentationModelFactory;
import naga.core.spi.platform.Platform;
import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public class UiRouteHandler implements Handler<UiRoutingContext> {

    private PresentationModelFactory presentationModelFactory;
    private Handler<UiState> uiBuilder;
    private Handler<UiState> uiToPresentationModelBinder;
    private Handler<PresentationModel> logicToPresentationModelBinder;
    private final UiState uiState = new UiState();

    public UiRouteHandler setPresentationModelFactory(PresentationModelFactory presentationModelFactory) {
        this.presentationModelFactory = presentationModelFactory;
        return this;
    }

    public UiRouteHandler setUiBuilder(Handler<UiState> uiBuilder) {
        this.uiBuilder = uiBuilder;
        return this;
    }

    public UiRouteHandler setUiToPresentationModelBinder(Handler<UiState> uiToPresentationModelBinder) {
        this.uiToPresentationModelBinder = uiToPresentationModelBinder;
        return this;
    }

    public <PM extends PresentationModel> UiRouteHandler setPresentationModelLogicBinder(Handler<PM> logicToPresentationModelBinder) {
        this.logicToPresentationModelBinder = (Handler<PresentationModel>) logicToPresentationModelBinder;
        return this;
    }

    @Override
    public void handle(UiRoutingContext context) {
        if (uiState.presentationModel() == null && presentationModelFactory != null)
            uiState.setPresentationModel(presentationModelFactory.createPresentationModel());
        if (!uiState.isLogicBoundToPresentationModel()) {
            bindLogicToPresentationModel(uiState.presentationModel());
            uiState.setLogicBoundToPresentationModel(true);
        }
        if (!uiState.isUiBuilt()) {
            buildUi();
            uiState.setUiBuilt(true);
        }
        if (!uiState.isUiBoundToPresentationModel()) {
            bindUiToPresentationModel();
            uiState.setUiBoundToPresentationModel(true);
        }
    }

    protected void buildUi() {
        if (uiBuilder != null)
            uiBuilder.handle(uiState);
    }

    protected void bindUiToPresentationModel() {
        if (uiToPresentationModelBinder != null)
            uiToPresentationModelBinder.handle(uiState);
    }

    protected void bindLogicToPresentationModel(PresentationModel presentationModel) {
        if (logicToPresentationModelBinder != null)
            Platform.runInBackground(() -> logicToPresentationModelBinder.handle(presentationModel));
    }
}
