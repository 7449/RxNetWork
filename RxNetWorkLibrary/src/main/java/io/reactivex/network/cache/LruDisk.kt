package io.reactivex.network.cache

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.annotations.NonNull
import io.reactivex.network.util.OkioUtils
import okhttp3.internal.cache.DiskLruCache
import okhttp3.internal.io.FileSystem
import okio.Okio
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

/**
 * by y on 20/09/2017.
 * file缓存
 */

class LruDisk internal constructor(path: File, version: Int, valueCount: Int, maxSize: Int) {

    private val diskLruCache: DiskLruCache?
    private val gson: Gson

    internal val cacheSize: Long
        get() {
            try {
                return diskLruCache!!.size()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return -1
        }

    init {
        if (!path.exists()) {
            path.mkdirs()
        }
        gson = Gson()
        diskLruCache = DiskLruCache.create(FileSystem.SYSTEM, path, version, valueCount, maxSize.toLong())
    }

    fun <T> insert(@NonNull key: Any, value: T): Boolean {
        delete(key)
        var editor: DiskLruCache.Editor? = null
        try {
            editor = diskLruCache!!.edit(key.toString())
            if (editor == null) {
                return false
            }
            val bufferedSink = Okio.buffer(editor.newSink(0))
            bufferedSink.writeString(gson.toJson(value), Charset.defaultCharset())
            bufferedSink.flush()
            editor.commit()
            return true
        } catch (e: IOException) {
            OkioUtils.abort(editor)
            e.printStackTrace()
        }

        OkioUtils.abort(editor)
        return false
    }

    fun <T> query(@NonNull key: Any, typeToken: TypeToken<T>): T? {
        var snapshot: DiskLruCache.Snapshot? = null
        try {
            snapshot = diskLruCache!!.get(key.toString())
            if (snapshot == null) {
                return null
            }
            val bufferedSource = Okio.buffer(snapshot.getSource(0))
            return gson.fromJson<T>(bufferedSource.readString(Charset.defaultCharset()), typeToken.type)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            OkioUtils.snapshot(snapshot)
        }
        return null
    }

    internal fun delete(@NonNull key: Any): Boolean {
        try {
            return diskLruCache!!.remove(key.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    internal fun deleteAll() {
        try {
            diskLruCache!!.delete()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    internal fun containsKey(@NonNull key: Any): Boolean {
        try {
            return diskLruCache!!.get(key.toString()) == null
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    internal fun onDestroy(): Boolean {
        if (diskLruCache != null) {
            try {
                if (!diskLruCache.isClosed) {
                    diskLruCache.close()
                }
                return true
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return false
    }
}
