package org.quicklybly.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * What is the output of the following code and how to fix it?
 */
public class Counter {
    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        var executor = Executors.newFixedThreadPool(100);
        int targetCount = 100_000;

        for (int i = 0; i < targetCount; i++) {
            executor.submit(() -> {
                count++;
            });
        }

        executor.shutdown();
        boolean result = executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println(count);
        System.exit(result ? 0 : 1);
    }
}
