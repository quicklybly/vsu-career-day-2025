package org.quicklybly.streams;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ExternalCallClient {

    public CompletableFuture<Integer> call(Integer parameter) {
        try {
            Thread.sleep(Duration.ofSeconds(3));
            return CompletableFuture.completedFuture(parameter);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
