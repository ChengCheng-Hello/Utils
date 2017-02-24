package com.cc.utils.manager;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 整体管理缓存，如是否可缓存，剩余缓存空间，清除缓存。
 * <p>
 * Created by Cheng on 16/11/10.
 */
public class TXCacheManager {

    private static final String TAG = "TXCacheManagerV2";

    // 最小可用空间,当系统剩余缓存空间不足20M时,提示用户空间不足
    private static final long MIN_USABLE_SPACE = 1024 * 1024 * 20;
    // 最长保存时间
    private static final long MAX_SAVE_TIME = TimeUnit.DAYS.toMillis(10);

    private Context mContext;
    private File mCacheDir;

    private static class InstanceHolder {
        public final static TXCacheManager instance = new TXCacheManager();
    }

    public static TXCacheManager getInstance() {
        return InstanceHolder.instance;
    }

    public void init(Context context) {
        if (context == null) {
            return;
        }

        mContext = context.getApplicationContext();
        mCacheDir = TXFileManager.getCacheDir(mContext);
    }

    public File getCacheDir() {
        return mCacheDir;
    }

    /**
     * 否可缓存
     *
     * @return
     */
    public boolean enableCache() {
        long usableSpace = getUsableSpace();
        return usableSpace > MIN_USABLE_SPACE;
    }

    /**
     * 获取可用空间
     *
     * @return
     */
    public long getUsableSpace() {
        if (mCacheDir == null) {
            return 0;
        }

        return mCacheDir.getUsableSpace();
    }

    /**
     * 已使用空间
     *
     * @return
     */
    public long getUsedSpace() {
        if (mCacheDir == null) {
            return 0;
        }

        return mCacheDir.getTotalSpace() - mCacheDir.getUsableSpace();
    }

    /**
     * 获取已使用缓存空间,只提供可删除的空间大小
     *
     * @return
     */
    public long getCacheUsedSpace() {
        if (mContext == null) {
            return 0;
        }

        if (mCacheDir == null) {
            return 0;
        }

        long cacheDirSize = getFolderSize(mCacheDir);
        long fileDirSize = getFolderSize(TXFileManager.getFileDir(mContext));

        return cacheDirSize + fileDirSize;
    }

    /**
     * 清除缓存,主要是外部缓存
     */
    public void clearCaches() {
//        if (mCacheDir != null && mCacheDir.exists() && mCacheDir.isDirectory()) {
//            File[] files = mCacheDir.listFiles();
//            for (File file : files) {
//                String name = file.getName();
//                // 如果是用户缓存使用DiskCache清除,这里不清除
//                if (name.contains(TXUserCache.TX_USER_CACHE)) {
//                    continue;
//                }
//                TXFileManager.deleteDirectory(file);
//            }
//        }
//
//        clearFiles();

//        TXUserCache.getInstance().clear();
    }

