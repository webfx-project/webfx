package mongoose.activities.backend.event.bookings;

import javafx.beans.property.*;
import mongoose.activities.shared.generic.eventdependent.EventDependentGenericTablePresentationModel;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
class BookingsPresentationModel extends EventDependentGenericTablePresentationModel {

    private final StringProperty columnsProperty = new SimpleStringProperty();
    public final StringProperty columnsProperty() { return columnsProperty; }
    public final void setColumns(String value) { columnsProperty().set(value); }
    public final String getColumns() { return columnsProperty().get(); }

    private final ObjectProperty<LocalDate> dayProperty = new SimpleObjectProperty<>();
    public final ObjectProperty<LocalDate> dayProperty() { return dayProperty; }
    public final void setDay(LocalDate value) { dayProperty().set(value); }
    public final LocalDate getDay() { return dayProperty().get(); }

    private final ObjectProperty<LocalDate> minDayProperty = new SimpleObjectProperty<>();
    public final ObjectProperty<LocalDate> minDayProperty() { return minDayProperty; }
    public final void setMinDay(LocalDate value) { minDayProperty().set(value); }
    public final LocalDate getMinDay() { return minDayProperty().get(); }

    private final ObjectProperty<LocalDate> maxDayProperty = new SimpleObjectProperty<>();
    public final ObjectProperty<LocalDate> maxDayProperty() { return maxDayProperty; }
    public final void setMaxDay(LocalDate value) { maxDayProperty().set(value); }
    public final LocalDate getMaxDay() { return maxDayProperty().get(); }

    private final BooleanProperty arrivalsProperty = new SimpleBooleanProperty();
    public final BooleanProperty arrivalsProperty() { return arrivalsProperty; }
    public final void setArrivals(boolean value) { arrivalsProperty().set(value); }
    public final boolean isArrivals() { return arrivalsProperty().get(); }

    private final BooleanProperty departuresProperty = new SimpleBooleanProperty();
    public final BooleanProperty departuresProperty() { return departuresProperty; }
    public final void setDepartures(boolean value) { departuresProperty().set(value); }
    public final boolean isDepartures() { return departuresProperty().get(); }

    private final StringProperty filterProperty = new SimpleStringProperty();
    public final StringProperty filterProperty() { return filterProperty; }
    public final void setFilter(String value) { filterProperty().set(value); }
    public final String getFilter() { return filterProperty().get(); }

    private final StringProperty orderByProperty = new SimpleStringProperty();
    public final StringProperty orderByProperty() { return orderByProperty; }
    public final void setOrderBy(String value) { orderByProperty().set(value); }
    public final String getOrderBy() { return orderByProperty().get(); }

    private final StringProperty groupByProperty = new SimpleStringProperty();
    public final StringProperty groupByProperty() { return groupByProperty; }
    public final void setGroupBy(String value) { groupByProperty().set(value); }
    public final String getGroupBy() { return groupByProperty().get(); }
}
