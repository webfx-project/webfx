package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import elemental2.dom.HTMLOptionElement;
import elemental2.dom.HTMLSelectElement;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ChoiceBox;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ChoiceBoxPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ChoiceBoxPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlMeasurableNoGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public final class HtmlChoiceBoxPeer
        <T, N extends ChoiceBox<T>, NB extends ChoiceBoxPeerBase<T, N, NB, NM>, NM extends ChoiceBoxPeerMixin<T, N, NB, NM>>

        extends HtmlControlPeer<N, NB, NM>
        implements ChoiceBoxPeerMixin<T, N, NB, NM>, HtmlMeasurableNoGrow {

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
