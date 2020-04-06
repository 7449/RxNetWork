package io.reactivex.network

import io.reactivex.disposables.Disposable

/**
 * [Disposable] Manager
 */
class RxNetWork private constructor() {

    companion object {

        val instance: RxNetWork by lazy { RxNetWork() }

        fun initialization(rxNetOptionFactory: RxNetOption) {
            instance.rxNetOptionFactory = rxNetOptionFactory
        }

        fun <T> observable(service: Class<T>): T = instance.rxNetOptionFactory.retrofit.create(service)
    }

    private val arrayMap: HashMap<Any, Disposable> = HashMap()
    private lateinit var rxNetOptionFactory: RxNetOption

    fun addTag(tag: Any, disposable: Disposable) {
        if (containsTag(tag)) {
            throw RuntimeException("tag already exists")
        }
        arrayMap[tag] = disposable
    }

    fun containsTag(key: Any): Boolean = arrayMap.containsKey(key)

    fun cancel(tag: Any) {
        arrayMap[tag]?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        arrayMap.remove(tag)
    }

    fun cancelAll() {
        arrayMap.forEach {
            if (!it.value.isDisposed) {
                it.value.dispose()
            }
        }
        arrayMap.clear()
    }
}
