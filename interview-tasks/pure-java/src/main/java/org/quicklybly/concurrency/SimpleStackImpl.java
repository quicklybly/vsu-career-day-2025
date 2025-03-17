package org.quicklybly.concurrency;

import java.util.concurrent.atomic.AtomicReference;

public class SimpleStackImpl implements SimpleStack {
    private static class Node {
        int value;
        Node prev;

        Node(int value, Node prev) {
            this.value = value;
            this.prev = prev;
        }

        Node(int value) {
            this(value, null);
        }
    }

    private AtomicReference<Node> head;

    @Override
    public void push(Integer value) {
        Node newHead = new Node(value);
        Node prevHead;

        do {
            prevHead = head.get();
            newHead.prev = prevHead;
        } while (head.compareAndSet(prevHead, newHead));
    }

    @Override
    public Integer pull() {
        Node currHead;
        Node newHead;
        do {
            currHead = head.get();
            if (currHead == null) {
                return null;
            }
            newHead = currHead.prev;
        } while (head.compareAndSet(currHead, newHead));
        return 0;
    }
}
