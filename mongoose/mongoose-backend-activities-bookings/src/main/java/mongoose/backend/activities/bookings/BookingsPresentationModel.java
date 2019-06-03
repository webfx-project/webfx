package mongoose.backend.activities.bookings;

import javafx.beans.property.*;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
final class BookingsPresentationModel extends EventDependentGenericTablePresentationModel {

    private final StringProperty columnsProperty = new SimpleStringProperty();
    final StringProperty columnsProperty() { return columnsProperty; }
    final void setColumns(String value) { columnsProperty().set(value); }
    final String getColumns() { return columnsProperty().get(); }

    private final ObjectProperty<LocalDate> dayProperty = new SimpleObjectProperty<>();
    final ObjectProperty<LocalDate> dayProperty() { return dayProperty; }
    final void setDay(LocalDate value) { dayProperty().set(value); }
    final LocalDate getDay() { return dayProperty().get(); }

    private final ObjectProperty<LocalDate> minDayProperty = new SimpleObjectProperty<>();
    final ObjectProperty<LocalDate> minDayProperty() { return minDayProperty; }
    final void setMinDay(LocalDate value) { minDayProperty().set(value); }
    final LocalDate getMinDay() { return minDayProperty().get(); }

    private final ObjectProperty<LocalDate> maxDayProperty = new SimpleObjectProperty<>();
    final ObjectProperty<LocalDate> maxDayProperty() { return maxDayProperty; }
    final void setMaxDay(LocalDate value) { maxDayProperty().set(value); }
    final LocalDate getMaxDay() { return maxDayProperty().get(); }

    private final BooleanProperty arrivalsProperty = new SimpleBooleanProperty();
    final BooleanProperty arrivalsProperty() { return arrivalsProperty; }
    final void setArrivals(boolean value) { arrivalsProperty().set(value); }
    final boolean isArrivals() { return arrivalsProperty().get(); }

    private final BooleanProperty departuresProperty = new SimpleBooleanProperty();
    final BooleanProperty departuresProperty() { return departuresProperty; }
    final void setDepartures(boolean value) { departuresProperty().set(value); }
    final boolean isDepartures() { return departuresProperty().get(); }

    private final StringProperty filterProperty = new SimpleStringProperty();
    final StringProperty filterProperty() { return filterProperty; }
    final void setFilter(String value) { filterProperty().set(value); }
    final String getFilter() { return filterProperty().get(); }

    private final StringProperty orderByProperty = new SimpleStringProperty();
    final StringProperty orderByProperty() { return orderByProperty; }
    final void setOrderBy(String value) { orderByProperty().set(value); }
    final String getOrderBy() { return orderByProperty().get(); }

    private final StringProperty groupByProperty = new SimpleStringProperty();
    final StringProperty groupByProperty() { return groupByProperty; }
    final void setGroupBy(String value) { groupByProperty().set(value); }
    final String getGroupBy() { return groupByProperty().get(); }

    private final StringProperty conditionStringFilterProperty = new SimpleStringProperty();
    final StringProperty conditionStringFilterProperty() { return conditionStringFilterProperty; }
    final void setConditionStringFilter(String value) { conditionStringFilterProperty().set(value); }
    final String getConditionStringFilter() { return conditionStringFilterProperty().get(); }

    private final StringProperty groupStringFilterProperty = new SimpleStringProperty();
    final StringProperty groupStringFilterProperty() { return groupStringFilterProperty; }
    final void setGroupStringFilter(String value) { groupStringFilterProperty().set(value); }
    final String getGroupStringFilter() { return groupStringFilterProperty().get(); }

    private final StringProperty columnsStringFilterProperty = new SimpleStringProperty();
    final StringProperty columnsStringFilterProperty() { return columnsStringFilterProperty; }
    final void setColumnsStringFilter(String value) { columnsStringFilterProperty().set(value); }
    final String getColumnsStringFilter() { return columnsStringFilterProperty().get(); }
}
