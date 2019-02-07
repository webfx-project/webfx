package mongoose.client.controls.calendargraphic.impl;

import javafx.beans.property.DoubleProperty;
import javafx.scene.text.Font;
import mongoose.shared.businessdata.time.TimeInterval;
import webfx.platform.shared.util.collection.Collections;
import webfx.platform.shared.util.collection.HashList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
final class VerticalDayTimePositioner {

    final static double slotHeight = 35d;
    final static Font slotFont = Font.font("Verdana", 13);

    private final DoubleProperty containerHeightProperty;
    private long firstDisplayedMinute;
    private long lastDisplayedMinute;

    VerticalDayTimePositioner(DoubleProperty containerHeightProperty) {
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
        //Doesn't work on Android: List<TimeInterval> slots = verticalDayTimePositionedCollection.stream().map(VerticalDayTimePositioned::getDayTimeMinuteInterval).collect(Collectors.toCollection(HashList::new));
        List<TimeInterval> slots = new HashList<>(Collections.map(verticalDayTimePositionedCollection, VerticalDayTimePositioned::getDayTimeMinuteInterval));
        //Doesn't work on Android: slots.sort(Comparator.comparingLong(TimeInterval::getIncludedStart));
        Collections.sort(slots, Collections.comparingLong(TimeInterval::getIncludedStart));
        for (VerticalDayTimePositioned verticalDayTimePositioned : verticalDayTimePositionedCollection) {
            TimeInterval minuteInterval = verticalDayTimePositioned.getDayTimeMinuteInterval();
            int slotIndex = slots.indexOf(minuteInterval);
            verticalDayTimePositioned.setYAndHeight(slotIndex * slotHeight, slotHeight - 1);
        }
        containerHeightProperty.setValue(DayColumnHeaderViewModel.dayColumnHeaderHeight + slots.size() * slotHeight);
    }
}
