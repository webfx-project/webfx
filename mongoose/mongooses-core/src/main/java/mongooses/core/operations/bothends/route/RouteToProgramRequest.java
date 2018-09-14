package mongooses.core.operations.bothends.route;

import mongooses.core.activities.sharedends.book.program.ProgramRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToProgramRequest extends RoutePushRequest {

    public RouteToProgramRequest(Object eventId, BrowsingHistory history) {
        super(ProgramRouting.getProgramPath(eventId), history);
    }

}
