package mongoose.activities.shared.logic.ui.calendargraphic.impl;

import javafx.beans.property.Property;
import mongoose.activities.shared.logic.time.TimeInterval;
import naga.commons.util.collection.HashList;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
class VerticalDayTimePositioner {

    final static double slotHeight = 35d;
    final static Font slotFont = Font.font("Verdana", 13);

    private final Property<Double> containerHeightProperty;
    private long firstDisplayedMinute;
    private long lastDisplayedMinute;

    VerticalDayTimePositioner(Property<Double> containerHeightProperty) {
        this.containerHeightProperty = containerHeightProperty;
        init();
    }

    void init() {
        firstDisplayedMinute = 24 * 60;
        lastDisplayedMinute = 0;
    }

    private final Collection<VerticalDayTimePositioned> verticalDayTimePositionedCollection = new ArrayList<>();

    void addVerticalDayTimePositioned(VerticalDayTimePositioned verticalDayTimePositioned) {
        verticalDayTimePositionedCollection.add(verticalDayTimePositioned);
        updateVerticalDayTimePositioned(verticalDayTimePositioned);
    }

    void removeVerticalDayTimePositioned(VerticalDayTimePositioned verticalDayTimePositioned) {
        verticalDayTimePositionedCollection.remove(verticalDayTimePositioned);
    }

    void updateVerticalDayTimePositioned(VerticalDayTimePositioned verticalDayTimePositioned) {
        TimeInterval minuteInterval = verticalDayTimePositioned.getDayTimeMinuteInterval();
        firstDisplayedMinute = Math.min(firstDisplayedMinute, minuteInterval.getIncludedStart());
        lastDisplayedMinute = Math.max(lastDisplayedMinute, minuteInterval.getExcludedEnd());
    }

    void updateVerticalPositions() {
        List<TimeInterval> slots = verticalDayTimePositionedCollection.stream().map(VerticalDayTimePositioned::getDayTimeMinuteInterval).collect(Collectors.toCollection(HashList::new));
        slots.sort(Comparator.comparingLong(TimeInterval::getIncludedStart));
        for (VerticalDayTimePositioned verticalDayTimePositioned : verticalDayTimePositionedCollection) {
            TimeInterval minuteInterval = verticalDayTimePositioned.getDayTimeMinuteInterval();
            int slotIndex = slots.indexOf(minuteInterval);
            verticalDayTimePositioned.setYAndHeight(slotIndex * slotHeight, slotHeight - 1);
        }
        containerHeightProperty.setValue(DayColumnHeaderViewModel.dayColumnHeaderHeight + slots.size() * slotHeight);
    }
}
