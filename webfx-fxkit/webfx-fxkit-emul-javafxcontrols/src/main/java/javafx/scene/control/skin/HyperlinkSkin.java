package javafx.scene.control.skin;

import com.sun.javafx.scene.control.behaviour.ButtonBehavior;
import javafx.scene.control.Hyperlink;

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
