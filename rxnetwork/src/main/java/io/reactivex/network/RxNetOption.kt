package io.reactivex.network

import retrofit2.Retrofit

/**
 * 网络请求配置项
 */
interface RxNetOption {
    val retrofit: Retrofit
}