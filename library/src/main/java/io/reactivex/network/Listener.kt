package io.reactivex.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit


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

interface RxNetOptionFactory {
    val baseUrl: String
    val timeoutTime: Long
    val retryOnConnectionFailure: Boolean
    val interceptors: List<Interceptor>?
    val netInterceptors: List<Interceptor>?
    val okHttpClient: OkHttpClient
    val retrofit: Retrofit
    val adapterFactory: CallAdapter.Factory
    val converterFactory: Converter.Factory
}

class SimpleRxNetOptionFactoryKt {

    private var superBaseUrl: String = ""
    private var superTimeoutTime: Long = 15
    private var superRetryOnConnectionFailure: Boolean = true
    private var superOkHttpClient: OkHttpClient? = null
    private var superRetrofit: Retrofit? = null
    private var superAdapterFactory: CallAdapter.Factory? = null
    private var superConverterFactory: Converter.Factory? = null
    private var superInterceptor: List<Interceptor>? = null
    private var superNetInterceptor: List<Interceptor>? = null

    fun superBaseUrl(superBaseUrl: () -> String) {
        this.superBaseUrl = superBaseUrl.invoke()
    }

    fun superTimeoutTime(superTimeoutTime: () -> Long) {
        this.superTimeoutTime = superTimeoutTime.invoke()
    }

    fun superRetryOnConnectionFailure(superRetryOnConnectionFailure: () -> Boolean) {
        this.superRetryOnConnectionFailure = superRetryOnConnectionFailure.invoke()
    }

    fun superOkHttpClient(superOkHttpClient: () -> OkHttpClient) {
        this.superOkHttpClient = superOkHttpClient.invoke()
    }

    fun superRetrofit(superRetrofit: () -> Retrofit) {
        this.superRetrofit = superRetrofit.invoke()
    }

    fun superAdapterFactory(superAdapterFactory: () -> CallAdapter.Factory) {
        this.superAdapterFactory = superAdapterFactory.invoke()
    }

    fun superConverterFactory(superConverterFactory: () -> Converter.Factory) {
        this.superConverterFactory = superConverterFactory.invoke()
    }

    fun superInterceptor(superInterceptor: () -> List<Interceptor>) {
        this.superInterceptor = superInterceptor.invoke()
    }

    fun superNetInterceptor(superNetInterceptor: () -> List<Interceptor>) {
        this.superNetInterceptor = superNetInterceptor.invoke()
    }

    internal fun build(): RxNetOptionFactory {
        return object : RxNetOptionFactory {
            override val baseUrl: String
                get() = superBaseUrl
            override val timeoutTime: Long
                get() = superTimeoutTime
            override val retryOnConnectionFailure: Boolean
                get() = superRetryOnConnectionFailure
            override val okHttpClient: OkHttpClient
                get() = superOkHttpClient
                        ?: initOkHttp(timeoutTime, retryOnConnectionFailure, interceptors, netInterceptors)
            override val retrofit: Retrofit
                get() = superRetrofit ?: Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(baseUrl)
                        .addConverterFactory(converterFactory)
                        .addCallAdapterFactory(adapterFactory)
                        .build()
            override val adapterFactory: CallAdapter.Factory
                get() = superAdapterFactory ?: RxJava2CallAdapterFactory.create()
            override val converterFactory: Converter.Factory
                get() = superConverterFactory
                        ?: throw KotlinNullPointerException("check converter factory")
            override val interceptors: List<Interceptor>?
                get() = superInterceptor
            override val netInterceptors: List<Interceptor>?
                get() = superNetInterceptor
        }
    }
}

@Suppress("LeakingThis")
open class SimpleRxNetOptionFactory(override val baseUrl: String, override val converterFactory: Converter.Factory) : RxNetOptionFactory {
    override val interceptors: List<Interceptor>? = null
    override val netInterceptors: List<Interceptor>? = null
    override val timeoutTime: Long = 15
    override val retryOnConnectionFailure: Boolean = true
    override val adapterFactory: CallAdapter.Factory = RxJava2CallAdapterFactory.create()
    override val okHttpClient: OkHttpClient = initOkHttp(timeoutTime, retryOnConnectionFailure, interceptors, netInterceptors)
    override val retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(adapterFactory)
            .build()
}

private fun initOkHttp(timeoutTime: Long, retryOnConnectionFailure: Boolean, interceptors: List<Interceptor>?, netInterceptors: List<Interceptor>?): OkHttpClient {
    val builder = OkHttpClient.Builder()
    interceptors?.let { it -> it.forEach { builder.addInterceptor(it) } }
    netInterceptors?.let { it -> it.forEach { builder.addNetworkInterceptor(it) } }
    return builder
            .connectTimeout(timeoutTime, TimeUnit.SECONDS)
            .writeTimeout(timeoutTime, TimeUnit.SECONDS)
            .readTimeout(timeoutTime, TimeUnit.SECONDS)
            .retryOnConnectionFailure(retryOnConnectionFailure)
            .build()
}