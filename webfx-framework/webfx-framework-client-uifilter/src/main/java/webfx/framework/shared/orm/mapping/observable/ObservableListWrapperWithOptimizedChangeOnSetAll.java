package webfx.framework.shared.orm.mapping.observable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

final class ObservableListWrapperWithOptimizedChangeOnSetAll<E> extends ObservableListWrapper<E> {

    ObservableListWrapperWithOptimizedChangeOnSetAll() {
        this(new ArrayList<>());
    }

    ObservableListWrapperWithOptimizedChangeOnSetAll(List<E> list) {
        super(list);
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
                for (E e : col) {
                    if (alreadyPresentSize == 0)
                        break;
                    if (!bs.get(i)) {
                        int old = indexOf(e);
                        if (old == i)
                            nextUpdate(i);
                        else {
                            E previous = backingList.set(i, e);
                            backingList.set(old, previous);
                            nextPermutation(old, old + 1, new int[]{i});
                            alreadyPresentSize--;
                        }
                    }
                    i++;
                }
            } finally {
                endChange();
            }
        }
        return true;
    }
}
