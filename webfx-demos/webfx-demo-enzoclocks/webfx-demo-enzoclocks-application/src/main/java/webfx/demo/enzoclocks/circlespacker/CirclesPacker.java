package webfx.demo.enzoclocks.circlespacker;

/**
 * @author Bruno Salmon
 */
interface CirclesPacker {

    void setContainerSize(double width, double height);

    void setCirclesCount(int count);

    boolean hasChanged();

    double getCirclesRadius();

    double getCircleCenterX(int index);

    double getCircleCenterY(int index);

}