    /**
     * 清除过期缓存文件
     */
    public void clearExpiredCacheFiles() {
        if (mContext == null) {
            return;
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // 延迟执行
//                    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
//
//                    File fileAudioDir = TXFileManager.getFileDir(mContext, TXFileManager.TYPE_AUDIO);
//                    if (fileAudioDir == null) {
//                        return;
//                    }
//
//                    File[] audioFiles = fileAudioDir.listFiles();
//                    for (File file : audioFiles) {
//                        if (file != null && file.exists() && file.isFile()) {
//                            long l = file.lastModified();
//                            TXDate today = new TXDate(new Date());
//                            long milliseconds = today.getMilliseconds();
//                            if (l + MAX_SAVE_TIME < milliseconds) {
//                                file.delete();
//                            }
//                        }
//                    }
//
//                    File fileImageDir = TXFileManager.getFileDir(mContext, TXFileManager.TYPE_IMAGE);
//                    if (fileImageDir == null) {
//                        return;
//                    }
//
//                    File[] imageFiles = fileAudioDir.listFiles();
//                    for (File file : imageFiles) {
//                        if (file != null && file.exists() && file.isFile()) {
//                            long l = file.lastModified();
//                            TXDate today = new TXDate(new Date());
//                            long milliseconds = today.getMilliseconds();
//                            if (l + MAX_SAVE_TIME < milliseconds) {
//                                file.delete();
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    /**
     * 清理不再使用的缓存机制下的缓存数据
     */
    public void clearDeprecatedCaches() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // 延迟执行
//                    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
//
//                    boolean login = TXAuthManager.getInstance().isLogin();
//                    if (!login) {
//                        return;
//                    }
//
//                    TXLog.d(TAG, "clearDeprecatedCaches start");
//
//                    boolean clearV2 = TXCommonCache.getInstance().getBoolean(TXCacheConst.CACHE_CLEAR_DEPRECATED_FLAG_V2, false);
//                    if (clearV2) {
//                        return;
//                    }
//
//                    // audio
//                    File fileDir = TXFileManager.getFileDir(mContext);
//                    if (fileDir != null) {
//                        File oldAudioDir = new File(fileDir + File.separator + "audio");
//                        TXFileManager.deleteDirectory(oldAudioDir);
//                    }
//
//                    TXCommonCache.getInstance().putBoolean(TXCacheConst.CACHE_CLEAR_DEPRECATED_FLAG_V2, true);
//
//                    TXUserAccountDataModel userAccount = TXAuthManager.getInstance().getUserAccount();
//                    if (userAccount == null) {
//                        return;
//                    }
//
//                    String cacheId = userAccount.getCacheId();
//
//                    boolean cleared = TXCommonCache.getInstance().getBoolean(TXCacheConst.CACHE_CLEAR_DEPRECATED_FLAG + cacheId, false);
//                    if (cleared) {
//                        return;
//                    }
//
//                    // image
//                    String cacheDir = FileUtils.tryGetGoodDiskCacheDir(mContext);
//                    File imageCache = new File(cacheDir, "image");
//                    TXFileManager.deleteDirectory(imageCache);
//
//                    // KVCache
//                    File kvCacheDir = new File(FileUtils.tryGetGoodDiskCacheDir(mContext), TXCacheManager.USER_KV_CACHE + cacheId);
//                    TXFileManager.deleteDirectory(kvCacheDir);
//
//                    // log
//                    if (TXDeployManager.EnvironmentType.TYPE_ONLINE != TXDeployManager.getEnvironmentType()) {
//                        String goodFileDir = FileUtils.tryGetGoodDiskFilesDir(mContext);
//                        File logFile = new File(goodFileDir, "txlog");
//                        TXFileManager.deleteDirectory(logFile);
//                    }
//
//                    TXCommonCache.getInstance().putBoolean(TXCacheConst.CACHE_CLEAR_DEPRECATED_FLAG + cacheId, true);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

//    /**
//     * 清除内部缓存 /data/data/PackageName/cache
//     */
//    private void clearInternalCache() {
//        File cacheDir = TXFileManager.getInnerDir(mContext);
//        TXFileManager.deleteDirectory(cacheDir);
//    }
//
//    /**
//     * 清除外部缓存 /mnt/sdcard/PackageName/cache
//     */
//    private void clearExternalCache() {
//        File cacheDir = TXFileManager.getExternalDir(mContext);
//        TXFileManager.deleteDirectory(cacheDir);
//    }
//
//    /**
//     * 清除内部数据库 /data/data/PackageName/databases
//     */
//    private void clearDatabases() {
//        File dbDir = TXFileManager.getDBDir(mContext);
//        TXFileManager.deleteDirectory(dbDir);
//    }
//
//    /**
//     * 清除内部SharedPreference数据 /data/data/PackageName/shared_prefs
//     */
//    private void clearSharedPreferences() {
//        File spDir = TXFileManager.getSharedPreferenceDir(mContext);
//        TXFileManager.deleteDirectory(spDir);
//    }

    /**
     * 清除files下数据
     */
    private void clearFiles() {
        File picDir = TXFileManager.getFileDir(mContext, TXFileManager.TYPE_IMAGE);
        File musicDir = TXFileManager.getFileDir(mContext, TXFileManager.TYPE_AUDIO);
        File downloadDir = TXFileManager.getFileDir(mContext, TXFileManager.TYPE_DOWNLOADS);
        File logDir = TXFileManager.getFileDir(mContext, TXFileManager.TYPE_LOGS);
        TXFileManager.deleteDirectory(picDir);
        TXFileManager.deleteDirectory(musicDir);
        TXFileManager.deleteDirectory(downloadDir);
        TXFileManager.deleteDirectory(logDir);
    }

    /**
     * 获取文件目录大小
     *
     * @param file
     * @return
     */
    private long getFolderSize(File file) {
        if (file == null) {
            return 0;
        }

        long size = 0;
        File[] files = file.listFiles();
        for (File fileItem : files) {
            if (fileItem.isDirectory()) {
                size += getFolderSize(fileItem);
            } else {
                size += fileItem.length();
            }
        }

        return size;
    }
}
