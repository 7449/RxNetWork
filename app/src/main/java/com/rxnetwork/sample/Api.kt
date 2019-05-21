package com.rxnetwork.sample

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * by y on 2016/8/7.
 */
object Api {

    const val ZL_BASE_API = "https://news-at.zhihu.com/api/4/"

    interface ZLService {
        @GET("news/latest")
        fun getList(): Observable<ListModel>

        @GET("news/latest")
        fun getString(): Observable<String>
    }

}
