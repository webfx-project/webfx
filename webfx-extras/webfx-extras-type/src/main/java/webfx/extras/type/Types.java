package webfx.extras.type;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */

public final class Types {

    public CollectionType STRINGS = collectionType(PrimType.STRING);

    public static ArrayType arrayType(Type... types) {
        return () -> types;
    }

    public static CollectionType collectionType(Type elementsType) {
        return () -> elementsType;
    }

    public static Type guessType(Object o) {
        if (o instanceof Object[]) // to replace by o.getClass().isArray() for TeaVm WebAssembly?
            return guessArrayType((Object[]) o);
        if (o instanceof Collection)
            return guessCollectionType((Collection) o);
        PrimType primType = PrimType.fromObject(o);
        if (primType != null)
            return primType;
        return ObjectType.fromObject(o);
    }

    public static ArrayType guessArrayType(Object[] objects) {
        int n = objects.length;
        Type[] types = new Type[n];
        for (int i = 0; i < n; i++)
            types[i] = guessType(objects[i]);
        return arrayType(types);
    }

    public static CollectionType guessCollectionType(Collection collection) {
        return collectionType(guessType(collection.iterator().next()));
    }

    public static PrimType getPrimType(Type type) {
        if (type instanceof PrimType)
            return (PrimType) type;
        if (type instanceof DerivedType)
            return ((DerivedType) type).getPrimType();
        return null;
    }

    public static boolean isBooleanType(Type type) {
        PrimType primType = getPrimType(type);
        return primType != null && primType.isBoolean();
    }

    public static boolean isStringType(Type type) {
        PrimType primType = getPrimType(type);
        return primType != null && primType.isString();
    }

    public static boolean isNumberType(Type type) {
        PrimType primType = getPrimType(type);
        return primType != null && primType.isNumber();
    }

    public static SpecializedText getSpecializedText(Type type) {
        return type instanceof SpecializedTextType ? ((SpecializedTextType) type).getSpecializedText() : null;
    }

    public static boolean isImageType(Type type) {
        return getSpecializedText(type) == SpecializedText.IMAGE_URL;
    }

    public static boolean isHtmlType(Type type) {
        return getSpecializedText(type) == SpecializedText.HTML;
    }

    public static boolean isArrayType(Type type) {
        return type instanceof ArrayType;
    }
}
