package hellonaga.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.user.client.ui.RootPanel;
import hellonaga.HelloNagaLogic;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.gwt.GwtPlatform;

/**
 * @author Bruno Salmon
 */
public class HelloNagaGwtApplication implements EntryPoint {

    static {
        GwtPlatform.register();
    }

    @Override
    public void onModuleLoad() {
        new HelloNagaLogic(HelloNagaGwtApplication::displayMessage).run();
    }

    private static void displayMessage(String message) {
        // Tracing the message in the console
        Platform.log(message);

        // Displaying the message in the DOM
        ParagraphElement p = Document.get().createPElement();
        p.setInnerText(message);
        RootPanel.get().getElement().appendChild(p);
    }

}
