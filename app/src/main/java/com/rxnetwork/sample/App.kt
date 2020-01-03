package com.rxnetwork.sample

import android.app.Application
import android.content.Context
import android.os.Environment
import io.reactivex.cache.RxCache
import io.reactivex.network.RxNetWork
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * by y on 2017/2/27
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RxNetWork.initOption {
            superBaseUrl { Api.BASE_API }
            superConverterFactory { GsonConverterFactory.create() }
        }
        RxCache
                .instance
                .setDiskBuilder(RxCache.DiskBuilder(getDiskCacheDir(this, "RxCache")))
    }

    private fun getDiskCacheDir(context: Context, fileName: String): File {
        val cachePath: String
        cachePath = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            val externalCacheDir = context.externalCacheDir!!
            externalCacheDir.path
        } else {
            context.cacheDir.path
        }
        return File(cachePath + File.separator + fileName)
    }
}

