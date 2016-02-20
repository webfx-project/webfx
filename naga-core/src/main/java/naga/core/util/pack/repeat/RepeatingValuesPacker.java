package naga.core.util.pack.repeat;

import naga.core.util.pack.ValuesPacker;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public class RepeatingValuesPacker implements ValuesPacker {

    private final List<Object> nonRepeatValues = new ArrayList<>();
    private final Map<Object, IncreasingIntegersTokenizer> repeatValues = new HashMap<>();

    private int globalIndex = 0;

    private Object lastValue;
    private IncreasingIntegersTokenizer lastRepeatValueIndexes;

    private Object lastNonRepeatValue;
    private int lastNonRepeatGlobalIndex = -1;

    public RepeatingValuesPacker() {
    }

    @Override
    public void pushValue(Object value) {
        if (value != lastValue || lastRepeatValueIndexes == null) {
            lastRepeatValueIndexes = repeatValues.get(value);
            if (lastRepeatValueIndexes == null) {
                if (lastNonRepeatValue == value && lastNonRepeatGlobalIndex >= 0) {
                    repeatValues.put(value, lastRepeatValueIndexes = new IncreasingIntegersTokenizer());
                    nonRepeatValues.remove(nonRepeatValues.size() - 1);
                    lastRepeatValueIndexes.pushInt(lastNonRepeatGlobalIndex);
                }
            }
        }
        if (lastRepeatValueIndexes != null)
            lastRepeatValueIndexes.pushInt(globalIndex);
        else {
            nonRepeatValues.add(lastNonRepeatValue = value);
            lastNonRepeatGlobalIndex = globalIndex;
        }
        lastValue = value;
        globalIndex++;
    }

    public Iterator packedValues() {
        return new Iterator() {

            private int index = 0;
            Iterator<Map.Entry<Object, IncreasingIntegersTokenizer>> repeatEntryIterator = repeatValues.entrySet().iterator();
            Map.Entry<Object, IncreasingIntegersTokenizer> repeatEntry;
            Iterator<Object> nonRepeatValuesIterator = nonRepeatValues.iterator();

            @Override
            public boolean hasNext() {
                return index <= 1 || repeatEntry != null || repeatEntryIterator.hasNext() || nonRepeatValuesIterator.hasNext();
            }

            @Override
            public Object next() {
                switch (index++) {
                    case 0: return globalIndex;
                    case 1: return repeatValues.size();
                }
                if (repeatEntry == null && repeatEntryIterator.hasNext()) {
                    repeatEntry = repeatEntryIterator.next();
                    return repeatEntry.getKey();
                }
                if (repeatEntry != null) {
                    IncreasingIntegersTokenizer indexes = repeatEntry.getValue();
                    repeatEntry = null;
                    return indexes.token();
                }
                return nonRepeatValuesIterator.next();
            }

            @Override
            public void remove() { // GWT complains if not overridden
                throw new UnsupportedOperationException("remove");
            }
        };
    }

}
