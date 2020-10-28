package webfx.demos.particles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
final class Particle {

    boolean alive;
    double x, y, radius;
    double vx, vy, wander, theta, drag;
    Color color;

    void init(double x, double y, double radius) {
        this.x = x;
        this.y = y;

        alive = true;

        this.radius = radius;
        wander = 0.15;
        theta = Math.random() * Math.PI * 2;
        drag = 0.92;
        color = Color.WHITE;

        vx = 0.0;
        vy = 0.0;
    }

    void move() {
        x += vx;
        y += vy;

        vx *= drag;
        vy *= drag;

        theta += (Math.random() -0.5) * wander;
        vx += Math.sin( theta ) * 0.1;
        vy += Math.cos( theta ) * 0.1;

        radius *= 0.96;
        alive = radius > 0.5;
    }

    void draw(GraphicsContext ctx) {
        ctx.beginPath();
        ctx.arc(x, y, radius, radius, 0, 360);
        ctx.setFill(color);
        ctx.fill();
    }

}
