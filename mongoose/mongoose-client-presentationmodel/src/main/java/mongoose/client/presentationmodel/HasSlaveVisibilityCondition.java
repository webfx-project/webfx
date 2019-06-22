package mongoose.client.presentationmodel;

public interface HasSlaveVisibilityCondition<E> {

    boolean isSlaveVisible(E selectedMaster);

}
