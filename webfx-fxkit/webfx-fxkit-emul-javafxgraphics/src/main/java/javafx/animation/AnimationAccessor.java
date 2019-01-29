package javafx.animation;

/**
 * @author Bruno Salmon
 */
public abstract class AnimationAccessor {
    public static AnimationAccessor DEFAULT;

    public static AnimationAccessor getDefault() {
        return DEFAULT;
    }

    public abstract void setCurrentRate(Animation animation, double currentRate);

    public abstract void setCurrentTicks(Animation animation, long ticks);

    public abstract void playTo(Animation animation, long pos, long cycleTicks);

    public abstract void jumpTo(Animation animation, long pos, long cycleTicks, boolean forceJump);

    public abstract void finished(Animation animation);


}

