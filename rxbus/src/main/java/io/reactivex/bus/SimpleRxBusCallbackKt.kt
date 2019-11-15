package io.reactivex.bus

class SimpleRxBusCallbackKt<T> {
    private var onBusNext: ((entity: T) -> Unit)? = null
    private var onBusError: ((throwable: Throwable) -> Unit)? = null
    private var busOfType: (() -> Class<T>)? = null

    fun onBusNext(onBusNext: (entity: T) -> Unit) {
        this.onBusNext = onBusNext
    }

    fun onBusError(onBusError: (throwable: Throwable) -> Unit) {
        this.onBusError = onBusError
    }

    fun busOfType(busOfType: () -> Class<T>) {
        this.busOfType = busOfType
    }

    internal fun build(): RxBusCallBack<T> {
        return object : RxBusCallBack<T> {
            override fun onBusNext(entity: T) {
                onBusNext?.invoke(entity)
            }

            override fun onBusError(throwable: Throwable) {
                onBusError?.invoke(throwable)
            }

            override fun busOfType(): Class<T> {
                return busOfType?.invoke() ?: throw KotlinNullPointerException("busOfType == null")
            }
        }
    }
}
