package naga.core.orm.expression.datalci;

/**
 * @author Bruno Salmon
 */
public interface DataWriter<D> extends DataReader<D> {

    void setValue(D data, Object field, Object value);

    void setParameterValue(String name, Object value);

}
