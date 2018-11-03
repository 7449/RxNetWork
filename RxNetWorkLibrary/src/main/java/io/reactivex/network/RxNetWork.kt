package io.reactivex.network


import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * by y on 2017/2/22.
 */
class RxNetWork private constructor() {

    companion object {
        private const val DEFAULT_TAG = "RxNetWork"
        val instance: RxNetWork by lazy { RxNetWork() }
        fun initialization(rxNetOptionFactory: RxNetOptionFactory) {
            instance.rxNetOptionFactory = rxNetOptionFactory
        }

        fun <T> observable(service: Class<T>): T = instance.rxNetOptionFactory.retrofit.create(service)
    }

    private val arrayMap: HashMap<Any, Disposable> = HashMap()
    private lateinit var rxNetOptionFactory: RxNetOptionFactory

    fun <M> getApi(mObservable: Observable<M>, listener: RxNetWorkListener<M>) {
        getApi(DEFAULT_TAG, mObservable, listener)
    }

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
        val disposable = observable
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
        arrayMap[tag] = disposable
    }

    fun cancel(tag: Any) {
        val disposable = arrayMap[tag]
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
            arrayMap.remove(tag)
        }
    }

    fun cancelDefaultKey() {
        cancel(DEFAULT_TAG)
    }

    fun cancelAll() {
        for ((key) in arrayMap) {
            cancel(key)
        }
    }

    fun containsKey(key: Any): Boolean = arrayMap.containsKey(key)
    fun getMap(): HashMap<Any, Disposable> = arrayMap
}
