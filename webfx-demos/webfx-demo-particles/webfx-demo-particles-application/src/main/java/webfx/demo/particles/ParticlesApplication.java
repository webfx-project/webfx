package webfx.demo.particles;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public class ParticlesApplication extends Application {

    private final static int MAX_PARTICLES = 280;
    private final static Color[] COLOURS = { Color.web("#69D2E7"), Color.web("#A7DBD8"), Color.web("#E0E4CC"), Color.web("#F38630"), Color.web("#FA6900"), Color.web("#FF4E50"), Color.web("#F9D423") };

    private final Canvas canvas = new Canvas();
    private final GraphicsContext ctx = canvas.getGraphicsContext2D();

    private final Queue<Particle> particles = new LinkedList<>();
    private final Queue<Particle> pool = new LinkedList<>();

    @Override
    public void start(Stage primaryStage) {
        // Creating the scene with the specified size (this size is ignored if running in the browser)
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        // Reading back the scene size which may finally be different in the case we run in the browser
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        root.setBackground(new Background(new BackgroundFill(Color.web("#222"), null, null)));

        // Set off some initial particles.
        for (int i = 0; i < 20; i++ ) {
            double x = ( canvas.getWidth()  * 0.5 ) + (Math.random() - 0.5) * 200;
            double y = ( canvas.getHeight() * 0.5 ) + (Math.random() - 0.5) * 200;
            spawn(x, y);
        }

        canvas.setOnMouseMoved(e -> onMouseOrTouchMoved(e.getX(), e.getY()));
        canvas.setOnTouchMoved(e -> onMouseOrTouchMoved(e.getTouchPoint().getX(), e.getTouchPoint().getY()));

        new AnimationTimer() {
            long startTime, framesCount;
            @Override
            public void handle(long now) {
                if (startTime == 0)
                    startTime = now;
                // Frame control: retro counting the frames the browser may have missed between 2 calls
                long newFramesCount = (now - startTime) / 16_000_000; // 60 FPS = 1 frame every 16ms
                while (framesCount < newFramesCount) {
                    update(); // 1 call per frame
                    framesCount++;
                }
                draw();
            }
        }.start();

        primaryStage.show();
    }

    private void onMouseOrTouchMoved(double x, double y) {
        double max = 1 + Math.random() * 3;
        for (int j = 0; j < max; j++)
            spawn(x, y);
    }

    private void spawn(double x, double y) {
        if (particles.size() >= MAX_PARTICLES)
            pool.offer(particles.poll());

        Particle particle = pool.isEmpty() ? new Particle() : pool.poll();
        particle.init( x, y, 5 + Math.random() * 35);

        particle.wander = 0.5 + Math.random() * 1.5;
        particle.color = COLOURS[(int) Math.floor(Math.random() * COLOURS.length)];
        particle.drag = 0.9 + Math.random() * 0.09;

        double theta = Math.random() * 2 * Math.PI;
        double force = 2 + Math.random() * 6;

        particle.vx = Math.sin( theta ) * force;
        particle.vy = Math.cos( theta ) * force;

        particles.offer(particle);
    }

    private void update() {
        for (Iterator<Particle> it = particles.iterator(); it.hasNext(); ) {
            Particle p = it.next();
            if (p.alive)
                p.move();
            else {
                it.remove();
                pool.offer(p);
            }
        }
    }

    private void draw() {
        ctx.setGlobalBlendMode(BlendMode.SRC_OVER); // Note: Necessary to do this in JavaFx otherwise clearRect() does nothing after BlendMode.LIGHTEN has been applied
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setGlobalBlendMode(BlendMode.LIGHTEN);  // Note: this blend mode implementation is slower in JavaFx than in the browser
        particles.forEach(p -> p.draw(ctx));
    }

    static final class Particle {
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
}
