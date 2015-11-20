package naga.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.ui.RootPanel;
import naga.core.Naga;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author Bruno Salmon
 */

public class GwApplication implements EntryPoint {

    public void onModuleLoad() {
        String nagaVersion = new Naga().getVersion();

        displayMessage(nagaVersion);
    }

    private void displayMessage(String message) {
        // Tracing the message in the console
        Logger logger = Logger.getLogger("NagaLogger");
        logger.log(Level.INFO,  message);

        // Displaying the message in the DOM
        SpanElement headingElement = Document.get().createSpanElement();
        headingElement.setInnerText(message);
        RootPanel.get().getElement().appendChild(headingElement);
    }
}
