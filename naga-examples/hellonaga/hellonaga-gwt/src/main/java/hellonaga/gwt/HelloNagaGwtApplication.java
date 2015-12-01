package hellonaga.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.user.client.ui.RootPanel;
import hellonaga.HelloNagaLogic;
import naga.core.spi.plat.Platform;

/**
 * @author Bruno Salmon
 */
public class HelloNagaGwtApplication implements EntryPoint {

    /* No need for GwtPlatform.register(); as the platform will be found by the customized ServiceLoader provided in the super-source */

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
