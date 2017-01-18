package naga.fx.spi.swing.peer;

import naga.fx.spi.peer.base.LabeledPeerBase;
import naga.fx.spi.swing.util.SwingFonts;
import naga.fx.spi.swing.util.SwingPaints;
import emul.javafx.scene.control.Labeled;
import emul.javafx.scene.paint.Paint;
import emul.javafx.scene.text.Font;
import emul.javafx.scene.text.TextAlignment;
import naga.fx.spi.peer.base.LabeledPeerMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public abstract class SwingLabeledPeer
        <N extends Labeled, NB extends LabeledPeerBase<N, NB, NM>, NM extends LabeledPeerMixin<N, NB, NM>>

        extends SwingControlPeer<N, NB, NM>
        implements LabeledPeerMixin<N, NB, NM>, SwingEmbedComponentPeer<N> {


    SwingLabeledPeer(NB base) {
        super(base);
    }

    @Override
    public void updateFont(Font font) {
        getSwingComponent().setFont(SwingFonts.toSwingFont(font));
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
    }

    @Override
    public void updateTextFill(Paint textFill) {
        getSwingComponent().setForeground((Color) SwingPaints.toSwingPaint(textFill));
    }
}
