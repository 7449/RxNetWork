package com.rxnetwork.sample

import android.app.Application
import io.reactivex.network.RxNetWork
import io.reactivex.network.cache.RxCache

/**
 * by y on 2017/2/27
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        RxNetWork
                .instance
                .apply {
                    baseUrl = Api.ZL_BASE_API
                    logInterceptor = SimpleLogInterceptor()
                }
        RxCache
                .instance
                .setDiskBuilder(RxCache.DiskBuilder(FileUtils.getDiskCacheDir(this, "RxCache")))
    }
}
