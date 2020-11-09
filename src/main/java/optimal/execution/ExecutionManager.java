package optimal.execution;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutionManager {
    private static ThreadPoolExecutor EXECUTOR = null;

    public static void createThreadPool(int amountOfThreads) {
        if (EXECUTOR != null) {
            throw new IllegalStateException("Stop previous thread pool at first");
        }
        EXECUTOR = (ThreadPoolExecutor) Executors.newFixedThreadPool(amountOfThreads);
    }

    public static Future<?> executeOnThreadPool(@NotNull Runnable runnable) {
        return EXECUTOR.submit(runnable);
    }

    public static void stopThreadPool() {
        EXECUTOR.shutdown();
        try {
            if (!EXECUTOR.awaitTermination(1, TimeUnit.MINUTES)) {
                EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            EXECUTOR.shutdownNow();
        }
        EXECUTOR = null;
    }
}
