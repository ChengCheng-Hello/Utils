package com.cc.utils.manager;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;

/**
 * 文件管理，用于获取各种目录。
 * <p>
 * Created by Cheng on 16/11/10.
 */
public class TXFileManager {

    private static final String TAG = "TXFileManager";

    private static final String TYPE_ROOT = "root";
    public static final String TYPE_AUDIO = "Music";
    public static final String TYPE_IMAGE = "Pictures";
    public static final String TYPE_DOWNLOADS = "Download";
    public static final String TYPE_LOGS = "Logs";

    @StringDef({TYPE_ROOT, TYPE_AUDIO, TYPE_IMAGE, TYPE_DOWNLOADS, TYPE_LOGS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    /**
     * 获取缓存目录, /cache
     * 先获取外部,如果外部获取失败,获取内部缓存路径
     *
     * @param context
     * @return
     */
    @Nullable
    public static File getCacheDir(Context context) {
        if (context == null) {
            return null;
        }

        File cacheDir;

        cacheDir = getExternalCacheDir(context, null);

        if (cacheDir == null) {
            cacheDir = getInnerCacheDir(context, null);
        }

        return cacheDir;
    }

    /**
     * 获取文件目录 /files
     * 先获取外部,如果外部获取失败,获取内部缓存路径
     *
     * @param context
     * @return
     */
    @Nullable
    public static File getFileDir(Context context) {
        if (context == null) {
            return null;
        }

        File fileDir;

        fileDir = getExternalCacheDir(context, TYPE_ROOT);

        if (fileDir == null) {
            fileDir = getInnerCacheDir(context, TYPE_ROOT);
        }

        return fileDir;
    }

    /**
     * 根据类型,获取文件目录 /files/Pictures
     * 先获取外部,如果外部获取失败,获取内部缓存路径
     *
     * @param context
     * @param type
     * @return
     */
    @Nullable
    public static File getFileDir(Context context, @TYPE String type) {
        if (context == null || TextUtils.isEmpty(type)) {
            return null;
        }

        File fileDir;

        fileDir = getExternalCacheDir(context, type);

        if (fileDir == null) {
            fileDir = getInnerCacheDir(context, type);
        }

        return fileDir;
    }

    /**
     * 获取文件
     *
     * @param context
     * @param type                     类型
     * @param fileName                 文件名称(不支持路径)
     * @param createFileWhileNonExists 不存在是否创建
     * @return
     */
    @Nullable
    public static File getFile(Context context, @TYPE String type, @NonNull String fileName, boolean createFileWhileNonExists) {
        File fileDir = TXFileManager.getFileDir(context, type);
        if (fileDir == null) {
            return null;
        }

        if (fileName.contains(File.separator)) {
            return null;
        }

        File file = new File(fileDir, fileName);
        try {
            if (createFileWhileNonExists && !file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
            Log.d(TAG, "getFile " + e.getMessage());
        }

        return file;
    }

//    /**
//     * 获取外部存储目录 /mnt/sdcard/Android/data/PackageName/cache
//     *
//     * @param context
//     * @return
//     */
//    @Nullable
//    public static File getExternalDir(Context context) {
//        if (context == null) {
//            return null;
//        }
//
//        return getExternalCacheDir(context, null);
//    }
//
//    /**
//     * 获取内部存储目录 /data/data/PackageName/cache
//     *
//     * @param context
//     * @return
//     */
//    @Nullable
//    public static File getInnerDir(Context context) {
//        if (context == null) {
//            return null;
//        }
//
//        return getInnerCacheDir(context, null);
//    }
//
//    /**
//     * 获取数据库目录 /data/data/PackageName/databases
//     *
//     * @param context
//     * @return
//     */
//    @Nullable
//    public static File getDBDir(Context context) {
//        if (context == null) {
//            return null;
//        }
//
//        File dbFile = null;
//
//        File cacheDir = context.getCacheDir();
//        if (cacheDir != null && cacheDir.exists()) {
//            File parentFile = cacheDir.getParentFile();
//            dbFile = new File(parentFile, "databases");
//        }
//
//        return dbFile;
//    }
//
//    /**
//     * 获取SP目录 /data/data/PackageName/shared_prefs
//     *
//     * @param context
//     * @return
//     */
//    @Nullable
//    public static File getSharedPreferenceDir(Context context) {
//        if (context == null) {
//            return null;
//        }
//
//        File spFile = null;
//
//        File cacheDir = context.getCacheDir();
//        if (cacheDir != null && cacheDir.exists()) {
//            File parentFile = cacheDir.getParentFile();
//            spFile = new File(parentFile, "shared_prefs");
//        }
//
//        return spFile;
//    }

    /**
     * 检查SDCard是否可用
     *
     * @return true 可用 false 不可用
     */
    private static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        return state != null && state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取外部缓存路径
     * <p>
     * /mnt/sdcard/Android/data/PackageName/cache
     * /mnt/sdcard/Android/data/PackageName/files/Music
     *
     * @param context
     * @param type    type为空获取cache目录,否则获取对应类型的files目录
     * @return
     */
    @Nullable
    private static File getExternalCacheDir(Context context, @TYPE String type) {
        File cacheDir = null;
        if (hasSDCardMounted()) {
            cacheDir = context.getExternalCacheDir();
            if (checkIfWritable(cacheDir)) {
                if (!TextUtils.isEmpty(type)) {
                    if (TYPE_ROOT.equals(type)) {
                        cacheDir = context.getExternalFilesDir(null);
                    } else {
                        cacheDir = context.getExternalFilesDir(type);
                    }
                }
            } else {
                return null;
            }
        }

        return cacheDir;
    }

    /**
     * 获取内部缓存路径
     * <p>
     * /data/data/PackageName/cache
     * /mnt/sdcard/Android/data/PackageName/files/Music
     *
     * @param context
     * @param type    type为空获取cache目录,否则获取对应类型的files目录
     * @return
     */
    @Nullable
    private static File getInnerCacheDir(Context context, @TYPE String type) {
        File cacheDir;

        if (TextUtils.isEmpty(type)) {
            cacheDir = context.getCacheDir();
        } else {
            cacheDir = context.getFileStreamPath(type);
        }

        return cacheDir;
    }

    /**
     * 创建一个文件夹来检测文件是否有权限
     */
    private static boolean checkIfWritable(File file) {
        if (file == null) {
            return false;
        }

        String tempDir = "checkIfWritable_" + System.currentTimeMillis();
        File temp = new File(file, tempDir);
        if (temp.mkdirs()) {
            deleteDirectory(temp);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除目录
     *
     * @param fileDir 目录
     * @return 是否成功
     */
    public static void deleteDirectory(File fileDir) {
        if (fileDir == null || !fileDir.exists()) {
            return;
        }

        File[] files = fileDir.listFiles();
        if (files == null || files.length == 0) {
            fileDir.delete();
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirectory(file);
                return;
            } else {
                file.delete();
            }
        }

        fileDir.delete();
    }

    /**
     * 单位换算
     *
     * @param size 单位为B
     * @return 转换后的单位
     */
    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat();
        String fileSizeString;
        if (size < 1024 && size > 0) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1024 * 1024) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) size / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
    }
}
