package naga.commons.type;

/*
 * @author Bruno Salmon
 */

public interface CollectionType extends Type {

    Type getElementsType();

}
