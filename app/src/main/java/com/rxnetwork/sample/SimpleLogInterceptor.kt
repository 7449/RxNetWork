package com.rxnetwork.sample

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * by y on 2017/2/22.
 */

internal class SimpleLogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        Log.i(TAG, HEADER_TITLE + response.headers().toString())
        Log.i(TAG, CONTENT_TITLE + content)
        return if (response.body() != null) {
            response.newBuilder().body(ResponseBody.create(mediaType, content)).build()
        } else {
            response
        }
    }

    companion object {

        private const val TAG = "RxNetWorkApiLog"
        private const val CONTENT_TITLE = "Api Content-->:"
        private const val HEADER_TITLE = "Api Header-->"
    }
}
