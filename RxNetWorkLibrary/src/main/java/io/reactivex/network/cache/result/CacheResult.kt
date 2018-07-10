package io.reactivex.network.cache.result

/**
 * by y on 20/09/2017.
 */

class CacheResult<T>(var result: T, var key: Any, var type: CacheType) {
    enum class CacheType {
        NETWORK, CACHE
    }
}
