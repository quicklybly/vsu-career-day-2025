package org.quicklybly.concurrency;

/**
 * Implement non-blocking stack
 */
public interface SimpleStack {
    void push(Integer value);
    Integer pull();
}

