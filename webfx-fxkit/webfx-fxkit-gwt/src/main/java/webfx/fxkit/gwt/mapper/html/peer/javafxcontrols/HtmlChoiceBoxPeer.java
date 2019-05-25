package webfx.fxkit.gwt.mapper.html.peer.javafxcontrols;

import elemental2.dom.HTMLOptionElement;
import elemental2.dom.HTMLSelectElement;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ChoiceBox;
import webfx.fxkit.gwt.mapper.html.peer.HtmlLayoutMeasurableNoGrow;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.ChoiceBoxPeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.ChoiceBoxPeerMixin;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public final class HtmlChoiceBoxPeer
        <T, N extends ChoiceBox<T>, NB extends ChoiceBoxPeerBase<T, N, NB, NM>, NM extends ChoiceBoxPeerMixin<T, N, NB, NM>>

        extends HtmlControlPeer<N, NB, NM>
        implements ChoiceBoxPeerMixin<T, N, NB, NM>, HtmlLayoutMeasurableNoGrow {

    private final HTMLSelectElement select;

    public HtmlChoiceBoxPeer() {
        this((NB) new ChoiceBoxPeerBase());
    }

    public HtmlChoiceBoxPeer(NB base) {
        super(base, HtmlUtil.createSelectElement());
        select = (HTMLSelectElement) getElement();
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        select.onchange = e -> {
            node.getSelectionModel().select(select.selectedIndex);
            return null;
        };
    }

    @Override
    public void updateItems(List<T> items, ListChangeListener.Change<T> change) {
        HtmlUtil.setChildren(select, items.stream().map(this::createOptionElement).collect(Collectors.toList()));
        select.selectedIndex = getNode().getSelectionModel().getSelectedIndex();
    }

    private HTMLOptionElement createOptionElement(T item) {
        HTMLOptionElement option = HtmlUtil.createOptionElement();
        option.text = getNode().getConverter().toString(item);
        return option;
    }
}
