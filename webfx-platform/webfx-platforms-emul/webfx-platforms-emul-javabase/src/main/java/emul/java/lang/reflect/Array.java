package emul.java.lang.reflect;

import webfx.platform.services.log.Logger;
import webfx.platform.services.query.QueryResult;
import webfx.platform.services.update.UpdateResult;

public final class Array {

    public static Object newInstance(Class<?> componentType, int length) throws NegativeArraySizeException {
        if (componentType.equals(Object.class)) return new Object[length];
        if (componentType.equals(QueryResult.class)) return new QueryResult[length];
        if (componentType.equals(UpdateResult.class)) return new UpdateResult[length];
        Logger.log("GWT super source Array.newInstance() has no case for type " + componentType + ", so new Object[] is returned but this may cause a ClassCastException.");
        return new Object[length];
    }

}