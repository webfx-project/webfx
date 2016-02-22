package naga.core.util.pack.repeat;

import naga.core.util.pack.ValuesPacker;

import java.util.*;

/**
 * A values packer that
 *
 * @author Bruno Salmon
 */
public class RepeatingValuesPacker implements ValuesPacker {

    public final static RepeatingValuesPacker SINGLETON = new RepeatingValuesPacker();

    private RepeatingValuesPacker() {}

    @Override
    public Object[] packValues(Object[] values) {
        int length = values.length;
        List<Object> nonRepeatValues = new ArrayList<>(length);
        Map<Object, IncreasingIntegersTokenizer> repeatValues = new HashMap<>();

        Object lastValue = null;
        IncreasingIntegersTokenizer lastRepeatValueIndexes = null;

        Object lastNonRepeatValue = null;
        int lastNonRepeatGlobalIndex = -1;

        // Packing algorithm
        for (int index = 0; index < length; index++) {
            Object value = values[index];
            if (!Objects.equals(value, lastValue) || lastRepeatValueIndexes == null) {
                lastRepeatValueIndexes = repeatValues.get(value);
                if (lastRepeatValueIndexes == null) {
                    if (Objects.equals(value, lastNonRepeatValue) && lastNonRepeatGlobalIndex >= 0) {
                        repeatValues.put(value, lastRepeatValueIndexes = new IncreasingIntegersTokenizer());
                        nonRepeatValues.remove(nonRepeatValues.size() - 1);
                        lastRepeatValueIndexes.pushInt(lastNonRepeatGlobalIndex);
                    }
                }
            }
            if (lastRepeatValueIndexes != null)
                lastRepeatValueIndexes.pushInt(index);
            else {
                nonRepeatValues.add(lastNonRepeatValue = value);
                lastNonRepeatGlobalIndex = index;
            }
            lastValue = value;
        }

        // Generating output
        Object[] packedValues = new Object[2 + 2 * repeatValues.size() + nonRepeatValues.size()];
        int packedIndex = 0;
        packedValues[packedIndex++] = length;
        packedValues[packedIndex++] = repeatValues.size();
        for (Map.Entry<Object, IncreasingIntegersTokenizer> repeatEntry : repeatValues.entrySet()) {
            packedValues[packedIndex++] = repeatEntry.getKey();
            packedValues[packedIndex++] = repeatEntry.getValue().token();
        }
        for (Object nonRepeatValue : nonRepeatValues)
            packedValues[packedIndex++] = nonRepeatValue;
        return packedValues;
    }

    @Override
    public Object[] unpackValues(Object[] packedValues) {
        int packedIndex = 0;
        int unpackLength = ((Number) packedValues[packedIndex++]).intValue(); // May be Double instead of Integer when coming from GWT json
        int repeatValuesSize = ((Number) packedValues[packedIndex++]).intValue(); // May be Double instead of Integer when coming from GWT json
        List<RepeatingValue> repeatValues = new ArrayList<>(repeatValuesSize);
        for (int i = 0; i < repeatValuesSize; i++)
            repeatValues.add(new RepeatingValue(packedValues[packedIndex++], (String) packedValues[packedIndex++]));

        Object[] unpackedValues = new Object[unpackLength];
        loop: for (int unpackedIndex = 0; unpackedIndex < unpackLength; unpackedIndex++) {
            for (RepeatingValue repeatValue : repeatValues)
                if (repeatValue.isRepeatedAtIndex(unpackedIndex)) {
                    unpackedValues[unpackedIndex] = repeatValue.getValue();
                    continue loop;
                }
            unpackedValues[unpackedIndex] = packedValues[packedIndex++];
        }
        return unpackedValues;
    }

}
