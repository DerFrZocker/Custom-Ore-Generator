package de.derfrzocker.custom.ore.generator.api;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class DebugLogger {

    private static final File DEBUG_LOG_FILE = new File("custom-ore-generaotr-debug.log");
    private static final BlockingQueue<String> DEBUG_LOG_QUEUE = new LinkedBlockingQueue<>();

    static {
        Thread LOGGER_THREAD = new Thread(() -> {
            if (!DEBUG_LOG_FILE.exists()) {
                try {
                    DEBUG_LOG_FILE.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try (FileWriter fileWriter = new FileWriter(DEBUG_LOG_FILE)) {
                while (true) {
                    try {
                        fileWriter.append(DEBUG_LOG_QUEUE.take());
                        fileWriter.append("\n");
                    } catch (InterruptedException e) {
                        fileWriter.close();
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        LOGGER_THREAD.setDaemon(true);
        LOGGER_THREAD.start();
    }

    private DebugLogger() {

    }

    public static void log(final String message) {
        DEBUG_LOG_QUEUE.add(Thread.currentThread().getName() + ": " + message);
    }
}
