package io.reactivex.network.util

import okhttp3.internal.cache.DiskLruCache
import java.io.IOException

/**
 * by y on 20/09/2017.
 */

object OkioUtils {


    fun abort(editor: DiskLruCache.Editor?) {
        if (editor != null) {
            try {
                editor.abort()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun snapshot(snapshot: DiskLruCache.Snapshot?) {
        snapshot?.close()
    }
}
