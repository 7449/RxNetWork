package io.reactivex.jsoup

import io.reactivex.Observable
import io.reactivex.network.RxNetWork
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface JsoupService {

    companion object {

        /**
         * 获取 [JsoupService] 实例
         */
        fun createGET(url: String): Observable<ResponseBody> = RxNetWork.observable(JsoupService::class.java).get(url)


        /**
         * 获取 [JsoupService] 实例
         */
        fun createPOST(url: String): Observable<ResponseBody> = RxNetWork.observable(JsoupService::class.java).post(url)

    }

    @GET
    fun get(@Url url: String): Observable<ResponseBody>

    @POST
    fun post(@Url url: String): Observable<ResponseBody>
}