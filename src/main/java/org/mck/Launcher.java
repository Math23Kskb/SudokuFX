package org.mck;

import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple launcher class used as the main entry point for the shaded JAR.
 * This works around potential classloader issues with JavaFX in shaded JARs
 * by calling the main method of the actual Application class.
 */
public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {

        String dsn = System.getenv("SENTRY_DSN");

        if(dsn == null || dsn.isEmpty()) {
            logger.error("Sentry DSN is not set. Please set the environment variable SENTRY_DSN");
            return;
        }

        Sentry.init(options -> {
            options.setDsn(dsn);
            options.setTracesSampleRate(1.0);
            options.setDebug(true);
        });

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            logger.error("Uncaught exception in thread {}", thread.getName(), throwable);
            Sentry.captureException(throwable);
        });

        try {
            Main.main(args);
        } catch (Exception e) {
            logger.error("Exception occurred in Launcher.main", e);
            Sentry.captureException(e);
        } finally {
            Sentry.close();
        }
    }
}