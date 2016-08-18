package naga.framework.expression.terms.function;

import naga.commons.type.DerivedType;
import naga.framework.expression.lci.DataReader;
import naga.framework.expression.terms.function.java.AbcNames;
import naga.framework.expression.terms.function.java.Coalesce;
import naga.framework.expression.terms.function.java.DateIntervalFormat;
import naga.commons.type.PrimType;
import naga.commons.type.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class Function<T> {
    protected final String name;
    protected final String[] argNames; // argument names
    protected final Type[] argTypes; // argument types
    protected final Type returnType;
    protected final boolean evaluable;

    private static Map<String, Function> functions = new HashMap<>();
    static {
        register(new Function("array"));
        register(new Function("abs"));
        register(new Function("now", PrimType.DATE));
        register(new Function("date_trunc", PrimType.DATE));
        register(new Function("date_part", PrimType.INTEGER));
        register(new Function("to_char", PrimType.STRING));
        register(new Function("lower", PrimType.STRING));
        register(new Function("char_length", PrimType.INTEGER));
        register(new Function("count", PrimType.LONG));
        register(new Function("min"));
        register(new Function("max"));
        register(new Function("sum"));
        register(new Function("string_agg", PrimType.STRING));

        register(new InlineFunction("readOnly", "e", null, "e"));
        register(new InlineFunction("image", "src", new Type[]{new DerivedType("image", PrimType.STRING, true)}, "src"));
        register(new InlineFunction("isSet", "s", new Type[]{PrimType.STRING}, "s!=null and s!=''"));
        register(new InlineFunction("isNotSet", "s", new Type[]{PrimType.STRING}, "s=null or s=''"));
        register(new InlineFunction("oneOrZero", "b", new Type[]{PrimType.BOOLEAN}, "b ? 1 : 0"));
        register(new InlineFunction("oneOrNull", "b", new Type[]{PrimType.BOOLEAN}, "b ? 1 : null"));
        register(new InlineFunction("countIf", "b", new Type[]{PrimType.BOOLEAN}, "count(oneOrNull(b))"));

        register(new Coalesce());
        register(new AbcNames());
        register(new AbcNames("alphaSearch"));
        register(new DateIntervalFormat());

        register(new Function("interpret_brackets", PrimType.STRING));
        register(new Function("compute_dates"));

        //register(new InlineFunction("searchMatchesDocument", "d", new Type[]{new Type(DataSource.getDataSource(3L).getDomainModel().getClass("Document"))}, "d..ref=?searchInteger or d..person_abcNames like ?abcSearchLike or d..person_email like ?searchEmailLike"));
        //register(new InlineFunction("searchMatchesPerson", "p", new Type[]{new Type(DataSource.getDataSource(3L).getDomainModel().getClass("Person"))}, "abcNames(p..firstName + ' ' + p..lastName) like ?abcSearchLike or p..email like ?searchEmailLike"));
    }

    protected static void register(Function function) {
        functions.put(function.name, function);
    }

    public static <T> Function<T> getFunction(String functionName) {
        return functions.get(functionName);
    }

    public Function(String name) {
        this(name, null);
    }
    public Function(String name, Type returnType) {
        this(name, null, null, returnType);
    }

    public Function(String name, String[] argNames, Type[] argTypes, Type returnType) {
        this(name, argNames, argTypes, returnType, false);
    }

    public Function(String name, String[] argNames, Type[] argTypes, Type returnType, boolean evaluable) {
        this.name = name;
        this.argNames = argNames;
        this.argTypes = argTypes;
        this.returnType = returnType;
        this.evaluable = evaluable;
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

    public Object evaluate(T argument, DataReader<T> dataReader) {
        throw new UnsupportedOperationException();
    }

}
