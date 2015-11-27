package helloworld.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.user.client.ui.RootPanel;
import helloworld.HelloWorldLogic;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public class HelloWorldGwtApplication implements EntryPoint {

    public void onModuleLoad() {
        // Calling the application logic
        String helloMessage = HelloWorldLogic.helloMessage();

        // Tracing the message in the console
        Logger logger = Logger.getLogger("HelloWorldLogger");
        logger.log(Level.INFO, helloMessage);

        // Displaying the message in the DOM
        HeadingElement headingElement = Document.get().createHElement(1);
        headingElement.setInnerText(helloMessage);
        RootPanel.get().getElement().appendChild(headingElement);
    }
}
