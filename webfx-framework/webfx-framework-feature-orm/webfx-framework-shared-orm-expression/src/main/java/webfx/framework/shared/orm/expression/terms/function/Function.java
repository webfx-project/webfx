package webfx.framework.shared.orm.expression.terms.function;

import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.framework.shared.orm.expression.terms.function.java.Coalesce;
import webfx.framework.shared.orm.expression.terms.function.java.CurrentDate;
import webfx.framework.shared.orm.expression.terms.function.java.StringAgg;
import webfx.framework.shared.orm.expression.terms.function.java.Sum;
import webfx.extras.type.PrimType;
import webfx.extras.type.SpecializedTextType;
import webfx.extras.type.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class Function<T> {
    protected final String name;
    protected final String[] argNames; // argument names
    private final Type[] argTypes; // argument types
    private final Type returnType;
    private final boolean evaluable;
    private final boolean keyword; // indicates if we should omit () when calling this function with no args (ex: current_date)

    private static final Map<String, Function> functions = new HashMap<>();

    static {
        new Function("array").register();
        new Function("abs").register();
        new Function("now", PrimType.DATE).register();
        new Function("date_trunc", PrimType.DATE).register();
        new Function("date_part", PrimType.INTEGER).register();
        new Function("to_char", PrimType.STRING).register();
        new Function("lower", PrimType.STRING).register();
        new Function("char_length", PrimType.INTEGER).register();
        new Function("count", PrimType.LONG).register();
        new Function("min").register();
        new Function("max").register();
        new Function("nullif").register();

        new Sum().register();
        new StringAgg().register();
        new CurrentDate().register();

        new InlineFunction("readOnly", "e", null, "e").register();
        new InlineFunction("image", "src", new Type[]{SpecializedTextType.IMAGE_URL}, "src").register();
        new InlineFunction("html", "text", new Type[]{SpecializedTextType.HTML}, "text").register();
        new InlineFunction("isSet", "s", new Type[]{PrimType.STRING}, "s!=null and s!=''").register();
        new InlineFunction("isNotSet", "s", new Type[]{PrimType.STRING}, "s=null or s=''").register();
        new InlineFunction("oneOrZero", "b", new Type[]{PrimType.BOOLEAN}, "b ? 1 : 0").register();
        new InlineFunction("oneOrNull", "b", new Type[]{PrimType.BOOLEAN}, "b ? 1 : null").register();
        new InlineFunction("countIf", "b", new Type[]{PrimType.BOOLEAN}, "count(oneOrNull(b))").register();

        new Coalesce().register();
    }

    public static void register(Function function) {
        functions.put(function.name, function);
    }

    public static <T> Function<T> getFunction(String functionName) {
        return functions.get(functionName);
    }

    public Function(String name) {
        this(name, false);
    }

    public Function(String name, boolean keyword) {
        this(name, null, null, keyword);
    }

    public Function(String name, Type returnType) {
        this(name, returnType, null, false);
    }

    public Function(String name, Type returnType, Boolean evaluable, boolean keyword) {
        this(name, null, null, returnType, evaluable, keyword);
    }

    public Function(String name, String[] argNames, Type[] argTypes, Type returnType) {
        this(name, argNames, argTypes, returnType, null);
    }

    public Function(String name, String[] argNames, Type[] argTypes, Type returnType, Boolean evaluable) {
        this(name, argNames, argTypes, returnType, evaluable, false);
    }

    public Function(String name, String[] argNames, Type[] argTypes, Type returnType, Boolean evaluable, boolean keyword) {
        this.name = name;
        this.argNames = argNames;
        this.argTypes = argTypes;
        this.returnType = returnType;
        this.evaluable = evaluable != null ? evaluable : this instanceof AggregateFunction;
        this.keyword = keyword;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public int getPrecedenceLevel() {
        return 9;
    }

    public boolean isEvaluable() {
        return evaluable;
    }

    public boolean isKeyword() {
        return keyword;
    }

    public boolean isSqlExpressible() {
        return !isEvaluable();
    }

    public boolean isIdentity() {
        return false;
    }

    public Object evaluate(T argument, DomainReader<T> domainReader) {
        throw new UnsupportedOperationException();
    }

    public void register() {
        register(this);
    }
}
