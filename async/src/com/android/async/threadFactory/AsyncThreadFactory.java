package com.android.async.threadFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vicboma on 15/10/14.
 */
public class AsyncThreadFactory implements ThreadFactory {

    private AtomicInteger uid = new AtomicInteger(0);

    public static AsyncThreadFactory create(){
        return new AsyncThreadFactory();
    }

    public AsyncThreadFactory() {
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, "Async thread: "+uid.getAndIncrement());
    }
}


