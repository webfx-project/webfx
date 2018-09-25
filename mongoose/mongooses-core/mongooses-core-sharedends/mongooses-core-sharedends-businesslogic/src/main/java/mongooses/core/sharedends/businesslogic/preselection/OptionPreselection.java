package mongooses.core.sharedends.businesslogic.preselection;

import mongooses.core.shared.domainmodel.time.DateTimeRange;
import mongooses.core.shared.domainmodel.time.DayTimeRange;
import mongooses.core.shared.entities.Option;

/**
 * @author Bruno Salmon
 */
public class OptionPreselection {
    private Option option;
    private DateTimeRange dateTimeRange;
    private DayTimeRange dayTimeRange;

    OptionPreselection(Option option, String dateTimeRange, String dayTimeRange) {
        this(option, DateTimeRange.parse(dateTimeRange), dayTimeRange);
    }

    OptionPreselection(Option option, DateTimeRange dateTimeRange, String dayTimeRange) {
        this(option, dateTimeRange, DayTimeRange.parse(dayTimeRange));
    }

    OptionPreselection(Option option, DateTimeRange dateTimeRange, DayTimeRange dayTimeRange) {
        this.option = option;
        this.dateTimeRange = dateTimeRange;
        this.dayTimeRange = dayTimeRange;
    }

    public Option getOption() {
        return option;
    }

    public DateTimeRange getDateTimeRange() {
        return dateTimeRange;
    }

    public DayTimeRange getDayTimeRange() {
        return dayTimeRange;
    }

}
