package dev.webfx.platform.shared.services.json.spi.impl.listmap;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class ListJsonArray extends ListBasedJsonArray {

    protected List<Object> list;

    protected ListJsonArray() { // super constructor will call recreateEmptyNativeArray() to initialize the list
    }

    protected ListJsonArray(List<Object> list) {
        super(list);
    }

    @Override
    public List<Object> getList() {
        return list;
    }

    @Override
    protected void setList(List<Object> list) {
        this.list = list;
    }

    @Override
    public List<Object> getNativeElement() {
        return list;
    }

    // Added default implementation due to TeaVm bug
    @Override
    public Object javaToNativeScalar(Object scalar) {
        return scalar;
    }
}
