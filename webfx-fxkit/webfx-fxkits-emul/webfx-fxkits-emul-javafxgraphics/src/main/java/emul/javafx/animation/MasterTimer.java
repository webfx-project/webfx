package emul.javafx.animation;

import webfx.platforms.core.services.uischeduler.UiScheduler;
import webfx.platforms.core.services.uischeduler.spi.AnimationFramePass;

/**
 * @author Bruno Salmon
 */
class MasterTimer extends AbstractMasterTimer {

    private static MasterTimer INSTANCE;
    public static MasterTimer get() {
        if (INSTANCE == null)
            INSTANCE = new MasterTimer();
        return INSTANCE;
    }

    @Override
    protected void postUpdateAnimationRunnable(DelayedRunnable animationRunnable) {
        UiScheduler.scheduleDelayInAnimationFrame(animationRunnable.getDelay(), animationRunnable, AnimationFramePass.UI_UPDATE_PASS);
    }

    @Override
    protected int getPulseDuration(int precision) {
        int rate = 60;
        return precision / rate;
    }
}
