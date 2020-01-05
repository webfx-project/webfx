package webfx.platform.shared.datascope;

/**
 * @author Bruno Salmon
 */
public interface DataScope {

    boolean intersects(DataScope dataScope);

}
