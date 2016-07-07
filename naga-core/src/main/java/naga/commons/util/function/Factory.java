package naga.commons.util.function;

/**
 * @author Bruno Salmon
 */
public interface Factory<T> {

    T create();

    /* doesn't compile with GWT
     static <T> Factory<T> fromDefaultConstructor(Class<? extends T> clazz) {
        return () -> {
            try {
                return  clazz.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        };
    }*/
}
