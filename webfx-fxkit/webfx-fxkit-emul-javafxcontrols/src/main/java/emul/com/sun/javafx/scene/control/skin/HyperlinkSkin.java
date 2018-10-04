package emul.com.sun.javafx.scene.control.skin;

import emul.com.sun.javafx.scene.control.behaviour.ButtonBehavior;
import emul.javafx.scene.control.Hyperlink;

/**
 * A Skin for Hyperlinks.
 */
public class HyperlinkSkin extends LabeledSkinBase<Hyperlink, ButtonBehavior<Hyperlink>> {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    public HyperlinkSkin(Hyperlink link) {
        super(link, new ButtonBehavior<>(link));
    }

}
