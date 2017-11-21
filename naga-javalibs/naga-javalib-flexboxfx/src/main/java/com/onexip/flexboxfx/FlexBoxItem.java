package com.onexip.flexboxfx;

import javafx.scene.Node;

/**
 * Created by TB on 19.10.16.
 */
class FlexBoxItem
{
    public int order = 0;
    public double grow = 0;
    public Node node;
    public double minWidth=0;

    public FlexBoxItem(Node node)
    {
        this.node = node;
    }
}
