package naga.core.orm.expression.datalci;


import naga.core.util.function.Callable;

/**
 * @author Bruno Salmon
 */
public class DataThreadContext implements AutoCloseable {

    private final static ThreadLocal<DataThreadContext> contexts = new ThreadLocal<>();

    private DataReader dataReader;

    public DataThreadContext(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    public DataReader getDataReader() {
        return dataReader;
    }

    @Override
    public void close() {
        contexts.set(null);
    }

    public static DataThreadContext open(DataReader dataReader) {
        DataThreadContext context = new DataThreadContext(dataReader);
        contexts.set(context);
        return context;
    }

    public static DataThreadContext getInstance() {
        return contexts.get();
    }
}
