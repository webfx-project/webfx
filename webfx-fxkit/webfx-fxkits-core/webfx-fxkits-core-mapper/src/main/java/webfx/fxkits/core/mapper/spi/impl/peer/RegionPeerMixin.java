package webfx.fxkits.core.mapper.spi.impl.peer;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public interface RegionPeerMixin
        <N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends NodePeerMixin<N, NB, NM> {

    void updateWidth(Number width);

    void updateHeight(Number height);

    void updateBackground(Background background);

    void updateBorder(Border border);

    void updatePadding(Insets padding);
}
