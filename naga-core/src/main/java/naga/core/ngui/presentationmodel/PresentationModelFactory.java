package naga.core.ngui.presentationmodel;

/**
 * @author Bruno Salmon
 */
public interface PresentationModelFactory<PM extends PresentationModel> {

    PM createPresentationModel();
}
