package mongooses.core.shared.entities.markers;

/**
 * @author Bruno Salmon
 */
public interface HasCancelled {

    void setCancelled(Boolean cancelled);

    Boolean isCancelled();

}
