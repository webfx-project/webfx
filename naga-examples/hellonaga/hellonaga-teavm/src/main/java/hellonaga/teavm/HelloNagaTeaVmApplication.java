package hellonaga.teavm;

import hellonaga.logic.HelloNagaLogic;
import naga.core.orm.expression.term.Select;
import naga.core.orm.expressionparser.ExpressionParser;
import naga.core.orm.expressionparser.lci.ParserModelReaderMock;
import naga.core.spi.platform.Platform;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * @author Bruno Salmon
 */
public class HelloNagaTeaVmApplication {

    /* No need for TeaVmPlatform.register(); as the platform will be found by the ServiceLoader */

    public static void main(String[] args) {
        // Testing the expression parser with TeaVM (produces a compilation error for now)
        Select select = ExpressionParser.parseSelect("select event,<ident> from Document order by id desc limit 100",
                new ParserModelReaderMock());
        Platform.log("expression: " + select);
        /*
        Platform.setWebLogger(HelloNagaTeaVmApplication::displayMessageInDom);
        HelloNagaLogic.runClient();*/
    }

    private static void displayMessageInDom(String helloMessage) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement preloader = document.getElementById("preloader");
        preloader.clear();
        HTMLElement p = document.createElement("h2");
        p.appendChild(document.createTextNode(helloMessage));
        preloader.appendChild(p);
    }

}
