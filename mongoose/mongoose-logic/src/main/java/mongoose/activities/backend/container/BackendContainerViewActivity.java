package mongoose.activities.backend.container;

import mongoose.activities.backend.event.bookings.BookingsRoutingRequest;
import mongoose.activities.backend.event.letters.LettersRoutingRequest;
import mongoose.activities.backend.events.EventsRoutingRequest;
import mongoose.activities.backend.monitor.MonitorRoutingRequest;
import mongoose.activities.backend.organizations.OrganizationsRoutingRequest;
import mongoose.activities.backend.tester.TesterRoutingRequest;
import mongoose.activities.shared.container.SharedContainerViewActivity;
import naga.framework.operation.OperationActionProducer;
import naga.framework.ui.action.Action;
import naga.util.collection.Collections;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class BackendContainerViewActivity extends SharedContainerViewActivity implements OperationActionProducer {

    @Override
    protected Collection<Action> navigationActions() {
        super.navigationActions();
        return Collections.listOf(
                  backAction
                , forwardAction
                , newAction(() -> new OrganizationsRoutingRequest(getHistory()))
                , newAction(() -> new EventsRoutingRequest(getHistory()))
                , newAction(() -> new BookingsRoutingRequest(getParameter("eventId"), getHistory()))
                , newAction(() -> new LettersRoutingRequest(getParameter("eventId"), getHistory()))
                , newAction(() -> new MonitorRoutingRequest(getHistory()))
                , newAction(() -> new TesterRoutingRequest(getHistory()))
                , englishAction
                , frenchAction
        );
    }
}
