package io.reactivex.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import java.io.IOException

interface JsoupService {
    @GET
    fun get(@Url url: String): Observable<ResponseBody>

    @POST
    fun post(@Url url: String): Observable<ResponseBody>
}

fun <T> Observable<ResponseBody>.jsoupApi(tag: Any, rxNetWorkListenerKt: SimpleJsoupRxNetWorkListenerKt<T>.() -> Unit) {
    RxNetWork.instance.getApi(tag, this, SimpleJsoupRxNetWorkListenerKt<T>().also(rxNetWorkListenerKt).build())
}

class SimpleJsoupRxNetWorkListenerKt<T> {

    private var onNetWorkStart: (() -> Unit)? = null
    private var onNetWorkError: ((e: Throwable) -> Unit)? = null
    private var onNetWorkComplete: (() -> Unit)? = null
    private var onNetWorkSuccess: ((data: T) -> Unit)? = null
    private lateinit var jsoupRule: ((document: Document) -> T)
    private var baseUrl = ""

    fun onNetWorkStart(onNetWorkStart: () -> Unit) {
        this.onNetWorkStart = onNetWorkStart
    }

    fun onNetWorkError(onNetWorkError: (e: Throwable) -> Unit) {
        this.onNetWorkError = onNetWorkError
    }

    fun onNetWorkComplete(onNetWorkComplete: () -> Unit) {
        this.onNetWorkComplete = onNetWorkComplete
    }

    fun jsoupRule(jsoupRule: (document: Document) -> T) {
        this.jsoupRule = jsoupRule
    }

    fun onNetWorkSuccess(onNetWorkSuccess: (data: T) -> Unit) {
        this.onNetWorkSuccess = onNetWorkSuccess
    }

    fun getBaseUrl(url: String) {
        this.baseUrl = url
    }

    internal fun build(): RxNetWorkListener<ResponseBody> {
        return object : RxNetWorkListener<ResponseBody> {
            override fun onNetWorkStart() {
                onNetWorkStart?.invoke()
            }

            override fun onNetWorkError(e: Throwable) {
                onNetWorkError?.invoke(e)
            }

            override fun onNetWorkComplete() {
                onNetWorkComplete?.invoke()
            }

            override fun onNetWorkSuccess(data: ResponseBody) {
                try {
                    onNetWorkSuccess?.invoke(jsoupRule.invoke(Jsoup.parse(data.string(), baseUrl)))
                } catch (e: Exception) {
                    onNetWorkError(IOException())
                }
            }
        }
    }
}