package com.cc.utils.thread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程异步处理相关Demo
 * <p>
 * Created by Cheng on 2017/6/8.
 */
public class TXThreadDemoActivity extends Activity {

    private static final String TAG = "TXThreadDemoActivity";

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXThreadDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        threadDemo();

        asyncTaskDemo();

        handleThreadDemo();

        intentServiceDemo();

        threadPoolExecutor();
    }

    /**
     * Thread：不建议直接使用。
     *
     * 线程优先级 跟随 创建线程所在的线程优先级，所以如果是在UI线程创建线程，那么默认优先级为 -10
     *
     * 如果使用，最好通过Process.setThreadPriority设置线程优先级
     *
     * 适用场景：单个任务
     */
    private void threadDemo() {
        new Thread("normal thread") {
            @Override
            public void run() {
                super.run();

                // normal thread Before -10
                Log.d(TAG, "normal thread Before " + Process.getThreadPriority(Process.myTid()));

                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                // normal thread After 10
                Log.d(TAG, "normal thread After " + Process.getThreadPriority(Process.myTid()));
            }
        }.run();
    }

    /**
     * AsyncTask：默认线程优先级 THREAD_PRIORITY_BACKGROUND = 10
     *
     * API 1.5 串行
     * 
     * API 1.5 ~ 3.0 并行
     * 
     * API 3.0 ~ now 串行
     *
     * 实现多线程，可传入自定义线程池
     *
     * 适用场景：单个任务
     */
    private void asyncTaskDemo() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void...params) {

                // asyncTask doInBackground Before 10
                Log.d(TAG, "asyncTask doInBackground Before " + Process.getThreadPriority(Process.myTid()));

                Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);

                // asyncTask doInBackground After 19
                Log.d(TAG, "asyncTask doInBackground After " + Process.getThreadPriority(Process.myTid()));

                new Thread("thread in async") {
                    @Override
                    public void run() {
                        super.run();

                        // thread in async 19
                        Log.d(TAG, "thread in async " + Process.getThreadPriority(Process.myTid()));
                    }
                }.run();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                // onPostExecute 10
                Log.d(TAG, "onPostExecute " + Process.getThreadPriority(Process.myTid()));
            }
        }.execute();
    }

    /**
     * HandleThread：默认线程优先级 THREAD_PRIORITY_DEFAULT = 0
     *
     * 不退出的前提下一直存在，避免线程相关的对象频繁重建和销毁造成的资源消耗。
     *
     * 适用场景：串行处理多任务的场景
     */
    private void handleThreadDemo() {
        HandlerThread handleThread = new HandlerThread("normal handleThread");
        handleThread.start();

        new Handler(handleThread.getLooper()) {
        }.post(new Runnable() {
            @Override
            public void run() {
                // handleThreadDemo normal handleThread 0
                Log.d(TAG, "handleThreadDemo normal handleThread " + Process.getThreadPriority(Process.myTid()));
            }
        });

        HandlerThread handleThread1 = new HandlerThread("handleThreadBackground", Process.THREAD_PRIORITY_BACKGROUND);
        handleThread1.start();

        new Handler(handleThread1.getLooper()) {
        }.post(new Runnable() {
            @Override
            public void run() {
                // handleThreadDemo handleThreadBackground 10
                Log.d(TAG, "handleThreadDemo handleThreadBackground " + Process.getThreadPriority(Process.myTid()));
            }
        });
    }

    /**
     * IntentService：Service + HandlerThread
     *
     * 默认线程优先级为 THREAD_PRIORITY_DEFAULT = 0
     *
     * 开启服务，进程优先级会提升；
     *
     * 无需手动关闭，执行完之后自动结束。
     *
     * 适用场景：处理与UI无关的多任务场景
     */
    private void intentServiceDemo() {
        Intent intent = new Intent(this, TXIntentService.class);
        startService(intent);
    }

    /**
     * ThreadPoolExecutor，默认线程优先级为 THREAD_PRIORITY_DEFAULT = 0
     *
     * 适用场景：并行多任务场景
     */
    private void threadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(2, 3, 10000, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(128));
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // threadPoolExecutor 0
                Log.d(TAG, "threadPoolExecutor " + Process.getThreadPriority(Process.myTid()));
            }
        });
    }
}
