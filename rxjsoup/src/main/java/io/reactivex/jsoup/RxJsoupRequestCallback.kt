package io.reactivex.jsoup

import io.reactivex.network.RxRequestCallback
import okhttp3.ResponseBody
import java.io.IOException

interface RxJsoupRequestCallback : RxRequestCallback<ResponseBody> {

    @Throws(IOException::class)
    override fun onNetWorkSuccess(data: ResponseBody) {
        onJsoupSuccess(data.string())
    }

    fun onJsoupSuccess(value: String)

}