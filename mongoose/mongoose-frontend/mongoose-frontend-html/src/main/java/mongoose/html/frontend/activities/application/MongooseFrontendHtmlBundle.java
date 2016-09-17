package mongoose.html.frontend.activities.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;
import mongoose.web.activities.shared.application.MongooseWebBundle;

/**
 * @author Bruno Salmon
 */
public interface MongooseFrontendHtmlBundle extends MongooseWebBundle {

    MongooseFrontendHtmlBundle R = GWT.create(MongooseFrontendHtmlBundle.class);

    @Source("mongoose/html/frontend/activities/event/fees/fees.html")
    TextResource feesHtml();

    @Source("mongoose/html/frontend/activities/event/terms/terms.html")
    TextResource termsHtml();

    @Source("mongoose/html/frontend/activities/event/program/program.html")
    TextResource programHtml();

    @Source("mongoose/html/frontend/activities/event/options/options.html")
    TextResource optionsHtml();
}
