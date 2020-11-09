package org.webcrawly.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * all executor code here
 */
public class ExecutionEnvironment {

    private final ExecutorService executorService;
    private final long timeout;
    private final TimeUnit timeUnit;

    public ExecutionEnvironment(ExecutorService executorService, long timeout, TimeUnit timeUnit) {
        this.executorService = executorService;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public void submit(Runnable task){
        executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    /**
     * @return whether work was completed
     */
    public boolean await() {
        try {
            return executorService.awaitTermination(timeout, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }
}
