package webfx.demo.mandelbrot.math.original;

import java.math.BigDecimal;

/**
 * A MandelbrotTask computes iteration counts for one row of pixels in
 * an image.  The constructor provides all the data necessary for the
 * computation.  The run() method does the computation.  After the
 * run() method has run, getResults() can be called to retrieve
 * the data that has been computed.  Note that this class includes support
 * for doing arbitrary precision computations.
 */
public class MandelbrotTask implements Runnable {

    // Updates June 2011 to use long[] arrays instead of int[]
    // for high-precision computation, for a significant speedup.

    private final BigDecimal xmin,xmax, yval;
    private final int columnCount;
    private final int rowNumber;
    private final int maxIterations;
    private final boolean highPrecision;

    private int jobNumber;
    private volatile boolean done;  // Marks this task as done (set by TaskManager).
    // Can be used to abort high-precision computation, not regular precision.

    private int[] iterationCounts;



    public MandelbrotTask(int rowNumber, BigDecimal xmin, BigDecimal xmax, BigDecimal yval,
                          int columnCount, int maxIterations, boolean highPrecision) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.yval = yval;
        this.columnCount = columnCount;
        this.rowNumber = rowNumber;
        this.maxIterations = maxIterations;
        this.highPrecision = highPrecision;
    }

    public BigDecimal getXmin() {
        return xmin;
    }

    public BigDecimal getXmax() {
        return xmax;
    }

    public BigDecimal getYval() {
        return yval;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public boolean isHighPrecision() {
        return highPrecision;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    /**
     * Sets a job number to identify the computation that this task is
     * part of.  This is used by the TaskManager, to keep track of jobs
     * that use network workers to do part of the job.  The number has
     * no meaning outside the context of the TaskManager.
     */
    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * If the task is done, this returns an array containing the iteration
     * counts that have been computed.  Before the job is done, the return value
     * can be null or can be an array that contains invalid data.
     */
    public int[] getResults() {
        return iterationCounts;
    }

    void setResults(int[] results) {  // used by network workers in the TaskManager
        iterationCounts = results;
    }

    void makeDone() {
        // used by the TaskManager, to mark this task as done or when aborting a job.
        // also used by MandelbrotNetworkTaksManager when aborting a job.
        done = true;
    }

    boolean isDone() {  // used by the TaskManager
        return done;
    }

    public void run() {
        iterationCounts = new int[columnCount];
        if (highPrecision) {
            digits = xmin.scale();
            createHPData();
            for (int i = 0; i < columnCount && !done; i++)
                iterationCounts[i] = countIterations(xs[i], y);
        }
        else {
            double xmin_d = xmin.doubleValue();
            double xmax_d = xmax.doubleValue();
            double yval_d = yval.doubleValue();
            double dx = (xmax_d - xmin_d) / (columnCount - 1);
            for (int i = 0; i < columnCount; i++)
                iterationCounts[i] = countIterations(xmin_d + dx*i, yval_d);
            zx = zy = work1 = work2 = work3 = y = null;
            xs = null;
        }
    }

    private int countIterations(double x, double y) {
        int count = 0;
        double zx = x;
        double zy = y;
        while (count < maxIterations
                && zx*zx + zy*zy < 8) {
            double new_zx = zx*zx - zy*zy + x;
            zy = 2*zx*zy + y;
            zx = new_zx;
            count++;
        }
        return count;
    }

    private int countIterations(long[] x, long[] y) {
        System.arraycopy(x,0,zx,0,chunks);
        System.arraycopy(y,0,zy,0,chunks);
        int count = 0;
        while (count < maxIterations) {
            System.arraycopy(zx, 0, work2, 0, chunks);
            multiply(work2,zx,chunks);  // work2 = zx*zx
            System.arraycopy(zy, 0, work1, 0, chunks);
            multiply(work1,zy,chunks);  // work1 = zy*zy
            System.arraycopy(work1,0,work3,0,chunks);   // work3 = zy*zy, save a copy.  (Note: multiplication uses work3.)
            add(work1,work2,chunks);  // work1 = zx*zx + zy*zy
            if ((work1[0] & 0xFFFFFFF8L) != 0 && (work1[0] & 0xFFFFFFF8L) != 0xFFFFFFF0L)
                break;
            negate(work3,chunks);  // work3 = -work3 = -zy*zy
            add(work2,work3,chunks);  // work2 = zx*zx - zy*zy
            add(work2,x,chunks); // work2 = zx*zx - zy*zy + x, the next value for zx
            System.arraycopy(zx,0,work1,0,chunks);  // work1 = zx
            add(work1,zx,chunks);  // work1 = 2*zx
            multiply(work1,zy,chunks);  // work1 = 2*zx*zy
            add(work1,y,chunks);  // work1 = 2*zx*zy + y, the next value for zy
            System.arraycopy(work1,0,zy,0,chunks);  // zy = work1
            System.arraycopy(work2,0,zx,0,chunks);  // zx = work2
            count++;
        }
        return count;
    }


    // ------- support for high-precision calculation ------------

    private long[][] xs;
    private long[] y;

    private long[] work1,work2,work3;
    private long[] zx, zy;

    private int digits;  // == 0 for normal precision, otherwise number of digits used
    private int chunks;

    private BigDecimal twoTo32 = new BigDecimal(0x100000000L);

    private final static double log2of10 = Math.log(10)/Math.log(2);

    private void createHPData() {
        chunks =(int)((digits * log2of10)/32 + 1.5);
        y = new long[chunks+1];
        convert(y,yval,chunks+1);
        xs = new long[columnCount][];
        xs[0] = new long[chunks+1];
        convert(xs[0],xmin,chunks+1);
        if (columnCount > 1) {
            long[] dx = new long[chunks+1];
            BigDecimal DX = xmax.subtract(xmin).setScale(2*xmax.scale()).divide(new BigDecimal(columnCount-1),BigDecimal.ROUND_HALF_EVEN);
            convert(dx,DX,chunks+1);
            for (int i = 1; i < columnCount; i++) {
                xs[i] = cloneArray(xs[i-1]);
                add(xs[i],dx,chunks+1);
            }
        }
        zx = new long[chunks];
        zy = new long[chunks];
        work1 = new long[chunks];
        work2 = new long[chunks];
        work3 = new long[chunks];
    }

    private long[] cloneArray(long[] array) {
        //return array.clone(); // doesn't compile with GWT
        long[] array2 = new long[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }

    private void convert(long[] x, BigDecimal X, int count) {
        boolean neg = false;
        if (X.signum() == -1) {
            neg = true;
            X = X.negate();
        }
        x[0] = X.longValue();
        for (int i = 1; i < count; i++) {
            X = X.subtract(new BigDecimal(x[i-1]));
            X = X.multiply(twoTo32);
            x[i] = X.longValue();
        }
        if (neg)
            negate(x,count);
    }

    private void add(long[] x, long[] dx, int count) {
        long carry = 0;
        for (int i = count - 1; i >= 0; i--) {
            x[i] += dx[i];
            x[i] += carry;
            carry = x[i] >>> 32;
            x[i] &= 0xFFFFFFFFL;
        }
    }

    private void multiply(long[] x, long[] y, int count){  // Can't allow x == y !
        boolean neg1 = (x[0] & 0x80000000L) != 0;
        if (neg1)
            negate(x,count);
        boolean neg2 = (y[0] & 0x80000000L) != 0;
        if (neg2)
            negate(y,count);
        if (x[0] == 0) {
            for (int i = 0; i < count; i++)
                work3[i] = 0;
        }
        else {
            long carry = 0;
            for (int i = count-1; i >= 0; i--) {
                work3[i] = x[0]*y[i] + carry;
                carry = work3[i] >>> 32;
                work3[i] &= 0xFFFFFFFFL;
            }
        }
        for (int j = 1; j < count; j++) {
            int i = count - j;
            long carry = (x[j]*y[i]) >>> 32;
            i--;
            int k = count - 1;
            while (i >= 0) {
                work3[k] += x[j]*y[i] + carry;
                carry = work3[k] >>> 32;
                work3[k] &= 0xFFFFFFFFL;
                i--;
                k--;
            }
            while (carry != 0 && k >= 0) {
                work3[k] += carry;
                carry = work3[k] >>> 32;
                work3[k] &= 0xFFFFFFFFL;
                k--;
            }
        }
        System.arraycopy(work3,0,x,0,count);
        if (neg2)
            negate(y,count);
        if (neg1 != neg2)
            negate(x,count);
    }

    private void negate(long[] x, int chunks) {
        for (int i = 0; i < chunks; i++)
            x[i] = 0xFFFFFFFFL-x[i];
        ++x[chunks-1];
        for (int i = chunks-1; i > 0 && (x[i] & 0x100000000L) != 0; i--) {
            x[i] &= 0xFFFFFFFFL;
            ++x[i-1];
        }
        x[0] &= 0xFFFFFFFFL;
    }

//	private BigDecimal convertBack(int[] x, int count) {
//		boolean neg = false;
//		if ((x[0] & 0x8000) != 0) {
//			negate(x,count);
//			neg = true;
//		}
//		BigDecimal X = new BigDecimal(x[0]).setScale(count*6,BigDecimal.ROUND_HALF_EVEN);
//		BigDecimal div = twoTo16;
//		for (int i = 1; i < count; i++) {
//			X = X.add(new BigDecimal(x[i]).setScale(count*4,BigDecimal.ROUND_HALF_EVEN).divide(div,BigDecimal.ROUND_HALF_EVEN));
//			div = div.multiply(twoTo16);
//		}
//		if (neg) {
//			X = X.negate();
//			negate(x,count);
//		}
//		return X;
//	}

}
