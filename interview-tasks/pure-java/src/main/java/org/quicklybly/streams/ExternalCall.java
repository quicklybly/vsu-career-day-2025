package org.quicklybly.streams;

import java.util.stream.Stream;

/**
 * Review this code and explain what is wrong with it.
 */
public class ExternalCall {

    public static void main(String[] args) {
        var client = new ExternalCallClient();

        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .map(p -> client.call(p).join())
                .filter(p -> p % 2 != 0)
                .limit(3)
                .forEach(System.out::println);
    }
}
