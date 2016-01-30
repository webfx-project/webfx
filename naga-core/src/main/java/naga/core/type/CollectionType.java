package naga.core.type;

/*
 * @author Bruno Salmon
 */

public interface CollectionType extends Type {

    Type getElementsType();

}
