package javafx.scene.transform;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasPivotXProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasPivotYProperty;

/**
 * @author Bruno Salmon
 */
abstract class PivotTransform extends Transform implements
        HasPivotXProperty,
        HasPivotYProperty {

    PivotTransform() {
    }

    PivotTransform(double pivotX, double pivotY) {
        setPivotX(pivotX);
        setPivotY(pivotY);
    }

    final DoubleProperty pivotXProperty = new SimpleDoubleProperty(0d) {
        @Override
        protected void invalidated() {
            transformChanged();
        }
    };

    @Override
    public DoubleProperty pivotXProperty() {
        return pivotXProperty;
    }

    final DoubleProperty pivotYProperty = new SimpleDoubleProperty(0d) {
        @Override
        protected void invalidated() {
            transformChanged();
        }
    };

    @Override
    public DoubleProperty pivotYProperty() {
        return pivotYProperty;
    }


}
