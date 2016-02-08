package naga.core.orm.expression;

import naga.core.orm.expression.datalci.DataReader;
import naga.core.orm.expression.datalci.DataThreadContext;
import naga.core.orm.expression.datalci.DataWriter;
import naga.core.orm.expression.term.Expression;

/**
 * @author Bruno Salmon
 */
public class Expressions {

    public static Object evaluate(Expression expression, Object data, DataReader dataReader) {
        try (DataThreadContext context = DataThreadContext.open(dataReader)) {
            return expression.evaluate(data);
        }
    }

    public static void setExpressionValue(Expression expression, Object data, Object value, DataReader dataWriter) {
        try (DataThreadContext context = DataThreadContext.open(dataWriter)) {
            expression.setValue(data, value);
        }
    }

    public static DataReader getDataReader() {
        return DataThreadContext.getInstance().getDataReader();
    }

    public static DataWriter getDataWriter() {
        DataReader dataReader = DataThreadContext.getInstance().getDataReader();
        return (dataReader instanceof DataWriter) ? (DataWriter) dataReader : null;
    }

    public static String toString(Expression expression) {
        return expression == null ? null : expression.toString(new StringBuilder()).toString();
    }

}
