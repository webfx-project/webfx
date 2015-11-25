package naga.teavm;

/*
 * @author Bruno Salmon
 */

import naga.core.spi.bus.BusOptions;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonObject;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.teavm.TeaVmPlatform;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

public class TeaVmApplication {

    public static void main(String[] args) {
        TeaVmPlatform.register();

        JsonObject p = Json.parse("{\"firstName\": \"John\", \"lastName\": \"Smith\", \"age\": 43}");
        p.set("fullName", p.getString("firstName") + " " + p.getString("lastName"));
        p.set("age", p.getNumber("age") + 1);
        displayMessage(p.toJsonString());

        Platform.createBus(new BusOptions().setProtocol("http")).send("version", "get", event -> displayMessage("" + event.body()));
    }

    private static void displayMessage(String message) {
        // Tracing the message in the console
        Platform.log(message);

        // Displaying the message in the DOM
        HTMLDocument document = HTMLDocument.current();
        HTMLElement p = document.createElement("p");
        p.appendChild(document.createTextNode(message));
        document.getBody().appendChild(p);
    }
}
