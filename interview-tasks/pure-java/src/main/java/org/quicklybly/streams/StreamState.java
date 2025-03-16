package org.quicklybly.streams;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * How many times will the following code sort the stream?
 */
public class StreamState {

    public static void main(String[] args) {
        Comparator<Integer> comparator = (o1, o2) -> {
            System.out.println("Sorting" + o1 + " " + o2);
            return o1 - o2;
        };

        SortedSet<Integer> data = new TreeSet<>(List.of(5, 4, 3, 2, 1));

        var c = data.stream()
                .sorted(comparator)
                .filter(i -> i % 2 != 0)
                .sorted(comparator)
                .map(i -> i * 2)
                .sorted(comparator)
                .toList();
        System.out.println(c);
        System.exit(0);
    }
}
