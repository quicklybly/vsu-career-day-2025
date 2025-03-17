package org.quicklybly.concurrency;

class SimpleStackImplTest extends SimpleStackTest {

    @Override
    SimpleStack impl() {
        return new SimpleStackImpl();
    }
}
