package org.quicklybly.streams;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * How many times will the following code sort the stream?
 */
public class StreamState {

    public static void main(String[] args) {
        SortedSet<Integer> data = new TreeSet<>(List.of(5, 4, 3, 2, 1));

        var c = data.stream()
                .sorted()
                .filter(i -> i % 2 != 0)
                .sorted()
                .map(i -> i * 2)
                .sorted()
                .toList();
        System.out.println(c);
        System.exit(0);
    }
}
