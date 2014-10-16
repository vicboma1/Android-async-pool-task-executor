package com.android.async.worker;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.Transcript;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@Config(emulateSdk = 17)
@RunWith(RobolectricTestRunner.class)
@SuppressLint("NewApi")
public class AsyncWorkerTest extends TestCase {

    private Transcript transcript;

    @Before
    public void setUp() throws Exception {
        transcript = new Transcript();
        Robolectric.getBackgroundScheduler().pause();
        Robolectric.getUiThreadScheduler().pause();
    }

    @Override
    public void tearDown() throws Exception {
        transcript.clear();
        transcript = null;
        Robolectric.getBackgroundScheduler().reset();
        Robolectric.getUiThreadScheduler().reset();
    }

    @Test
    public void executeOnExecutor() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 11);

        final String expected = "45";
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        final AsyncTaskInteger asyncTaskInteger = new AsyncTaskInteger();

        for (int i = 0; i < 1000; i++) {
            AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTaskInteger, params);
            assertEquals("Rejected executeTask", expected, executeTask.get(100, TimeUnit.MILLISECONDS));
        }
    }

    @Test
    public void execute() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 1);

        final String expected = "45";
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        final AsyncTaskInteger asyncTaskInteger = new AsyncTaskInteger();

        for (int i = 0; i < 1000; i++) {
            AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTaskInteger, params);
            transcript.assertEventsSoFar("onPreExecute");

            Robolectric.runBackgroundTasks();
            transcript.assertEventsSoFar("doInBackground " + expected);
            assertEquals("Rejected executeTask", expected, executeTask.get(100, TimeUnit.MILLISECONDS));

            Robolectric.runUiThreadTasks();
            transcript.assertEventsSoFar("onPostExecute " + expected);
        }
    }

    @Test
    public void executeReturnsAsyncTask() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 11);

        final String expected = "45";
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Robolectric.getBackgroundScheduler().unPause();
        for (int i = 0; i < 1000; i++) {
            final AsyncTaskInteger asyncTaskInteger = new AsyncTaskInteger();
            AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTaskInteger, params);
            assertThat(executeTask.get(), equalTo(expected));
        }
    }

    @Test
    public void executeOnExecutorReturnsAsyncTask() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 11);
        final String expected = "45";
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Robolectric.getBackgroundScheduler().unPause();
        for (int i = 0; i < 1000; i++) {
            final AsyncTaskInteger asyncTaskInteger = new AsyncTaskInteger();
            AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTaskInteger, params);
            assertThat(executeTask.get(), equalTo(expected));
        }
    }

    @Test
    public void executeStatusForAsyncTask() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 1);
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        final AsyncTaskInteger asyncTaskInteger = new AsyncTaskInteger();
        assertThat(asyncTaskInteger.getStatus(), is(AsyncTask.Status.PENDING));
        AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTaskInteger, params);
        assertThat(executeTask.getStatus(), is(AsyncTask.Status.RUNNING));
        Robolectric.getBackgroundScheduler().unPause();
        assertThat(executeTask.getStatus(), is(AsyncTask.Status.FINISHED));
    }

    @Test
    public void executeOnExecutorStatusForAsyncTask() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 11);
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        final AsyncTaskInteger asyncTaskInteger = new AsyncTaskInteger();
        assertThat(asyncTaskInteger.getStatus(), is(AsyncTask.Status.PENDING));
        AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTaskInteger, params);
        assertThat(executeTask.getStatus(), is(AsyncTask.Status.RUNNING));
        Robolectric.getBackgroundScheduler().unPause();
        executeTask.get();
        assertThat(executeTask.getStatus(), is(AsyncTask.Status.FINISHED));
    }

    @Test
    public void executeOnProgressUpdateQueue() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 1);
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        final String expected = "45";

        AsyncTask<Integer, Integer, String> asyncTask = new AsyncTaskInteger() {
            @Override
            protected String doInBackground(Integer... integers) {
                publishProgress(10);
                publishProgress(50);
                publishProgress(100);
                return "45";
            }
        };

        AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTask, params);
        transcript.assertEventsSoFar("onPreExecute");

        Robolectric.runBackgroundTasks();
        transcript.assertNoEventsSoFar();
        assertEquals("Rejected executeTask", expected, executeTask.get(100, TimeUnit.MILLISECONDS));

        Robolectric.runUiThreadTasks();
        transcript.assertEventsSoFar(
                "onProgressUpdate 10",
                "onProgressUpdate 50",
                "onProgressUpdate 100",
                "onPostExecute 45");
    }


    @Test
    public void executeOnExecutorOnProgressUpdateQueue() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 11);
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        final String expected = "45";

        AsyncTask<Integer, Integer, String> asyncTask = new AsyncTaskInteger() {
            @Override
            protected String doInBackground(Integer... integers) {
                publishProgress(10);
                publishProgress(50);
                publishProgress(100);
                return "45";
            }
        };

        AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTask, params);
        transcript.assertEventsSoFar("onPreExecute");

        Robolectric.runBackgroundTasks();
        transcript.assertNoEventsSoFar();
        assertEquals("Rejected executeTask", expected, executeTask.get(100, TimeUnit.MILLISECONDS));

        Robolectric.runUiThreadTasks();
        transcript.assertEventsSoFar(
                "onProgressUpdate 10",
                "onProgressUpdate 50",
                "onProgressUpdate 100",
                "onPostExecute 45");
    }

    @Test
    public void executeCancelBeforeBackground() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 1);
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        final AsyncTaskInteger asyncTaskInteger = new AsyncTaskInteger();
        for (int i = 0; i < 1000; i++) {
            final AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTaskInteger, params);
            transcript.assertEventsSoFar("onPreExecute");

            assertTrue(executeTask.cancel(true));
            assertTrue(executeTask.isCancelled());

            Robolectric.runBackgroundTasks();
            transcript.assertNoEventsSoFar();

            Robolectric.runUiThreadTasks();
            transcript.assertEventsSoFar("onCancelled");
        }
    }

    @Test
    public void executeOnExecuteCancelBeforeBackground() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 11);
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        final AsyncTaskInteger asyncTaskInteger = new AsyncTaskInteger();
        for (int i = 0; i < 1000; i++) {
            final AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTaskInteger, params);
            assertTrue(executeTask.cancel(true));
            assertTrue(executeTask.isCancelled());

            Robolectric.runBackgroundTasks();
            transcript.assertEventsSoFar("onPreExecute");

            Robolectric.runUiThreadTasks();
            transcript.assertEventsSoFar("onCancelled");
        }
    }

    @Test
    public void executeCancelBeforePostExecute() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 1);
        final String expected = "45";

        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        final AsyncTaskInteger asyncTaskInteger = new AsyncTaskInteger();
        for (int i = 0; i < 1000; i++) {
            final AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTaskInteger, params);
            transcript.assertEventsSoFar("onPreExecute");

            Robolectric.runBackgroundTasks();
            transcript.assertEventsSoFar("doInBackground " + expected);
            assertEquals("Rejected executeTask", expected, executeTask.get(100, TimeUnit.MILLISECONDS));

            assertFalse(executeTask.cancel(true));
            assertFalse(executeTask.isCancelled());

            Robolectric.runUiThreadTasks();
            transcript.assertEventsSoFar("onPostExecute " + expected);
        }
    }

    @Test
    public void executeOnExecutorCancelBeforePostExecute() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 11);
        final String expected = "45";
        final Integer[] params = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        final AsyncTaskInteger asyncTaskInteger = new AsyncTaskInteger();
        for (int i = 0; i < 1000; i++) {
            final AsyncTask<Integer, Integer, String> executeTask = AsyncWorker.execute(asyncTaskInteger, params);
            assertEquals("Rejected executeTask", expected, executeTask.get(100, TimeUnit.MILLISECONDS));
            assertFalse(executeTask.cancel(true));
            assertFalse(executeTask.isCancelled());
        }
    }


    private class AsyncTaskInteger extends AsyncTask<Integer, Integer, String> {

        public static final String ON_PRE_EXECUTE = "onPreExecute";
        public static final String DO_IN_BACKGROUND = "doInBackground";
        public static final String ON_POST_EXECUTE = "onPostExecute";
        public static final String ON_CANCELLED = "onCancelled";
        public static final String ON_PROGRESS_UPDATE = "onProgressUpdate";

        public AsyncTaskInteger() {
        }

        @Override
        protected String doInBackground(Integer... params) {
            int count = 0;

            try {
                for (int i = 0; i < params.length; i++)
                    count += params[i];
            } finally {
                final String result = String.valueOf(count);
                transcript.add(DO_IN_BACKGROUND + " " + result);
                return result;
            }
        }

        @Override
        protected void onPreExecute() {
            transcript.add(ON_PRE_EXECUTE);
        }

        @Override
        protected void onPostExecute(String file_url) {
            transcript.add(ON_POST_EXECUTE + " " + file_url);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            transcript.add(ON_PROGRESS_UPDATE + " " + values[0]);
        }

        @Override
        protected void onCancelled() {
            transcript.add(ON_CANCELLED);
        }
    }
}