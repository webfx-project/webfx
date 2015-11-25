package naga.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.user.client.ui.RootPanel;
import naga.core.spi.bus.BusOptions;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonObject;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.gwt.GwtPlatform;

/*
 * @author Bruno Salmon
 */

public class GwtApplication implements EntryPoint {

    static {
        GwtPlatform.register();
    }

    public void onModuleLoad() {
        JsonObject p = Json.parse("{\"firstName\": \"John\", \"lastName\": \"Smith\", \"age\": 43}");
        p.set("fullName", p.getString("firstName") + " " + p.getString("lastName"));
        p.set("age", p.getNumber("age") + 1);
        displayMessage(p.toJsonString());

        Platform.createBus(new BusOptions().setProtocol("http")).send("version", "get", event -> displayMessage("" + event.body()));
    }

    private void displayMessage(String message) {
        // Tracing the message in the console
        Platform.log(message);

        // Displaying the message in the DOM
        ParagraphElement p = Document.get().createPElement();
        p.setInnerText(message);
        RootPanel.get().getElement().appendChild(p);
    }
}
