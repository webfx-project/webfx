package hellonaga.teavm;

import hellonaga.logic.HelloNagaLogic;
import naga.core.orm.expression.term.Select;
import naga.core.orm.expressionparser.ExpressionParser;
import naga.core.orm.expressionparser.lci.ParserDomainModelReaderMock;
import naga.core.spi.platform.Platform;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * @author Bruno Salmon
 */
public class HelloNagaTeaVmApplication {

    /* No need for TeaVmPlatform.register(); as the platform will be found by the ServiceLoader */

    public static void main(String[] args) {
        Platform.setWebLogger(HelloNagaTeaVmApplication::displayMessageInDom);
        // Testing the expression parser with TeaVM
        String eql = "select name,country.(name, continent) from Organization where !closed order by name";
        Platform.log("Parsing expression: " + eql + " ...");
        try {
            Select select = ExpressionParser.parseSelect(eql, new ParserDomainModelReaderMock());
            Platform.log("Expression successfully parsed: " + select);
        } catch (Throwable e) {
            Platform.log(e);
        }
        HelloNagaLogic.runClient();
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
