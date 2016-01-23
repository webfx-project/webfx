package hellonaga;

import naga.core.spi.platform.Platform;

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
        Platform.bus().send("version", "get", event -> messageDisplayer.displayMessage("" + event.body()));
    }
}
