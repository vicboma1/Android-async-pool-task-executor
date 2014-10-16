package com.android.async.pool;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.android.async.worker.AsyncWorker;
import com.android.utils.timeSpan.ICallBackTimeSpan;
import com.android.utils.timeSpan.ITimeSpan;
import com.android.utils.timeSpan.TimeSpan;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by vicboma on 16/10/14.
 */
public class AsyncPool {

    private static final int MILLISECONDS_THREAD_SLEEP_ = 100;
    private static final int MILLISECOND_THREAD_SLEEP_TASK_RESOLVED = 25;


    private static final int millisecondsLoop = 0;
    private static final int millisecondsStartDelay = 25;

    private static Queue<Callable> priorityQueue;
    private static ITimeSpan timeSpan;

    public static AsyncPool create() {
        return new AsyncPool(new ConcurrentLinkedQueue(), createTaskProcessor());
    }

    public AsyncPool(Queue<Callable> _priorityQueue, ITimeSpan _timeSpan) {
        priorityQueue = _priorityQueue;
        timeSpan = _timeSpan;
        timeSpan.run();
    }

    private static ITimeSpan createTaskProcessor() {
        return TimeSpan.create("AsyncPool", millisecondsLoop, millisecondsStartDelay, CALL_BACK);
    }

    public <T> void executeQueue(Callable callable) {
        priorityQueue.add(callable);
    }

    private static final ICallBackTimeSpan<Object> CALL_BACK = new ICallBackTimeSpan<Object>() {
        @Override
        public Object run() {
            final Queue<Callable> _priorityQueue = priorityQueue;

            if (_priorityQueue.isEmpty()) {
                timeSpan.sleepThread(MILLISECONDS_THREAD_SLEEP_);
                return null;
            }

            final int size = _priorityQueue.size();
            for (int i = 0; i < size; i++) {
                final Callable poll = _priorityQueue.poll();
                try {
                    AsyncWorker.execute(new CallableWorker(), poll);
                } catch (Exception e) {
                    System.err.printf("Taks Rejected ******************** " + poll.toString());
                    _priorityQueue.add(poll);
                } finally {
                }
            }
            timeSpan.sleepThread(MILLISECOND_THREAD_SLEEP_TASK_RESOLVED);
            return null;
        }
    };


    @SuppressLint("NewApi")
    private static class CallableWorker extends AsyncTask<Callable<Integer>, Void, Integer> {
        public CallableWorker() {
            super();
        }

        @Override
        protected Integer doInBackground(Callable<Integer>... params) {
            int result = 0;

            try {
                final Callable<Integer> param = params[0];
                result = param.call();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return result;
            }
        }

    }
}
