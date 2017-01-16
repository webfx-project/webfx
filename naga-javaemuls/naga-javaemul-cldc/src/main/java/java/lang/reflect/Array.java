package java.lang.reflect;

import naga.platform.services.query.QueryResultSet;
import naga.platform.spi.Platform;

public final class Array {

    public static Object newInstance(Class<?> componentType, int length) throws NegativeArraySizeException {
        if (componentType.equals(Object.class)) return new Object[length];
        if (componentType.equals(QueryResultSet.class)) return new QueryResultSet[length];
        Platform.log("GWT super source Array.newInstance() has no case for type " + componentType + ", so new Object[] is returned but this may cause a ClassCastException.");
        return new Object[length];
    }

}