package java.util;

/* Customized ServiceLoader that work to provide the GwtPlatform on automatic registration Platform.get() */

public class ServiceLoader<S> {

    public static <S> ServiceLoader<S> load(Class<S> service) {
        if (service.equals(naga.core.spi.plat.Platform.class))
            return new ServiceLoader<>();
        return null;
    }

    public Iterator<S> iterator() {
        ArrayList list = new ArrayList();
        list.add(new naga.core.spi.plat.gwt.GwtPlatform());
        return (Iterator<S>) list.iterator();
    }
}