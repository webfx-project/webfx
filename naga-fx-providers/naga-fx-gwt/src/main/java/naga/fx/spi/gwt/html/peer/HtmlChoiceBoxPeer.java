package naga.fx.spi.gwt.html.peer;

import elemental2.dom.HTMLOptionElement;
import elemental2.dom.HTMLSelectElement;
import emul.javafx.scene.control.ChoiceBox;
import naga.fx.scene.SceneRequester;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.ChoiceBoxPeerBase;
import naga.fx.spi.peer.base.ChoiceBoxPeerMixin;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public class HtmlChoiceBoxPeer
        <T, N extends ChoiceBox<T>, NB extends ChoiceBoxPeerBase<T, N, NB, NM>, NM extends ChoiceBoxPeerMixin<T, N, NB, NM>>

        extends HtmlControlPeer<N, NB, NM>
        implements ChoiceBoxPeerMixin<T, N, NB, NM> {

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
            node.getSelectionModel().select((int) select.selectedIndex);
            return null;
        };
    }

    @Override
    public void updateItems(List<T> items) {
        HtmlUtil.setChildren(select, items.stream().map(this::createOptionElement).collect(Collectors.toList()));
        select.selectedIndex = getNode().getSelectionModel().getSelectedIndex();
    }

    private HTMLOptionElement createOptionElement(T item) {
        HTMLOptionElement option = HtmlUtil.createOptionElement();
        option.text = getNode().getConverter().toString(item);
        return option;
    }
}
