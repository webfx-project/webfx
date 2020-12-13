package dev.webfx.platform.vertx.services.appcontainer.spi.impl;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.function.Consumer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public final class VertxRunner {

    public static void runClusteredVerticle(Class clazz) {
        runVerticle(clazz, new VertxOptions().setClustered(true), null);
    }

    public static void runVerticle(Class clazz) {
        runVerticle(clazz, null);
    }

    public static void runVerticle(Class clazz, DeploymentOptions options) {
        runVerticle(clazz, new VertxOptions().setClustered(false), options);
    }

    public static void runVerticle(Class clazz, VertxOptions options, DeploymentOptions deploymentOptions) {
        runVerticle(clazz.getName(), options, deploymentOptions);
    }

    public static void runVerticle(String verticleID, VertxOptions options, DeploymentOptions deploymentOptions) {
        if (options == null) {
            // Default parameter
            options = new VertxOptions();
        }

        /*
        // Smart cwd detection
        // Based on the current directory (.) and the desired directory (exampleDir), we try to compute the vertx.cwd
        // directory:
        try {
            // We need to use the canonical file. Without the file name is .
            File current = new File(".").getCanonicalFile();
            if (exampleDir.startsWith(current.getName()) && !exampleDir.equals(current.getName())) {
                exampleDir = exampleDir.substring(current.getName().length() + 1);
            }
        } catch (IOException e) {
            // Ignore it.
        }
        System.setProperty("vertx.cwd", exampleDir);
        */

        Consumer<Vertx> runner = vertx -> {
            try {
                if (deploymentOptions != null) {
                    vertx.deployVerticle(verticleID, deploymentOptions);
                } else {
                    vertx.deployVerticle(verticleID);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };
        if (options.isClustered()) {
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                } else {
                    res.cause().printStackTrace();
                }
            });
        } else {
            Vertx vertx = Vertx.vertx(options);
            runner.accept(vertx);
        }
    }
}