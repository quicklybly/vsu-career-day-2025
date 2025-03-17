package org.quicklybly.concurrency;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

abstract class SimpleStackTest {
    abstract SimpleStack impl();

    @Test
    public void test() {
        var stack = impl();

        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.pull());
        stack.push(3);
        assertEquals(3, stack.pull());
        assertEquals(1, stack.pull());
        assertNull(stack.pull());
    }
}