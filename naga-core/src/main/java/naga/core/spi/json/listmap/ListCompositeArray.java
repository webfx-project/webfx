package naga.core.spi.json.listmap;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class ListCompositeArray extends ListBasedCompositeArray {

    protected List<Object> list;

    protected ListCompositeArray() { // super constructor will call recreateEmptyNativeArray() to initialize the list
    }

    protected ListCompositeArray(List<Object> list) {
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

}
