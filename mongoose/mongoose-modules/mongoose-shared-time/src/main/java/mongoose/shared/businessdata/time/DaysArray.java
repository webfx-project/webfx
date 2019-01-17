package mongoose.shared.businessdata.time;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static mongoose.shared.businessdata.time.TimeConverter.convertTime;

/**
 * @author Bruno Salmon
 */
public class DaysArray implements Iterable<Long> {

    private final long[] array;
    private final TimeUnit timeUnit;

    public DaysArray(long[] array, TimeUnit timeUnit) {
        this.array = array;
        this.timeUnit = timeUnit;
    }

    public long[] getArray() {
        return array;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public boolean isEmpty() {
        return array.length == 0;
    }

    public long getDay(int index) {
        return array[index];
    }

    public long getEpochDay(int index) {
        return convertTime(getDay(index), timeUnit, TimeUnit.DAYS);
    }

    public LocalDate getDate(int index) {
        return LocalDate.ofEpochDay(getEpochDay(index));
    }

    public LocalDate getFirstDate() {
        return getDate(0);
    }

    public int indexOfDay(long day) {
        int n = array.length;
        if (n > 0 && day >= array[0] && day <= array[n - 1])
            for (int i = 0; i < n; i++) {
                long d = array[i];
                if (d == day)
                    return i;
                if (d > day)
                    break;
            }
        return -1;
    }

    @Override
    public Iterator<Long> iterator() {
        return new Iterator<Long>() {
            int nextIndex = 0;
            @Override
            public boolean hasNext() {
                return nextIndex < array.length;
            }

            @Override
            public Long next() {
                return getDay(nextIndex++);
            }

            public void remove() { // necessary for GWT compilation (perhaps they haven't written the default method)
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    public Iterable<LocalDate> localDateIterable() {
        return this::localDateIterator;
    }

    public Iterator<LocalDate> localDateIterator() {
        return new Iterator<LocalDate>() {
            int nextIndex = 0;
            @Override
            public boolean hasNext() {
                return nextIndex < array.length;
            }

            @Override
            public LocalDate next() {
                return getDate(nextIndex++);
            }

            public void remove() { // necessary for GWT compilation (perhaps they haven't written the default method)
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    public DaysArray changeTimeUnit(TimeUnit newTimeUnit) {
        if (timeUnit == newTimeUnit)
            return this;
        int n = array.length, i = 0;
        long[] newArray = new long[n];
        for (long day : array)
            newArray[i++] = convertTime(day, timeUnit, newTimeUnit);
        return new DaysArray(newArray, newTimeUnit);
    }

    public DaysArray shift(long shiftDays) {
        return shift(shiftDays, TimeUnit.DAYS);
    }

    public DaysArray shift(long shiftDays, TimeUnit shiftTimeUnit) {
        shiftDays = convertTime(shiftDays, shiftTimeUnit, timeUnit);
        if (shiftDays == 0)
            return this;
        int n = array.length, i = 0;
        long[] newArray = new long[n];
        for (long day : array)
            newArray[i++] = day + shiftDays;
        return new DaysArray(newArray, timeUnit);
    }

    public boolean overlaps(DaysArray daysArray) {
        if (!daysArray.isEmpty()) {
            daysArray = daysArray.changeTimeUnit(timeUnit);
            for (long day : array)
                if (daysArray.indexOfDay(day) != -1)
                    return true;
        }
        return false;
    }

    public TimeSeries toSeries() {
        return new TimeSeriesBuilder(timeUnit).addDaysArrays(this).build();
    }
}
