package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;

/**
 * @author Bruno Salmon
 */
public abstract class NumberExpressionBase implements NumberExpression {

    // ===============================================================
    // String conversions

    @Override
    public StringBinding asString() {
        return (StringBinding) StringFormatter.convert(this);
    }

}
