package org.quicklybly.streams;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Review this code and explain what is wrong with it.
 */
public class ExternalCall {

    public static void main(String[] args) {
        var client = new ExternalCallClient();

        List<CompletableFuture<Integer>> futures = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .map(client::call)
                .toList();

        CompletableFuture<List<Integer>> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(_ -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());

        allFutures.thenAccept(results -> results.stream()
                        .filter(p -> p % 2 != 0)
                        .limit(3)
                        .forEach(System.out::println))
                .join();
    }
}
