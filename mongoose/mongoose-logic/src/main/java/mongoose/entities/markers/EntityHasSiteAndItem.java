package mongoose.entities.markers;

/**
 * @author Bruno Salmon
 */
public interface EntityHasSiteAndItem extends EntityHasSite, EntityHasItem, HasSiteAndItem {

    default boolean hasSiteAndItem() {
        return hasSite() && hasItem();
    }

}
