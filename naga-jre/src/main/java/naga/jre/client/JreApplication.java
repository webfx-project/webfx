package naga.jre.client;

import naga.core.spi.json.Json;
import naga.core.spi.json.JsonObject;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.javaplat.JavaPlatform;

/*
 * @author Bruno Salmon
 */

public class JreApplication {

    public static void main(String[] args) {
        JavaPlatform.register();

        JsonObject p = Json.parse("{\"firstName\": \"John\", \"lastName\": \"Smith\", \"age\": 43}");
        p.set("fullName", p.getString("firstName") + " " + p.getString("lastName"));
        p.set("age", p.getNumber("age") + 1);
        displayMessage(p.toJsonString());

        Platform.createBus().send("version", null, event -> Platform.log(event.body()));
    }

    private static void displayMessage(String message) {
        // Tracing the message in the console
        Platform.log(message);
    }
}
