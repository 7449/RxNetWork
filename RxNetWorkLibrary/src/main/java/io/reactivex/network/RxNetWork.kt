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
        fun <T> observable(service: Class<T>): T = instance.retrofitFactory().create(service)
    }

    private val arrayMap: ArrayMap<Any, Disposable> = ArrayMap()
    var timeoutTime = 15.toLong()
    var isRetryOnConnectionFailure = true
    var mGson: Gson? = null
    var baseUrl: String? = null
    var mOkHttpClient: OkHttpClient? = null
    var retrofit: Retrofit? = null
    var mConverterFactory: Converter.Factory? = null
    var mAdapterFactory: CallAdapter.Factory? = null
    var mLogInterceptor: Interceptor? = null
    var mHeaderInterceptor: Interceptor? = null

    fun setRetryOnConnectionFailure(retryOnConnectionFailure: Boolean) = apply { isRetryOnConnectionFailure = retryOnConnectionFailure }
    fun setBaseUrl(url: String) = apply { this.baseUrl = url }
    fun setGson(gson: Gson) = apply { this.mGson = gson }
    fun setOkHttpClient(okHttpClient: OkHttpClient) = apply { this.mOkHttpClient = okHttpClient }
    fun setLogInterceptor(mLogInterceptor: Interceptor) = apply { this.mLogInterceptor = mLogInterceptor }
    fun setHeaderInterceptor(mHeaderInterceptor: Interceptor) = apply { this.mHeaderInterceptor = mHeaderInterceptor }
    fun setTimeout(timeout: Int) = apply { this.timeoutTime = timeout.toLong() }
    fun setConverterFactory(factory: Converter.Factory) = apply { this.mConverterFactory = factory }
    fun setRetrofit(retrofit: Retrofit) = apply { this.retrofit = retrofit }
    fun setAdapterFactory(factory: CallAdapter.Factory) = apply { this.mAdapterFactory = factory }

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
                .client(mOkHttpClient!!)
                .baseUrl(baseUrl!!)
                .addConverterFactory(mConverterFactory!!)
                .addCallAdapterFactory(mAdapterFactory!!)
                .build()
    }

    private fun initOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (mLogInterceptor != null) {
            builder.addInterceptor(mLogInterceptor!!)
        }
        if (mHeaderInterceptor != null) {
            builder.addInterceptor(mHeaderInterceptor!!)
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
        mConverterFactory = GsonConverterFactory.create(mGson!!)
    }

    private fun rxNetWorkAdapterFactory() {
        mAdapterFactory = RxJava2CallAdapterFactory.create()
    }


}
