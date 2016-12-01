package naga.providers.toolkit.html;

import elemental2.HTMLButtonElement;
import naga.platform.spi.Platform;
import naga.providers.toolkit.html.drawing.svg.SvgDrawingNode;
import naga.providers.toolkit.html.nodes.controls.*;
import naga.providers.toolkit.html.nodes.layouts.HtmlHBox;
import naga.providers.toolkit.html.nodes.layouts.HtmlVBox;
import naga.providers.toolkit.html.nodes.layouts.HtmlVPage;
import naga.providers.toolkit.html.nodes.layouts.HtmlWindow;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class HtmlToolkit extends Toolkit {

    public HtmlToolkit() {
        super(/* TODO: remove this dependency to Platform */Platform.get().scheduler(), HtmlWindow::new);
        registerNodeFactory(VPage.class, HtmlVPage::new);
        registerNodeFactory(VBox.class, HtmlVBox::new);
        registerNodeFactory(HBox.class, HtmlHBox::new);
        registerNodeFactoryAndWrapper(Button.class, HtmlButton::new, HTMLButtonElement.class, HtmlButton::new);
        registerNodeFactory(CheckBox.class, HtmlCheckbox::new);
        registerNodeFactory(RadioButton.class, HtmlRadioButton::new);
        registerNodeFactory(SearchBox.class, HtmlSearchBox::new);
        registerNodeFactory(TextView.class, HtmlTextView::new);
        registerNodeFactory(HtmlView.class, HtmlHtmlView::new);
        registerNodeFactory(Image.class, HtmlImage::new);
        registerNodeFactory(Table.class, HtmlTable::new);
        registerNodeFactory(DrawingNode.class, SvgDrawingNode::new);
    }
}