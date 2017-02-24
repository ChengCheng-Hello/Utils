# Utils
## Cache
- `TXCacheManager`：整体管理缓存，如是否可缓存，剩余缓存空间，清除缓存。
- `TXUserCache`：和用户绑定的缓存，使用DiskLruCache实现。
- `TXCommonCache`：通用缓存，使用SP实现，不建议存储长串，对性能影响较大。
- `TXFileManager`：文件管理，用于获取各种目录。
