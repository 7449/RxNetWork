package io.reactivex.network

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class RxNetWork private constructor() {

    companion object {

        @JvmStatic
        val instance: RxNetWork by lazy { RxNetWork() }

        @JvmStatic
        fun initOption(rxNetOptionFactoryKt: SimpleRxNetOptionFactoryKt.() -> Unit) = initialization(SimpleRxNetOptionFactoryKt().also(rxNetOptionFactoryKt).build())

        @JvmStatic
        fun initialization(rxNetOptionFactory: RxNetOptionFactory) {
            instance.rxNetOptionFactory = rxNetOptionFactory
        }

        @JvmStatic
        fun <T> getApi(tag: Any, observable: Observable<T>, rxNetWorkListenerKt: SimpleRxNetWorkListenerKt<T>.() -> Unit) {
            instance.getApi(tag, observable, SimpleRxNetWorkListenerKt<T>().also(rxNetWorkListenerKt).build())
        }

        @JvmStatic
        fun cancelTag(tag: Any) = instance.cancel(tag)

        @JvmStatic
        fun cancelListTag(vararg tags: Any?) = run {
            for (tag in tags) {
                if (tag != null) {
                    cancelTag(tag)
                }
            }
        }

        @JvmStatic
        fun cancelAllTag() = instance.cancelAll()

        @JvmStatic
        fun containsKeyX(key: Any): Boolean = instance.containsKey(key)

        @JvmStatic
        fun <T> observable(service: Class<T>): T = instance.rxNetOptionFactory.retrofit.create(service)
    }

    private val arrayMap: HashMap<Any, Disposable> = HashMap()
    private lateinit var rxNetOptionFactory: RxNetOptionFactory

    fun <M> getApi(tag: Any, observable: Observable<M>, listener: RxNetWorkListener<M>?) {
        listener?.onNetWorkStart()
        arrayMap[tag] = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<M>() {

                    override fun onError(e: Throwable) {
                        listener?.onNetWorkError(e)
                    }

                    override fun onComplete() {
                        listener?.onNetWorkComplete()
                    }

                    override fun onNext(m: M) {
                        listener?.onNetWorkSuccess(m)
                    }
                })
    }

    fun <M> getApiTask(tag: Any, observable: Observable<M>, listener: RxNetWorkTaskListener<RxNetWorkTask<M>>) {
        listener.onNetWorkStart()
        arrayMap[tag] = observable
                .map { t -> RxNetWorkTask(t, listener.tag) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<RxNetWorkTask<M>>() {

                    override fun onNext(mRxNetWorkTask: RxNetWorkTask<M>) {
                        listener.onNetWorkSuccess(mRxNetWorkTask)
                    }

                    override fun onError(e: Throwable) {
                        listener.onNetWorkError(e)
                    }

                    override fun onComplete() {
                        listener.onNetWorkComplete()
                    }
                })
    }

    fun cancel(tag: Any) {
        val disposable = arrayMap[tag]
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
        arrayMap.remove(tag)
    }

    fun cancelAll() {
        for ((key) in arrayMap) {
            cancel(key)
        }
    }

    fun containsKey(key: Any): Boolean = arrayMap.containsKey(key)

    fun getMap(): HashMap<Any, Disposable> = arrayMap
}
