package mongoose.activities.monitor.metrics.controller;

import mongoose.activities.monitor.metrics.model.SysBean;

import java.time.Instant;

/**
 * @author Jean-Pierre Alonso.
 */
public abstract class SystemLookup {

    protected SysBean sb;

    public SysBean snapshot() {
        long rnd = (long)Math.random();
        sb = new SysBean();

        sb.setDate(Instant.now());
        sb.setCommittedMem(200+rnd*20);
        sb.setTotalMem(64+rnd*10);
        sb.setFreeMem(20+rnd*40);
        sb.setUsedMem(sb.getTotalMem()-sb.getFreeMem());
        sb.setMaxMem(100+rnd*10);
        sb.setFreePhMem(800+rnd*20);
        sb.setTotalPhMem(1000+rnd*20);
        sb.setAvailableProcessors(4);
        sb.setSysLoad(50+rnd*20);
        sb.setCpuLoad(-1);
        sb.setCpuTime(rnd-100000);
        return sb;
    }
}
