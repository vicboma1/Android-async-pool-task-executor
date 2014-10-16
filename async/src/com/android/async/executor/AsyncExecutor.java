package com.android.async.executor;

import com.android.async.threadFactory.AsyncThreadFactory;
import com.android.utils.Processor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by vicboma on 15/10/14.
 */
public class AsyncExecutor {

    private static final int AVAIBLE_THREADS = Processor.deviceCore();
    private static final int CORE_POOL_SIZE = (AVAIBLE_THREADS != 1) ? (AVAIBLE_THREADS / 2) : 1;
    private static final int MAXIMUM_POOL_SIZE = (AVAIBLE_THREADS != 1) ? (AVAIBLE_THREADS - 1) : 1;
    private static final int KEEP_ALIVE = 256;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    protected static ThreadPoolExecutor threadPoolExecutor;

    public static AsyncExecutor create() {
        return new AsyncExecutor(
                new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TIME_UNIT,
                        new LinkedBlockingQueue(),
                        AsyncThreadFactory.create()
                )
        );
    }

    public AsyncExecutor(ThreadPoolExecutor threadPoolExec) {
        threadPoolExecutor = threadPoolExec;
    }

    public ThreadPoolExecutor threadPoolExecutor() {
        return threadPoolExecutor;
    }

}
