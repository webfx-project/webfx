package webfx.framework.client.orm.reactive.dql.statement.conventions;

public interface HasSlaveVisibilityCondition<E> {

    boolean isSlaveVisible(E selectedMaster);

}
