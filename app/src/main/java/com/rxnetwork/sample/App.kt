package com.rxnetwork.sample

import android.app.Application
import io.reactivex.network.RxNetOptionFactory
import io.reactivex.network.RxNetWork
import io.reactivex.network.SimpleRxNetOptionFactory
import io.reactivex.network.cache.RxCache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * by y on 2017/2/27
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RxNetWork.initialization(SimpleRxNetOptionFactory(Api.ZL_BASE_API, JacksonConverterFactory.create()))
        RxCache
                .instance
                .setDiskBuilder(RxCache.DiskBuilder(FileUtils.getDiskCacheDir(this, "RxCache")))
    }
}

class SimpleRxNetOption : RxNetOptionFactory {
    override val baseUrl: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val timeoutTime: Long
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val retryOnConnectionFailure: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val okHttpClient: OkHttpClient
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val retrofit: Retrofit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val adapterFactory: CallAdapter.Factory
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val converterFactory: Converter.Factory
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val logInterceptor: Interceptor?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val headerInterceptor: Interceptor?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}
