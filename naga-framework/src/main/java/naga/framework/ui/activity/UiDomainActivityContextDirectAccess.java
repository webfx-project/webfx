package naga.framework.ui.activity;

/**
 * @author Bruno Salmon
 */
public interface UiDomainActivityContextDirectAccess<C extends UiDomainActivityContext<C>> extends UiDomainActivityContext<C>, UiActivityContextDirectAccess<C>, DomainActivityContextDirectAccess<C> {
}
