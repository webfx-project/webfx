package naga.providers.toolkit.html.fx;

import naga.providers.toolkit.html.fx.html.HtmlScene;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.impl.WindowImpl;

import static elemental2.Global.document;
import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
public class HtmlWindow extends WindowImpl {

    public HtmlWindow() {
        document.body.style.overflow = "hidden";
        setWidth(window.innerWidth);
        setHeight(window.innerHeight);
        window.onresize = a -> {
            setWidth(window.innerWidth);
            setHeight(window.innerHeight);
            return null;
        };
    }

    @Override
    protected void onTitleUpdate() {
        document.title = getTitle();
    }

    @Override
    protected void onSceneRootUpdate() {
        setWindowContent(((HtmlScene) getScene()).getSceneNode());
    }

    private void setWindowContent(elemental2.Node content) {
        //Platform.log("Setting window root " + content);
        HtmlUtil.setBodyContent(content);
        //Platform.log("Ok");
    }

}
