package mongoose.backend.operations.entities.generic;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.extras.type.PrimType;
import webfx.extras.type.Type;
import webfx.extras.type.Types;
import webfx.framework.shared.orm.expression.terms.*;

import java.util.ArrayList;
import java.util.List;

final class ExportHelper {

    static <T> Expression<T> getConcatExpression(Expression<T> expression, boolean excludeNumbers, String separator) {
        Expression<T> textExpression = getTextExpression(expression, excludeNumbers, false);
        if (textExpression instanceof ExpressionArray) {
            Expression<T> concatExpression = null;
            for (Expression<T> exp : ((ExpressionArray<T>) textExpression).getExpressions())
                concatExpression = concatExpression == null ? exp : new Plus<>(concatExpression, new Plus<>(Constant.newConstant(separator), exp));
            textExpression = concatExpression;
        }
        return textExpression;
    }

    static <T> Expression<T> getTextExpression(Expression<T> expression, boolean excludeNumbers, boolean skipArray) {
        if (expression instanceof Symbol) {
            Expression<T> subExpression = ((Symbol<T>) expression).getExpression();
            if (subExpression != null)
                return getTextExpression(subExpression, excludeNumbers, skipArray);
        }
        if (expression instanceof ExpressionArray) {
            List<Expression<T>> textExpressions = null;
            for (Expression<T> exp : ((ExpressionArray<T>) expression).getExpressions()) {
                Expression<T> te = getTextExpression(exp, excludeNumbers, skipArray);
                if (te != null) {
                    if (skipArray)
                        return te;
                    if (textExpressions == null)
                        textExpressions = new ArrayList<>();
                    textExpressions.add(te);
                }
            }
            return textExpressions == null ? null : textExpressions.size() == 1 ? textExpressions.get(0) : new ExpressionArray<>(textExpressions);
        }
        if (expression instanceof Dot) {
            Dot<T> dot = (Dot<T>) expression;
            Expression<T> r = getTextExpression(dot.getRight(), excludeNumbers, skipArray);
            return r == null ? null : r == dot.getRight() ? dot : new Dot<>(dot.getLeft(), r, dot.isOuterJoin());
        }
        Type type = expression.getType();
        if (excludeNumbers && Types.isNumberType(type) || Types.isImageType(type))
            return null;
        Expression topRightExpression = expression.getFinalForwardingTypeExpression();
        if (topRightExpression instanceof DomainField) {
            DomainClass foreignClass = ((DomainField) topRightExpression).getForeignClass();
            if (foreignClass != null) {
                Expression<T> fte = getTextExpression(foreignClass.getForeignFields(), excludeNumbers, skipArray);
                return fte == null ? null : new Dot<>(expression, fte, true);
            }
        }
        PrimType primType = Types.getPrimType(type);
        if (primType != null) {
            if (primType.isBoolean())
                return new TernaryExpression<>(expression, Constant.newConstant(expression.toString()), Constant.newConstant("not " + expression.toString()));
            if (primType.isString() || primType.isNumber() || primType.isDate())
                return expression;
        }
        return null;
    }

    static String getTextExpressionValue(Object value) {
        if (value == null)
            return "";
        if (value instanceof Object[]) {
            StringBuilder sb = new StringBuilder();
            for (Object v : (Object[]) value) {
                if (sb.length() > 0)
                    sb.append(' ');
                sb.append(getTextExpressionValue(v));
            }
            value = sb.toString();
        }
        return value.toString() == null /*ex: NoValue*/ ? "" : value.toString().replace('\n', ' '); // removing \n in order to paste the whole content in the same Excel cell (ex: special needs)
    }
}