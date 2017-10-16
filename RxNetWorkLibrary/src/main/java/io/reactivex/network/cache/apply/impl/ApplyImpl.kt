package io.reactivex.network.cache.apply.impl


import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.annotations.NonNull
import io.reactivex.network.cache.CustomizeTransformerCall
import io.reactivex.network.cache.LruDisk
import io.reactivex.network.cache.apply.Apply
import io.reactivex.network.cache.result.CacheResult

/**
 * by y on 20/09/2017.
 */

class ApplyImpl : Apply {

    override fun <T> applyCacheNetWork(@NonNull key: Any, observable: Observable<T>, lruDisk: LruDisk, typeToken: TypeToken<T>, network: Boolean): Observable<CacheResult<T>> {
        val query = lruDisk.query(key, typeToken)
        return if (query == null || network)
            apply(key, observable, lruDisk, true)
        else
            Observable.just(CacheResult(query, key, CacheResult.CacheType.CACHE))
    }

    override fun <T> apply(@NonNull key: Any, observable: Observable<T>, lruDisk: LruDisk, isInsert: Boolean): Observable<CacheResult<T>> {
        return observable.map { t ->
            val tCacheResult = CacheResult(t, key, CacheResult.CacheType.NETWORK)
            if (isInsert) {
                lruDisk.insert(key, tCacheResult.result)
            }
            tCacheResult
        }
    }

    override fun <T> applyCustomize(@NonNull key: Any, upstream: Observable<T>, customizeTransformerCall: CustomizeTransformerCall): ObservableSource<CacheResult<T>> =
            customizeTransformerCall.applyCustomize(key, upstream)
}
