package com.cc.utils.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

/**
 * 通用缓存,使用sp实现,不能存储超大的value
 * <p>
 * Created by Cheng on 16/11/11.
 */
public class TXCommonCache implements TXBaseCacheInterface {

    private static final String TAG = "TXCommonCache";

    private static final String TX_COMMON_CACHE = "tx.common.cache";
    private SharedPreferences mCache;

    private static class InstanceHolder {
        public final static TXCommonCache instance = new TXCommonCache();
    }

    public static TXCommonCache getInstance() {
        return InstanceHolder.instance;
    }

    public void init(Context context) {
        if (context == null) {
            Log.e(TAG, "init fail context is null");
            return;
        }

        mCache = context.getSharedPreferences(TX_COMMON_CACHE, Context.MODE_PRIVATE);
    }

    @Override
    public String getString(String key, String defaultValue) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        return mCache.getString(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        return mCache.getBoolean(key, defaultValue);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        return mCache.getInt(key, defaultValue);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        return mCache.getLong(key, defaultValue);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        return mCache.getFloat(key, defaultValue);
    }

    @Override
    public boolean contains(String key) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return false;
        }

        return mCache.contains(key);
    }

    @Override
    public void remove(String key) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return;
        }

        mCache.edit().remove(key).apply();
    }

    @Override
    public void putString(String key, String value) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return;
        }

        mCache.edit().putString(key, value).apply();
    }

    @Override
    public void putBoolean(String key, boolean value) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return;
        }

        mCache.edit().putBoolean(key, value).apply();
    }

    @Override
    public void putInt(String key, int value) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return;
        }

        mCache.edit().putInt(key, value).apply();
    }

    @Override
    public void putLong(String key, long value) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return;
        }

        mCache.edit().putLong(key, value).apply();
    }

    @Override
    public void putFloat(String key, float value) {
        if (mCache == null || TextUtils.isEmpty(key)) {
            return;
        }

        mCache.edit().putFloat(key, value).apply();
    }
}
