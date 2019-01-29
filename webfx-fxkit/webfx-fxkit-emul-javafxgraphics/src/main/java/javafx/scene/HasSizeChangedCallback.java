package javafx.scene;

/**
 * @author Bruno Salmon
 */
public interface HasSizeChangedCallback {

    void setSizeChangedCallback(Runnable sizeChangedCallback); // Ex: when the peer skin has changed

}
