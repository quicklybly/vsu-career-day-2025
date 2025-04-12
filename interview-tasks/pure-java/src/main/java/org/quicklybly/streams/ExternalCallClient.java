package org.quicklybly.streams;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ExternalCallClient {

    public CompletableFuture<Integer> call(Integer parameter) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep interrupted", e);
            }
            return parameter;
        });
    }
}
