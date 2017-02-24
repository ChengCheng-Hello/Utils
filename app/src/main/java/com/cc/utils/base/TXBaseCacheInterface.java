package com.cc.utils.base;

/**
 * Created by Cheng on 16/11/11.
 */
public interface TXBaseCacheInterface {

    String getString(String key, String defaultValue);

    boolean getBoolean(String key, boolean defaultValue);

    int getInt(String key, int defaultValue);

    long getLong(String key, long defaultValue);

    float getFloat(String key, float defaultValue);

    boolean contains(String key);

    void remove(String key);

    void putString(String key, String value);

    void putBoolean(String key, boolean value);

    void putInt(String key, int value);

    void putLong(String key, long value);

    void putFloat(String key, float value);
}
