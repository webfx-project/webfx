package emul.com.sun.javafx.binding;

import emul.javafx.beans.InvalidationListener;
import emul.javafx.beans.Observable;
import emul.javafx.beans.binding.Binding;

import java.lang.ref.WeakReference;

public class BindingHelperObserver implements InvalidationListener {

    private final WeakReference<Binding<?>> ref;

    public BindingHelperObserver(Binding<?> binding) {
        if (binding == null) {
            throw new NullPointerException("Binding has to be specified.");
        }
        ref = new WeakReference<Binding<?>>(binding);
    }

    @Override
    public void invalidated(Observable observable) {
        final Binding<?> binding = ref.get();
        if (binding == null) {
            observable.removeListener(this);
        } else {
            binding.invalidate();
        }
    }

}
