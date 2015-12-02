package naga.core.spi.json.javaplat.listmap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class ListJsonArray extends ListBasedJsonArray {

    protected List list;

    protected ListJsonArray() { // super constructor will call recreateEmptyNativeArray() to initialize the list
    }

    protected ListJsonArray(List list) {
        this.list = list;
    }

    @Override
    public List getList() {
        return list;
    }

    @Override
    protected Object getNativeArray() {
        return list;
    }

    @Override
    protected void recreateEmptyNativeArray() {
        list = new ArrayList<>();
    }

    @Override
    protected void deepCopyNativeArray() {
        list = ListMapUtil.convertList(list);
    }

}
