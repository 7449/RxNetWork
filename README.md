# RxNetWork

## rxnetwork

    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.6.2'
    implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
    
## rxbus

    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'

## rxcache

    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    implementation 'com.google.code.gson:gson:2.8.5'
    implementation 'com.squareup.retrofit2:retrofit:2.6.2'

## rxjsoup

    implementation 'com.ydevelop:rxNetWork:0.2.1'
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    implementation 'com.squareup.retrofit2:retrofit:2.6.2'
    implementation "org.jsoup:jsoup:1.12.1"
   
#### rxnetwork init

    RxNetWork.initOption {
        superBaseUrl { baseUrl }
        superConverterFactory { GsonConverterFactory.create() }
    }
    
    or

    RxNetWork.initialization(object : RxNetOptionFactory {
        override val adapterFactory: CallAdapter.Factory
            get() = 
        override val baseUrl: String
            get() = 
        override val converterFactory: Converter.Factory
            get() = 
        override val interceptors: List<Interceptor>?
            get() = 
        override val netInterceptors: List<Interceptor>?
            get() = 
        override val okHttpClient: OkHttpClient
            get() = 
        override val retrofit: Retrofit
            get() = 
        override val retryOnConnectionFailure: Boolean
            get() = 
        override val timeoutTime: Long
            get() = 
    })
    
#### rxnetwork api

    RxNetWork
            .observable(class.java)
            .get()
            .cancelTag(javaClass.simpleName)
            .getApi(javaClass.simpleName) {
                onNetWorkStart {}
                onNetWorkSuccess {}
                onNetWorkComplete {}
                onNetWorkError {}
            }
            
#### rxcache init

    class App : Application() {
        override fun onCreate() {
            super.onCreate()
            RxCache.setDiskBuilder(RxCache.DiskBuilder(FileUtils.getDiskCacheDir(context, "RxCache")))
        }
    }

#### rxcache api

* 只走网络 


    Observable
              .compose(RxCache.getInstance().<T>transformerN())
              
              
* 走缓存

true ： 有网的情况下 网络优先，否则 缓存优先

    Observable
              .compose(RxCache.getInstance().transformerCN("", true, new TypeToken<Any>() {}))


#### RxBus

    RxBus.postBus(tag,message)

    RxBus.register(tag,object :RxBusCallBack<Any>{
                override fun onBusError(throwable: Throwable) {
                }

                override fun busOfType(): Class<Any> {
                }

                override fun onBusNext(entity: Any) {
                }
            })

	RxBus.unregisterBus(tag)
	RxBus.unregisterAllBus()
	
#### rxjsoup

    RxNetWork
            .observable(JsoupService::class.java)
            .get(url) // post(url)
            .jsoupApi<String>("tag") {
                onNetWorkStart { }
                onNetWorkSuccess { }
                onNetWorkComplete { }
                onNetWorkError { }
                jsoupRule { document -> document.toString() }
            }