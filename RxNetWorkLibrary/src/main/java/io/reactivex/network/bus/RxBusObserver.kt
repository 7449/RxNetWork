package io.reactivex.network.bus

import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableObserver

/**
 * by y on 2017/5/22
 */

internal open class RxBusObserver<T> : DisposableObserver<T>() {
    override fun onNext(@NonNull t: T) {
    }

    override fun onError(@NonNull e: Throwable) {
    }

    override fun onComplete() {
    }
}
