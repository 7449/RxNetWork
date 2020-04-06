package io.reactivex.cache

import com.google.gson.reflect.TypeToken
import io.reactivex.ObservableTransformer
import java.io.File

class RxCache private constructor() {

    companion object {
        val instance: RxCache by lazy { RxCache() }

        fun <T> customizeTransformer(key: Any, simpleCustomizeTransformerCallKt: SimpleCustomizeTransformerCallKt<T>.() -> Unit): ObservableTransformer<T, CacheResult<T>> {
            return instance.customizeTransformer(key, SimpleCustomizeTransformerCallKt<T>().also(simpleCustomizeTransformerCallKt).build())
        }
    }

    private val apply: ApplyImpl = ApplyImpl()
    private lateinit var lruDisk: LruDisk

    fun setDiskBuilder(diskBuilder: DiskBuilder) = apply { lruDisk = LruDisk(diskBuilder.file, diskBuilder.version, diskBuilder.valueCount, diskBuilder.maxSize) }

    class DiskBuilder {
        var file: File
        var version = 1
        var valueCount = 1
        var maxSize = 50 * 1024 * 1024

        constructor(file: File) {
            this.file = file
        }

        constructor(file: File, version: Int) {
            this.file = file
            this.version = version
        }

        constructor(file: File, version: Int, maxSize: Int) {
            this.file = file
            this.version = version
            this.maxSize = maxSize
        }

        constructor(file: File, version: Int, valueCount: Int, maxSize: Int) {
            this.file = file
            this.version = version
            this.valueCount = valueCount
            this.maxSize = maxSize
        }
    }

    /**
     * 优先级： 文件 --> 网络
     *
     * @param network :true 有网的情况下，获取网络数据并缓存，没网的情况再获取缓存(网络状态)
     */
    fun <T> transformerCN(key: Any, network: Boolean, typeToken: TypeToken<T>): ObservableTransformer<T, CacheResult<T>> = transformer(key, Type.CACHE_NETWORK, typeToken, network)

    /**
     * 只走网络
     */
    fun <T> transformerN(): ObservableTransformer<T, CacheResult<T>> = transformer("", Type.NETWORK, null, false)

    fun <T> customizeTransformer(key: Any, customizeTransformerCall: CustomizeTransformerCall<T>): ObservableTransformer<T, CacheResult<T>> = ObservableTransformer { upstream -> apply.applyCustomize(key, upstream, customizeTransformerCall) }

    private fun <T> transformer(key: Any, type: Type, typeToken: TypeToken<T>?, network: Boolean): ObservableTransformer<T, CacheResult<T>> {
        return ObservableTransformer { upstream ->
            when (type) {
                Type.CACHE_NETWORK -> {
                    typeToken?.let {
                        return@ObservableTransformer apply.applyCacheNetWork(key, upstream, lruDisk, typeToken, network)
                    } ?: throw NullPointerException("init TypeToken")
                }
                Type.NETWORK -> return@ObservableTransformer apply.apply(key, upstream, lruDisk, false)
            }
        }
    }

    fun onDestroy(): Boolean = lruDisk.onDestroy()

    fun delete(key: Any): Boolean = lruDisk.delete(key)

    fun deleteAll() = lruDisk.deleteAll()

    fun containsKey(key: Any): Boolean = lruDisk.containsKey(key)

    val cacheSize: Long
        get() = lruDisk.cacheSize
}
