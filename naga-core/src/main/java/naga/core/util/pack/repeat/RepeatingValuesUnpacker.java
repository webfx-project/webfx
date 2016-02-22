package naga.core.util.pack.repeat;

import naga.core.util.pack.ValuesUnpacker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class RepeatingValuesUnpacker implements ValuesUnpacker {

    public final static RepeatingValuesUnpacker SINGLETON = new RepeatingValuesUnpacker();

    private RepeatingValuesUnpacker() {
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
