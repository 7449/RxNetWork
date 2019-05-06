package com.rxnetwork.sample

import android.app.Application
import io.reactivex.network.RxNetWork
import io.reactivex.network.cache.RxCache
import retrofit2.converter.gson.GsonConverterFactory

/**
 * by y on 2017/2/27
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RxNetWork.initOption {
            superBaseUrl { Api.ZL_BASE_API }
            superConverterFactory { GsonConverterFactory.create() }
            superLogInterceptor { SimpleLogInterceptor() }
        }
        RxCache
                .instance
                .setDiskBuilder(RxCache.DiskBuilder(FileUtils.getDiskCacheDir(this, "RxCache")))
    }
}
