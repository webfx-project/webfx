package emul.java.lang.reflect;

import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.query.QueryResult;
import webfx.platforms.core.services.update.UpdateResult;

public final class Array {

    public static Object newInstance(Class<?> componentType, int length) throws NegativeArraySizeException {
        if (componentType.equals(Object.class)) return new Object[length];
        if (componentType.equals(QueryResult.class)) return new QueryResult[length];
        if (componentType.equals(UpdateResult.class)) return new UpdateResult[length];
        Logger.log("GWT super source Array.newInstance() has no case for type " + componentType + ", so new Object[] is returned but this may cause a ClassCastException.");
        return new Object[length];
    }

}