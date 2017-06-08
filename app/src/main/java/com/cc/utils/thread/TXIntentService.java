package com.cc.utils.thread;

import android.app.IntentService;
import android.content.Intent;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * IntentService：Service + HandlerThread
 * <p>
 * 默认线程优先级为 THREAD_PRIORITY_DEFAULT = 0
 * <p>
 * 开启服务，进程优先级会提升；
 * <p>
 * 无需手动关闭，执行完之后自动结束。
 * <p>
 * 适用场景：处理与UI无关的多任务场景
 * <p>
 * Created by Cheng on 2017/6/8.
 */
public class TXIntentService extends IntentService {

    private static final String TAG = "TXIntentService";

    public TXIntentService() {
        super(TAG);
    }

    public TXIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "TXIntentService onCreate");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "TXIntentService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // TXIntentService onHandleIntent 0
        Log.d(TAG, "TXIntentService onHandleIntent " + Process.getThreadPriority(Process.myTid()));
    }
}
