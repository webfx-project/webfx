package naga.core.util.pack.repeat;

import naga.core.util.pack.ValuesUnpacker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class RepeatingValuesUnpacker implements ValuesUnpacker {

    private final Iterator packedValues;

    private int uncompressedValuesSize = -1;

    public RepeatingValuesUnpacker(Iterator packedValues) {
        this.packedValues = packedValues;
    }

    @Override
    public int unpackedSize() {
        if (uncompressedValuesSize == -1)
            uncompressedValuesSize = ((Number) packedValues.next()).intValue(); // May be Double instead of Integer when coming from GWT json
        return uncompressedValuesSize;
    }

    @Override
    public Iterator unpackedValues() {
        unpackedSize();
        int repeatValuesSize = ((Number) packedValues.next()).intValue(); // May be Double instead of Integer when coming from GWT json
        List<RepeatingValue> repeatValues = new ArrayList<>(repeatValuesSize);
        for (int i = 0; i < repeatValuesSize; i++)
            repeatValues.add(new RepeatingValue(packedValues.next(), (String) packedValues.next()));
        return new Iterator() {
            int index = -1;
            @Override
            public boolean hasNext() {
                return index + 1 < uncompressedValuesSize;
            }

            @Override
            public Object next() {
                index++;
                for (RepeatingValue repeatValue : repeatValues)
                    if (repeatValue.isRepeatedAtIndex(index))
                        return repeatValue.getValue();
                return packedValues.next();
            }

            @Override
            public void remove() { // GWT complains if not overridden
                throw new UnsupportedOperationException("remove");
            }
        };
    }
}
