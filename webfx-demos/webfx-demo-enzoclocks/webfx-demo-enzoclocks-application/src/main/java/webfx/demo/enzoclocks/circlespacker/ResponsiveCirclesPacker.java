package webfx.demo.enzoclocks.circlespacker;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Bruno Salmon
 */
final class ResponsiveCirclesPacker implements CirclesPacker {

    private final CirclesPacker[] circlesPackers = {
            new GridCirclesPacker(),
            new HoneyCombCirclesPacker()
    };
    private CirclesPacker maxRadiusCirclePacker;

    @Override
    public void setContainerSize(double width, double height) {
        Arrays.stream(circlesPackers).forEach(cp -> cp.setContainerSize(width, height));
    }

    @Override
    public void setCirclesCount(int count) {
        Arrays.stream(circlesPackers).forEach(cp -> cp.setCirclesCount(count));
    }

    @Override
    public boolean hasChanged() {
        return Arrays.stream(circlesPackers).anyMatch(CirclesPacker::hasChanged);
    }

    @Override
    public double getCirclesRadius() {
        maxRadiusCirclePacker = Arrays.stream(circlesPackers).max(Comparator.comparingInt(cp -> (int) cp.getCirclesRadius())).orElse(null);
        return maxRadiusCirclePacker == null ? 0 : maxRadiusCirclePacker.getCirclesRadius();
    }

    @Override
    public double getCircleCenterX(int index) {
        return maxRadiusCirclePacker.getCircleCenterX(index);
    }

    @Override
    public double getCircleCenterY(int index) {
        return maxRadiusCirclePacker.getCircleCenterY(index);
    }
}
