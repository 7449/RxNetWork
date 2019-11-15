package io.reactivex.network

interface RxNetWorkListener<in T> {

    fun onNetWorkStart()

    fun onNetWorkError(e: Throwable)

    fun onNetWorkComplete()

    fun onNetWorkSuccess(data: T)
}

class RxNetWorkTask<T>(var data: T, var tag: Any)

interface RxNetWorkTaskListener<T> : RxNetWorkListener<T> {
    val tag: Any
}

class SimpleRxNetWorkListenerKt<T> {
    private var onNetWorkStart: (() -> Unit)? = null
    private var onNetWorkError: ((e: Throwable) -> Unit)? = null
    private var onNetWorkComplete: (() -> Unit)? = null
    private var onNetWorkSuccess: ((data: T) -> Unit)? = null

    fun onNetWorkStart(onNetWorkStart: () -> Unit) {
        this.onNetWorkStart = onNetWorkStart
    }

    fun onNetWorkError(onNetWorkError: (e: Throwable) -> Unit) {
        this.onNetWorkError = onNetWorkError
    }

    fun onNetWorkComplete(onNetWorkComplete: () -> Unit) {
        this.onNetWorkComplete = onNetWorkComplete
    }

    fun onNetWorkSuccess(onNetWorkSuccess: (data: T) -> Unit) {
        this.onNetWorkSuccess = onNetWorkSuccess
    }

    internal fun build(): RxNetWorkListener<T> {
        return object : RxNetWorkListener<T> {
            override fun onNetWorkStart() {
                onNetWorkStart?.invoke()
            }

            override fun onNetWorkError(e: Throwable) {
                onNetWorkError?.invoke(e)
            }

            override fun onNetWorkComplete() {
                onNetWorkComplete?.invoke()
            }

            override fun onNetWorkSuccess(data: T) {
                onNetWorkSuccess?.invoke(data)
            }
        }
    }
}
