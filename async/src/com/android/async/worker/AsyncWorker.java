package com.android.async.worker;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import com.android.async.executor.AsyncExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by vicboma on 15/10/14.
 */
public class AsyncWorker {

    private static AsyncExecutor asyncExecutor = AsyncExecutor.create();

    /**
     * AsyncTask for Android version with executor
     * @param threadPoolExecutor
     * @param <P> Params
     * @param <S> Status
     * @param <R> Result
     * @return asyncTask
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static <P,S,R > AsyncTask<P, S, R> execute(AsyncTask<P, S, R> task, ThreadPoolExecutor threadPoolExecutor, P... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(threadPoolExecutor, params);
        else
            task.execute(params); //SerialExecutor by default

        return task;
    }

    /**
     * AsyncTask for Android version
     * @param <P> Params
     * @param <S> Status
     * @param <R> Result
     * @return asyncTask
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static <P,S,R > AsyncTask<P, S, R> execute(AsyncTask<P, S, R> task, P... params) {
        return execute(task, asyncExecutor.threadPoolExecutor(), params);
    }

}
