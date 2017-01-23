package mongoose.activities.frontend.application.html;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;
import mongoose.activities.shared.application.web.WebMongooseBundle;

/**
 * @author Bruno Salmon
 */
public interface HtmlFrontendMongooseBundle extends WebMongooseBundle {

    HtmlFrontendMongooseBundle R = GWT.create(HtmlFrontendMongooseBundle.class);

    @Source("mongoose/html/frontend/activities/container/container.html")
    TextResource containerHtml();

    @Source("mongoose/html/frontend/activities/event/fees/fees.html")
    TextResource feesHtml();

    @Source("mongoose/html/frontend/activities/event/terms/terms.html")
    TextResource termsHtml();

    @Source("mongoose/html/frontend/activities/event/program/program.html")
    TextResource programHtml();

    @Source("mongoose/html/frontend/activities/event/options/options.html")
    TextResource optionsHtml();
}
