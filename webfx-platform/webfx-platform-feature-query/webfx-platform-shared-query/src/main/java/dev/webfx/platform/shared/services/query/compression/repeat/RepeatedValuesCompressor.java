package dev.webfx.platform.shared.services.query.compression.repeat;

import dev.webfx.platform.shared.util.Numbers;
import dev.webfx.platform.shared.services.query.compression.ValuesCompressor;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public final class RepeatedValuesCompressor implements ValuesCompressor {

    public final static RepeatedValuesCompressor SINGLETON = new RepeatedValuesCompressor();

    private RepeatedValuesCompressor() {}

    @Override
    public Object[] compress(Object[] values) {
        int length = values.length;
        List<Object> nonRepeatValues = new ArrayList<>(length);
        Map<Object, SortedIntegersTokenizer> repeatValues = new HashMap<>();

        Object lastValue = null;
        SortedIntegersTokenizer lastRepeatValueIndexes = null;

        Object lastNonRepeatValue = null;
        int lastNonRepeatGlobalIndex = -1;

        // Compression algorithm
        for (int index = 0; index < length; index++) {
            Object value = values[index];
            if (!Objects.equals(value, lastValue) || lastRepeatValueIndexes == null) {
                lastRepeatValueIndexes = repeatValues.get(value);
                if (lastRepeatValueIndexes == null) {
                    if (Objects.equals(value, lastNonRepeatValue) && lastNonRepeatGlobalIndex >= 0) {
                        repeatValues.put(value, lastRepeatValueIndexes = new SortedIntegersTokenizer());
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
        Object[] compressedValues = new Object[2 + 2 * repeatValues.size() + nonRepeatValues.size()];
        int packedIndex = 0;
        compressedValues[packedIndex++] = length;
        compressedValues[packedIndex++] = repeatValues.size();
        for (Map.Entry<Object, SortedIntegersTokenizer> repeatEntry : repeatValues.entrySet()) {
            compressedValues[packedIndex++] = repeatEntry.getKey();
            compressedValues[packedIndex++] = repeatEntry.getValue().token();
        }
        for (Object nonRepeatValue : nonRepeatValues)
            compressedValues[packedIndex++] = nonRepeatValue;
        return compressedValues;
    }

    @Override
    public Object[] uncompress(Object[] compressedValues) {
        int compressedIndex = 0;
        int uncompressedLength = Numbers.intValue(compressedValues[compressedIndex++]); // May be Double instead of Integer when coming from GWT json
        int repeatValuesSize = Numbers.intValue(compressedValues[compressedIndex++]);   // May be Double instead of Integer when coming from GWT json
        List<RepeatedValue> repeatValues = new ArrayList<>(repeatValuesSize);
        for (int i = 0; i < repeatValuesSize; i++)
            repeatValues.add(new RepeatedValue(compressedValues[compressedIndex++], (String) compressedValues[compressedIndex++]));

        Object[] uncompressedValues = new Object[uncompressedLength];
        loop: for (int uncompressedIndex = 0; uncompressedIndex < uncompressedLength; uncompressedIndex++) {
            for (RepeatedValue repeatValue : repeatValues)
                if (repeatValue.isRepeatedAtIndex(uncompressedIndex)) {
                    uncompressedValues[uncompressedIndex] = repeatValue.getValue();
                    continue loop;
                }
            uncompressedValues[uncompressedIndex] = compressedValues[compressedIndex++];
        }
        return uncompressedValues;
    }

}
