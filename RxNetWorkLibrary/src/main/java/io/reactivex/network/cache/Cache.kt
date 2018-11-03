package io.reactivex.network.cache

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.annotations.NonNull

class CacheResult<T>(var result: T, var key: Any, var type: CacheType) {
    enum class CacheType {
        NETWORK, CACHE
    }
}

internal enum class Type {
    CACHE_NETWORK, NETWORK
}

interface CustomizeTransformerCall {
    fun <T> applyCustomize(@NonNull key: Any, upstream: Observable<T>): ObservableSource<CacheResult<T>>
}
