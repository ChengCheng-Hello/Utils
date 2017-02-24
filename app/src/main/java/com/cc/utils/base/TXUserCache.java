package com.cc.utils.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.cc.utils.manager.TXCacheManager;
import com.cc.utils.model.TXDataModel;
import com.cc.utils.utils.TXJsonUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import cache.DiskCache;

/**
 * 和用户绑定的缓存,使用DiskCache实现
 * <p>
 * Created by Cheng on 16/11/11.
 */
public class TXUserCache implements TXBaseCacheInterface {

    private static final String TAG = "TXUserCache";

    public static final String TX_USER_CACHE = "tx.user.cache.";
    // 最大50M
    private static final long MAX_SIZE = 1024 * 1024 * 50;
    private boolean mInitSucc;
    private DiskCache mDiskCache;

    private static class InstanceHolder {
        public final static TXUserCache instance = new TXUserCache();
    }

    public static TXUserCache getInstance() {
        return InstanceHolder.instance;
    }

    public void init(Context context, String cacheId) {
        File cacheDir = TXCacheManager.getInstance().getCacheDir();
        if (cacheDir != null && cacheDir.exists()) {
            File dir = new File(cacheDir, TX_USER_CACHE + cacheId);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e(TAG, "init fail dir create fail");
                    return;
                }
            }

            try {
                mDiskCache = DiskCache.create(dir, 1, MAX_SIZE);
                mInitSucc = true;
            } catch (IOException e) {
                mInitSucc = false;
                Log.e(TAG, "init fail for " + e.getMessage());
            }
        }
    }

    @Override
    public String getString(String key, String defaultValue) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        String value = mDiskCache.getString(key);
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }

        return value;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        String valueStr = mDiskCache.getString(key);
        if (TextUtils.isEmpty(valueStr)) {
            return defaultValue;
        }

        boolean value = defaultValue;
        try {
            value = Boolean.parseBoolean(valueStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return value;
    }

    @Override
    public int getInt(String key, int defaultValue) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        String valueStr = mDiskCache.getString(key);
        if (TextUtils.isEmpty(valueStr)) {
            return defaultValue;
        }

        int value = defaultValue;
        try {
            value = Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return value;
    }

    @Override
    public long getLong(String key, long defaultValue) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        String valueStr = mDiskCache.getString(key);
        if (TextUtils.isEmpty(valueStr)) {
            return defaultValue;
        }

        long value = defaultValue;
        try {
            value = Long.parseLong(valueStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return value;
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        String valueStr = mDiskCache.getString(key);
        if (TextUtils.isEmpty(valueStr)) {
            return defaultValue;
        }

        float value = defaultValue;
        try {
            value = Float.parseFloat(valueStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return value;
    }

    @Nullable
    public <T extends TXDataModel> T getModel(String key, @NonNull Class<T> clazz) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return null;
        }

        String valueStr = getString(key, null);
        if (TextUtils.isEmpty(valueStr)) {
            return null;
        }

        return TXJsonUtil.getModel(valueStr, clazz);
    }

    public <T extends TXDataModel> List<T> getModelList(String key, @NonNull Type typeOfT) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return null;
        }

        String valueStr = getString(key, null);
        if (TextUtils.isEmpty(valueStr)) {
            return null;
        }

        return TXJsonUtil.getModelList(valueStr, typeOfT);
    }

    @Override
    public boolean contains(String key) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return false;
        }

        return mDiskCache.contains(key);
    }

    @Override
    public void remove(String key) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return;
        }

        mDiskCache.delete(key);
    }

    @Override
    public void putString(String key, String value) {
        if (!mInitSucc || TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }

        mDiskCache.put(key, value);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return;
        }

        mDiskCache.put(key, String.valueOf(value));
    }

    @Override
    public void putInt(String key, int value) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return;
        }

        mDiskCache.put(key, String.valueOf(value));
    }

    @Override
    public void putLong(String key, long value) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return;
        }

        mDiskCache.put(key, String.valueOf(value));
    }

    @Override
    public void putFloat(String key, float value) {
        if (!mInitSucc || TextUtils.isEmpty(key)) {
            return;
        }

        mDiskCache.put(key, String.valueOf(value));
    }

    public <T extends TXDataModel> void putModel(String key, T model) {
        if (!mInitSucc || TextUtils.isEmpty(key) || null == model) {
            return;
        }

        String valueStr = TXJsonUtil.parse(model);
        if (TextUtils.isEmpty(valueStr)) {
            return;
        }

        putString(key, valueStr);
    }

    public <T extends TXDataModel> void putModelList(String key, List<T> modelList) {
        if (!mInitSucc || TextUtils.isEmpty(key) || null == modelList || modelList.size() == 0) {
            return;
        }

        String valueStr = TXJsonUtil.parse(modelList);
        if (TextUtils.isEmpty(valueStr)) {
            return;
        }

        putString(key, valueStr);
    }

    public void clear() {
        mDiskCache.clear();
    }
}
