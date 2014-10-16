package com.android.async.executor;

import junit.framework.TestCase;

import java.util.concurrent.ThreadPoolExecutor;

public class AsyncExecutorTest extends TestCase {

    private AsyncExecutor asyncExecutor;

    public void setUp() throws Exception {
        asyncExecutor = AsyncExecutor.create();
    }

    public void tearDown() throws Exception {
        asyncExecutor = null;
    }

    public void testCreatePattern() throws Exception {
        final AsyncExecutor _asyncExecutor = AsyncExecutor.create();
        assertNotSame("Rejected factory method pattern", asyncExecutor, _asyncExecutor);
    }

    public void testThreadPoolExecutor() throws Exception {
        final ThreadPoolExecutor threadPoolExecutor = asyncExecutor.threadPoolExecutor();
        assertNotNull("Rejected threadPoolExecutor",threadPoolExecutor);
    }
}