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
        const val DEFAULT_TAG = "RxNetWork"
        val instance: RxNetWork by lazy { RxNetWork() }
        fun <T> observable(service: Class<T>): T = instance.getRetrofit().create(service)
    }

    private val arrayMap: ArrayMap<Any, Disposable> = ArrayMap()

    private var timeoutTime = 15.toLong()
    private var isRetryOnConnectionFailure = true
    private var mGson: Gson? = null
    private var baseUrl: String? = null
    private var mOkHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null
    private var mConverterFactory: Converter.Factory? = null
    private var mAdapterFactory: CallAdapter.Factory? = null
    private var mLogInterceptor: Interceptor? = null
    private var mHeaderInterceptor: Interceptor? = null

    fun setRetryOnConnectionFailure(retryOnConnectionFailure: Boolean): RxNetWork {
        isRetryOnConnectionFailure = retryOnConnectionFailure
        return this
    }

    fun setBaseUrl(url: String): RxNetWork {
        this.baseUrl = url
        return this
    }

    fun setGson(gson: Gson): RxNetWork {
        this.mGson = gson
        return this
    }

    fun setOkHttpClient(okHttpClient: OkHttpClient): RxNetWork {
        this.mOkHttpClient = okHttpClient
        return this
    }

    fun setLogInterceptor(mLogInterceptor: Interceptor): RxNetWork {
        this.mLogInterceptor = mLogInterceptor
        return this
    }

    fun setHeaderInterceptor(mHeaderInterceptor: Interceptor): RxNetWork {
        this.mHeaderInterceptor = mHeaderInterceptor
        return this
    }

    fun setTimeout(timeout: Int): RxNetWork {
        this.timeoutTime = timeout.toLong()
        return this
    }

    fun setConverterFactory(factory: Converter.Factory): RxNetWork {
        this.mConverterFactory = factory
        return this
    }

    fun setRetrofit(retrofit: Retrofit): RxNetWork {
        this.retrofit = retrofit
        return this
    }


    fun setAdapterFactory(factory: CallAdapter.Factory): RxNetWork {
        this.mAdapterFactory = factory
        return this
    }

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

    private fun getRetrofit(): Retrofit {
        if (mOkHttpClient == null) {
            mOkHttpClient = initOkHttp()
        }
        if (mConverterFactory == null) {
            rxNetWorkConverterFactory()
        }
        if (mAdapterFactory == null) {
            rxNetWorkAdapterFactory()
        }
        if (retrofit == null) {
            retrofit = initRetrofit()
        }
        return retrofit as Retrofit
    }

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(baseUrl!!)
                .addConverterFactory(mConverterFactory)
                .addCallAdapterFactory(mAdapterFactory)
                .build()
    }

    private fun initOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (mLogInterceptor != null) {
            builder.addInterceptor(mLogInterceptor)
        }
        if (mHeaderInterceptor != null) {
            builder.addInterceptor(mHeaderInterceptor)
        }
        builder.connectTimeout(timeoutTime, TimeUnit.SECONDS)
                .writeTimeout(timeoutTime, TimeUnit.SECONDS)
                .readTimeout(timeoutTime, TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(isRetryOnConnectionFailure)
        return builder.build()
    }

    private fun rxNetWorkConverterFactory() {
        if (mGson == null) {
            mGson = Gson()
        }
        mConverterFactory = GsonConverterFactory.create(mGson)
    }

    private fun rxNetWorkAdapterFactory() {
        mAdapterFactory = RxJava2CallAdapterFactory.create()
    }


}
