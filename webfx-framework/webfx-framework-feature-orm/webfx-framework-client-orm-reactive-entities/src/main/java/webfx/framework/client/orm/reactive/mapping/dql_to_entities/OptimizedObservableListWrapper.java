package webfx.framework.client.orm.reactive.mapping.dql_to_entities;

import java.util.*;

/**
 * An optimized version of ObservableListWrapper when calling setAll(): the change computes the permutation over the
 * remaining elements (the initial version was just a remove all followed by add all). Quite important when using this
 * mapper to update a user interface with server push notifications.
 *
 * @author Bruno Salmon
 */
public final class OptimizedObservableListWrapper<E> extends ObservableListWrapper<E> {

    public OptimizedObservableListWrapper() {
        this(new ArrayList<>());
    }

    OptimizedObservableListWrapper(List<E> list) {
        super(list);
    }

    public void runInTransaction(Runnable runnable) {
        try {
            beginChange();
            runnable.run();
        } finally {
            endChange();
        }
    }

    @Override
    public boolean setAll(Collection<? extends E> col) {
        if (isEmpty())
            addAll(col);
        else {
            beginChange();
            try {
                // Removing all current elements not present in the new collection
                retainAll(col);
                // Adding all new elements at their final position
                int newSize = col.size();
                int alreadyPresentSize = size();
                int addedSize = newSize - alreadyPresentSize;
                int i = 0;
                BitSet bs = new BitSet(newSize);
                for (E e : col) {
                    if (addedSize == 0)
                        break;
                    if (!contains(e)) {
                        add(i, e);
                        bs.set(i);
                        addedSize--;
                    }
                    i++;
                }
                // Permuting elements that are not in their final position
                i = 0;
                int[] perm = null;
                for (E e : col) {
                    if (alreadyPresentSize == 0)
                        break;
                    if (!bs.get(i)) {
                        int old = indexOf(e);
                        if (old == i)
                            nextUpdate(i);
                        else if (perm == null || perm[old] != i) {
                            E previous = backingList.set(i, e);
                            backingList.set(old, previous);
                            if (perm == null) {
                                perm = new int[size()];
                                for (int j = 0; j < perm.length; j++)
                                    perm[j] = j;
                            }
                            perm[old] = i;
                            alreadyPresentSize--;
                        }
                    }
                    i++;
                }
                if (perm != null)
                    nextPermutation(0, perm.length, perm);
            } finally {
                endChange();
            }
        }
        return true;
    }
}
