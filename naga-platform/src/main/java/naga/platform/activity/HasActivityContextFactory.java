package naga.platform.activity;

/**
 * @author Bruno Salmon
 */
public interface HasActivityContextFactory<C extends ActivityContext<C>> {

    ActivityContextFactory<C> getActivityContextFactory();

}
