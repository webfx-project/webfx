package mongoose.activities.tester.metrics.controller;

import mongoose.activities.tester.metrics.model.SysBean;

/**
 * @author Jean-Pierre Alonso.
 */
public abstract class SystemLookup {

    protected SysBean sb;

    public SysBean snapshot() {
        sb = new SysBean();

        long current = System.currentTimeMillis();
        sb.setCommittedMem(200+(long)Math.cos(current)*20);
        sb.setTotalMem(64+(long)Math.cos(current)*10);
        sb.setFreeMem(20+(long)Math.cos(current)*40);
        sb.setUsedMem(sb.getTotalMem()-sb.getFreeMem());
        sb.setMaxMem(100+(long)Math.cos(current)*10);
        sb.setFreePhMem(800+(long)Math.cos(current)*20);
        sb.setTotalPhMem(1000+(long)Math.cos(current)*20);
        sb.setAvailableProcessors(4);
        sb.setSysLoad(50+(long)Math.cos(current)*20);
        sb.setCpuLoad(-1);
        sb.setCpuTime(current-100000);
        return sb;
    }
}
