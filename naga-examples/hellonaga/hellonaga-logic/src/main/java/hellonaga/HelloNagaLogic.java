package hellonaga;

import naga.core.spi.json.Json;
import naga.core.spi.json.JsonObject;
import naga.core.spi.plat.Platform;

/**
 * @author Bruno Salmon
 */
public class HelloNagaLogic {

    public interface MessageDisplayer {
        void displayMessage(String message);
    }
    private MessageDisplayer messageDisplayer;

    public HelloNagaLogic() {
        this(System.out::println);
    }

    public HelloNagaLogic(MessageDisplayer messageDisplayer) {
        setMessageDisplayer(messageDisplayer);
    }

    public void setMessageDisplayer(MessageDisplayer messageDisplayer) {
        this.messageDisplayer = messageDisplayer;
    }

    public void run() {
        JsonObject p = Json.parse("{\"firstName\": \"John\", \"lastName\": \"Smith\", \"age\": 43}");
        p.set("fullName", p.getString("firstName") + " " + p.getString("lastName"));
        p.set("age", p.getNumber("age") + 1);
        messageDisplayer.displayMessage(p.toJsonString());

        Platform.bus().send("version", "get", event -> messageDisplayer.displayMessage("" + event.body()));
    }
}
