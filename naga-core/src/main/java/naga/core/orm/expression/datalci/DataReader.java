package naga.core.orm.expression.datalci;

/**
 * @author Bruno Salmon
 */

public interface DataReader<D> {

    D getData(Object id);

    Object getDataId(D data);

    Object getValue(D data, Object field);

    Object getParameterValue(String name);

}
