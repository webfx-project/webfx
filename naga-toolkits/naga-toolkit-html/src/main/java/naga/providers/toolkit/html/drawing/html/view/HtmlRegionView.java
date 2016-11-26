package naga.providers.toolkit.html.drawing.html.view;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.layout.Region;
import naga.toolkit.drawing.spi.view.base.RegionViewBase;
import naga.toolkit.drawing.spi.view.base.RegionViewMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlRegionView
        extends HtmlNodeView<Region, RegionViewBase, RegionViewMixin>
        implements RegionViewMixin {

    public HtmlRegionView() {
        super(new RegionViewBase(), HtmlUtil.createAbsolutePositionDiv());
    }
}
