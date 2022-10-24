package javafx.scene.media;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.scene.Node;
import dev.webfx.kit.registry.javafxmedia.JavaFxMediaRegistry;

/**
 * @author Bruno Salmon
 */
public class MediaView extends Node {

    private final MediaPlayer mediaPlayer;

    public MediaView(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        // We create and bind the node peer right now, so that GwtMediaPlayerPeer.setMediaElement() is called straightaway.
        // Then if the uer calls mediaPlayer.play() before the node is inserted in the scene graph, then it will correctly
        // propagate this call to the media element.
        getOrCreateAndBindNodePeer();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return bounds;
    }

    static {
        JavaFxMediaRegistry.registerMediaView();
    }
}
