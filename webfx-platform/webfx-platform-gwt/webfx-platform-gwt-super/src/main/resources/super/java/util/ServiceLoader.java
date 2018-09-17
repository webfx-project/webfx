/**
 * ServiceLoader GWT implementation
 *
 * @author Bruno Salmon
 */

package java.util;

public abstract class ServiceLoader<S> implements Iterable<S> {

    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {
        return new GwtPlatformServiceLoader().load(serviceClass);
    }
}