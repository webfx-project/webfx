package rx;

/**
 * @author Bruno Salmon
 */
public interface Subscription {

    void unsubscribe();

    boolean isUnsubscribed();

}

