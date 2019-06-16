package mongoose.backend.operations.income;

import mongoose.backend.activities.statistics.routing.StatisticsRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToIncomeRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToIncome";

    public RouteToIncomeRequest(Object eventId, BrowsingHistory history) {
        super(StatisticsRouting.getEventStatisticsPath(eventId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

}
