package io.reactivex.jsoup

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface JsoupService {
    @GET
    fun get(@Url url: String): Observable<ResponseBody>

    @POST
    fun post(@Url url: String): Observable<ResponseBody>
}