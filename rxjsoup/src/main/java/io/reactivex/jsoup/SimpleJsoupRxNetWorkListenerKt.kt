package io.reactivex.jsoup

import io.reactivex.network.RxNetWorkListener
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

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