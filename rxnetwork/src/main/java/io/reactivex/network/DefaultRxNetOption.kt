package io.reactivex.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Default  [RxNetOption]
 */
class DefaultRxNetOption(
        private val baseUrl: String,
        private val converterFactory: Converter.Factory,
        private val interceptors: List<Interceptor> = ArrayList(),
        private val netInterceptors: List<Interceptor> = ArrayList()
) : RxNetOption {

    override val retrofit: Retrofit
        get() = Retrofit.Builder()
                .client(initHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

    private fun initHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            interceptors.let { it -> it.forEach { this.addInterceptor(it) } }
            netInterceptors.let { it -> it.forEach { this.addNetworkInterceptor(it) } }
            this.retryOnConnectionFailure(true)
        }.build()
    }

}