package io.reactivex.network


import android.support.v4.util.ArrayMap
import android.support.v4.util.SimpleArrayMap
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * by y on 2017/2/22.
 */
class RxNetWork private constructor() {

    companion object {
        private const val DEFAULT_TAG = "RxNetWork"
        val instance: RxNetWork by lazy { RxNetWork() }
        fun <T> observable(service: Class<T>): T = instance.retrofitFactory().create(service)
    }

    private val arrayMap: ArrayMap<Any, Disposable> = ArrayMap()
    var timeoutTime = 15.toLong()
    var retryOnConnectionFailure = true
    var gson: Gson? = null
    var baseUrl: String? = null
    var okHttpClient: OkHttpClient? = null
    var retrofit: Retrofit? = null
    var converterFactory: Converter.Factory? = null
    var adapterFactory: CallAdapter.Factory? = null
    var logInterceptor: Interceptor? = null
    var headerInterceptor: Interceptor? = null

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
    fun getMap(): SimpleArrayMap<Any, Disposable> = arrayMap

    private fun retrofitFactory(): Retrofit {
        if (okHttpClient == null) {
            okHttpClient = initOkHttp()
        }
        if (converterFactory == null) {
            rxNetWorkConverterFactory()
        }
        if (adapterFactory == null) {
            rxNetWorkAdapterFactory()
        }
        if (retrofit == null) {
            retrofit = initRetrofit()
        }
        return retrofit as Retrofit
    }

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient!!)
                .baseUrl(baseUrl!!)
                .addConverterFactory(converterFactory!!)
                .addCallAdapterFactory(adapterFactory!!)
                .build()
    }

    private fun initOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (logInterceptor != null) {
            builder.addInterceptor(logInterceptor!!)
        }
        if (headerInterceptor != null) {
            builder.addInterceptor(headerInterceptor!!)
        }
        builder.connectTimeout(timeoutTime, TimeUnit.SECONDS)
                .writeTimeout(timeoutTime, TimeUnit.SECONDS)
                .readTimeout(timeoutTime, TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(retryOnConnectionFailure)
        return builder.build()
    }

    private fun rxNetWorkConverterFactory() {
        if (gson == null) {
            gson = Gson()
        }
        converterFactory = GsonConverterFactory.create(gson!!)
    }

    private fun rxNetWorkAdapterFactory() {
        adapterFactory = RxJava2CallAdapterFactory.create()
    }


}
