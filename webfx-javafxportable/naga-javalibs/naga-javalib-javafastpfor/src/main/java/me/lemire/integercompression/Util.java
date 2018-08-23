/**
 * This code is released under the
 * Apache License Version 2.0 http://www.apache.org/licenses/.
 *
 * (c) Daniel Lemire, http://lemire.me/en/
 */
package me.lemire.integercompression;

/**
 * Routine utility functions.
 * 
 * @author Daniel Lemire
 * 
 */
public final class Util {
    /**
     * Compute the maximum of the integer logarithms (ceil(log(x+1)) of a range
     * of value
     * 
     * @param i
     *            source array
     * @param pos
     *            starting position
     * @param length
     *            number of integers to consider
     * @return integer logarithm
     */
    public static int maxbits(int[] i, int pos, int length) {
        int mask = 0;
        for (int k = pos; k < pos + length; ++k)
            mask |= i[k];
        return bits(mask);
    }

    /**
     * Compute the maximum of the integer logarithms (ceil(log(x+1)) of a the
     * successive differences (deltas) of a range of value
     * 
     * @param initoffset
     *            initial vallue for the computation of the deltas
     * @param i
     *            source array
     * @param pos
     *            starting position
     * @param length
     *            number of integers to consider
     * @return integer logarithm
     */
    public static int maxdiffbits(int initoffset, int[] i, int pos, int length) {
        int mask = 0;
        mask |= (i[pos] - initoffset);
        for (int k = pos + 1; k < pos + length; ++k) {
            mask |= i[k] - i[k - 1];
        }
        return bits(mask);
    }

    /**
     * Compute the integer logarithms (ceil(log(x+1)) of a value
     * 
     * @param i
     *            source value
     * @return integer logarithm
     */
    public static int bits(int i) {
        return 32 - Integer.numberOfLeadingZeros(i);
    }

    protected static int packsize(int num, int b) {
        if (b > 16)
            return num;
        int howmanyfit = 32 / b;
        return (num + howmanyfit - 1) / howmanyfit;
    }

    protected static int pack(int[] outputarray, int arraypos, int[] data, int datapos,
            int num, int b) {
        if (num == 0)
            return arraypos;
        if (b > 16) {
            System.arraycopy(data, datapos, outputarray, arraypos, num);
            return num + arraypos;
        }
        for (int k = 0; k < packsize(num, b); ++k)
            outputarray[k + arraypos] = 0;
        int inwordpointer = 0;
        for (int k = 0; k < num; ++k) {
            outputarray[arraypos] |= (data[k + datapos] << inwordpointer);
            inwordpointer += b;
            final int increment = ((inwordpointer + b - 1) >> 5);
            arraypos += increment;
            inwordpointer &= ~(-increment);
        }
        return arraypos + (inwordpointer > 0 ? 1 : 0);
    }

    /**
     * return floor(value / factor) * factor
     * 
     * @param value
     *            numerator
     * @param factor
     *            denominator
     * @return greatest multiple of factor no larger than value
     */
    public static int greatestMultiple(int value, int factor) {
        return value - value % factor;
    }

}
