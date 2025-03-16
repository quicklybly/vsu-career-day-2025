package org.quicklybly.streams;

import java.util.stream.Stream;

/**
 * What is the output of the following code?
 */
public class TellMeOutput {
    public static void main(String[] args) {
        var result = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(i -> i % 2 == 0)
                .peek(System.out::println)
                .map(i -> i * 2);
        System.out.print(result);
        System.exit(0);
    }
}
