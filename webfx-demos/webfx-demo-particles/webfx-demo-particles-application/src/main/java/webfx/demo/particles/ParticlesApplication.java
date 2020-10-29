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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class ParticlesApplication extends Application {

    private final static int MAX_PARTICLES = 280;
    private final static Color[] COLOURS = { Color.web("#69D2E7"), Color.web("#A7DBD8"), Color.web("#E0E4CC"), Color.web("#F38630"), Color.web("#FA6900"), Color.web("#FF4E50"), Color.web("#F9D423") };
    private final static Background BLACK_BACKGROUND = new Background(new BackgroundFill(Color.BLACK, null, null));

    private final Canvas canvas = new Canvas();
    private final GraphicsContext ctx = canvas.getGraphicsContext2D();

    private final List<Particle> particles = new ArrayList<>();
    private final List<Particle> pool = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // Creating the scene with the specified size (this size is ignored if running in the browser)
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        // Reading back the scene size which may finally be different in the case we run in the browser
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        root.setBackground(BLACK_BACKGROUND);

        // Set off some initial particles.
        for (int i = 0; i < 20; i++ ) {
            double x = ( canvas.getWidth()  * 0.5 ) + (Math.random() - 0.5) * 200;
            double y = ( canvas.getHeight() * 0.5 ) + (Math.random() - 0.5) * 200;
            spawn( x, y );
        }

        canvas.setOnMouseMoved(e -> onMouseOrTouchMoved(e.getX(), e.getY()));
        canvas.setOnTouchMoved(e -> onMouseOrTouchMoved(e.getTouchPoint().getX(), e.getTouchPoint().getY()));

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
            }
        }.start();

        primaryStage.show();
    }

    private void onMouseOrTouchMoved(double x, double y) {
        double max = 1 + Math.random() * 3;
        for (int j = 0; j < max; j++) {
            spawn(x, y);
        }
    }

    private void spawn(double x, double y) {
        double theta, force;

        if ( particles.size() >= MAX_PARTICLES )
            pool.add( particles.remove(0) );

        Particle particle = pool.isEmpty() ? new Particle() : pool.remove(0);
        particle.init( x, y, 5 + Math.random() * 35);

        particle.wander = 0.5 + Math.random() * 1.5;
        particle.color = COLOURS[(int) Math.floor(Math.random() * COLOURS.length)];
        particle.drag = 0.9 + Math.random() * 0.09;

        theta = Math.random() * 2 * Math.PI;
        force = 2 + Math.random() * 6;

        particle.vx = Math.sin( theta ) * force;
        particle.vy = Math.cos( theta ) * force;

        particles.add( particle );
    }

    private void update() {

        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            if (p.alive)
                p.move();
            else {
                particles.remove(i--);
                pool.add(p);
            }
        }
    }

    private void draw() {
        ctx.setGlobalBlendMode(BlendMode.SRC_OVER);  // Note: Necessary to do this in JavaFx otherwise clearRect() does nothing
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setGlobalBlendMode(BlendMode.LIGHTEN); // Note: implementation is much slower in JavaFx than in the browser
        particles.forEach(p -> p.draw(ctx));
    }
}
