package naga.core.ngui.routing;

import naga.core.ngui.presentationmodel.PresentationModel;

/**
 * @author Bruno Salmon
 */
public class UiState {

    private boolean built;
    private PresentationModel presentationModel;
    private boolean uiBoundToPresentationModel;
    private boolean logicBoundToPresentationModel;
    //private final Map<String, Object> data = new HashMap<>();

    public UiState setUiBuilt(boolean built) {
        this.built = built;
        return this;
    }

    public boolean isUiBuilt() {
        return built;
    }

    public UiState setPresentationModel(PresentationModel presentationModel) {
        this.presentationModel = presentationModel;
        return this;
    }

    public PresentationModel presentationModel() {
        return presentationModel;
    }

    public UiState setUiBoundToPresentationModel(boolean bound) {
        uiBoundToPresentationModel = bound;
        return this;
    }

    public boolean isUiBoundToPresentationModel() {
        return uiBoundToPresentationModel;
    }

    public UiState setLogicBoundToPresentationModel(boolean bound) {
        logicBoundToPresentationModel = bound;
        return this;
    }

    public boolean isLogicBoundToPresentationModel() {
        return logicBoundToPresentationModel;
    }


    /*
    UiState put(String key, Object obj);

    <T> T get(String key);

    Map<String, Object> data();
    */
}
