package com.android.async.pool;

import android.annotation.SuppressLint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

@Config(emulateSdk = 17)
@RunWith(RobolectricTestRunner.class)
@SuppressLint("NewApi")
public class AsyncPoolTest {

    private AsyncPool asyncPool;

    @Before
    public void setUp() throws Exception {
        asyncPool = AsyncPool.create();
    }

    @After
    public void tearDown() throws Exception {
        asyncPool = null;
    }

    @Test
    public void testCreate() throws Exception {
        final AsyncPool _asyncPool = AsyncPool.create();
        assertNotSame("Rejected factory method pattern", asyncPool, _asyncPool);
    }

    @Test
    public void testExecuteQueue() throws Exception {
        final AtomicInteger uid = new AtomicInteger(0);
        final int expected = 1000;
        for(int i = 0; i< expected; i++)
            asyncPool.executeQueue(() -> uid.getAndIncrement());

        new CountDownLatch(1).await(2000, TimeUnit.MILLISECONDS);

        assertEquals("Rejected execute callbacks",expected,uid.get());
    }
}