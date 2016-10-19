package mongoose.html.frontend.activities.container;

import elemental2.Element;
import javafx.beans.property.Property;
import mongoose.activities.frontend.container.FrontendContainerViewModel;
import naga.providers.toolkit.html.nodes.HtmlNode;
import naga.providers.toolkit.html.nodes.layouts.HtmlVPage;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.HtmlView;

import static mongoose.html.frontend.activities.application.MongooseFrontendHtmlBundle.R;
import static naga.providers.toolkit.html.util.HtmlUtil.getElementById;

/**
 * @author Bruno Salmon
 */
public class ContainerUi {

    public static FrontendContainerViewModel buildView(Toolkit toolkit) {
        HtmlView htmlView = toolkit.createHtmlView(R.containerHtml().getText());
        Element rootElement = (Element) htmlView.unwrapToNativeNode();
        Button backButton = toolkit.createButton();
        Button forwardButton = toolkit.createButton();
        Button organizationsButton = toolkit.createButton();
        Button eventsButton = toolkit.createButton();
        Button englishButton = toolkit.createButton();
        Button frenchButton = toolkit.createButton();
        Property mountNodeProperty = new HtmlVPage(getElementById(rootElement, "subview")).centerProperty();
        return new FrontendContainerViewModel(toolkit.createVPage()
                .setHeader(toolkit.createHBox(backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton))
                .setCenter(new HtmlNode(getElementById(rootElement, "container"))),
                backButton, forwardButton, organizationsButton, eventsButton, englishButton, frenchButton,
                mountNodeProperty);
    }

}
