package io.reactivex.network

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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

interface RxNetOptionFactory {
    val baseUrl: String
    val gson: Gson
    val timeoutTime: Long
    val retryOnConnectionFailure: Boolean
    val okHttpClient: OkHttpClient
    val retrofit: Retrofit
    val converterFactory: Converter.Factory
    val adapterFactory: CallAdapter.Factory
    val logInterceptor: Interceptor?
    val headerInterceptor: Interceptor?
}

@Suppress("LeakingThis")
open class SimpleRxNetOptionFactory(override val baseUrl: String,
                                    override val logInterceptor: Interceptor?,
                                    override val headerInterceptor: Interceptor?) : RxNetOptionFactory {
    override val gson: Gson = Gson()
    override val timeoutTime: Long = 15
    override val retryOnConnectionFailure: Boolean = true
    override val converterFactory: Converter.Factory = GsonConverterFactory.create(gson)
    override val adapterFactory: CallAdapter.Factory = RxJava2CallAdapterFactory.create()
    override val okHttpClient: OkHttpClient = initOkHttp(timeoutTime, retryOnConnectionFailure, logInterceptor, headerInterceptor)
    override val retrofit: Retrofit =
            Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl)
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(adapterFactory)
                    .build()
}

private fun initOkHttp(timeoutTime: Long, retryOnConnectionFailure: Boolean, logInterceptor: Interceptor?, headerInterceptor: Interceptor?): OkHttpClient {
    val builder = OkHttpClient.Builder()
    if (logInterceptor != null) {
        builder.addInterceptor(logInterceptor)
    }
    if (headerInterceptor != null) {
        builder.addInterceptor(headerInterceptor)
    }
    return builder
            .connectTimeout(timeoutTime, TimeUnit.SECONDS)
            .writeTimeout(timeoutTime, TimeUnit.SECONDS)
            .readTimeout(timeoutTime, TimeUnit.SECONDS)
            .retryOnConnectionFailure(retryOnConnectionFailure)
            .build()
}