package webfx.framework.client.activity;

/**
 * @author Bruno Salmon
 */
public interface HasActivityContext<C extends ActivityContext<C>> {

    C getActivityContext();

}
