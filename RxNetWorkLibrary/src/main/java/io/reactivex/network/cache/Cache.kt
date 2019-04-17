package io.reactivex.network.cache

import io.reactivex.Observable
import io.reactivex.ObservableSource

class CacheResult<T>(var result: T, var key: Any, var type: CacheType) {
    enum class CacheType {
        NETWORK, CACHE
    }
}

enum class Type {
    CACHE_NETWORK, NETWORK
}

interface CustomizeTransformerCall<T> {
    fun applyCustomize(key: Any, upstream: Observable<T>): ObservableSource<CacheResult<T>>
}

class SimpleCustomizeTransformerCallKt<T> {

    private var applyCustomize: ((key: Any, upstream: Observable<T>) -> ObservableSource<CacheResult<T>>)? = null

    fun applyCustomize(applyCustomize: (key: Any, upstream: Observable<T>) -> ObservableSource<CacheResult<T>>) {
        this.applyCustomize = applyCustomize
    }

    internal fun build(): CustomizeTransformerCall<T> {
        return object : CustomizeTransformerCall<T> {
            override fun applyCustomize(key: Any, upstream: Observable<T>): ObservableSource<CacheResult<T>> {
                return applyCustomize?.invoke(key, upstream)
                        ?: throw  KotlinNullPointerException("check applyCustomize")
            }
        }
    }
}

