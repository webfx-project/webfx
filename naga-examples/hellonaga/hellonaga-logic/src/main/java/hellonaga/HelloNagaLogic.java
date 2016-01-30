package hellonaga;

import naga.core.Naga;
import naga.core.buscall.BusCallService;

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

    public void runClient() {
        BusCallService.call(Naga.VERSION_ADDRESS, "ignored").setHandler(asyncResult -> messageDisplayer.displayMessage("" + (asyncResult.succeeded() ? asyncResult.result() : asyncResult.cause())));
    }
}
