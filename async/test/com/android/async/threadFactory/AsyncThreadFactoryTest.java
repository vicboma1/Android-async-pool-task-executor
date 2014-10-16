package com.android.async.threadFactory;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

public class AsyncThreadFactoryTest extends TestCase {

    private AsyncThreadFactory asyncThreadFactory;

    public void setUp() throws Exception {
        asyncThreadFactory = AsyncThreadFactory.create();
    }

    public void tearDown() throws Exception {
        asyncThreadFactory = null;
    }

    public void testCreatePattern() throws Exception {
        final AsyncThreadFactory _asyncThreadFactory = AsyncThreadFactory.create();
        assertNotSame("Rejected factory method pattern", asyncThreadFactory, _asyncThreadFactory);
    }

    public void testNewThread() throws Exception {
        Set<Thread> list = new HashSet();
        final int size = 1000;
        for (int i = 0; i < size; i++) {
            final Thread thread = this.asyncThreadFactory.newThread(() -> {
            });
            if (list.contains(thread))
                fail("Mutable thread create");
            else list.add(thread);
        }
        assertEquals(size, list.size());
    }
}