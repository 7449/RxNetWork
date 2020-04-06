# RxNetWork

## rxnetwork

    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.6.2'
    implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
    implementation 'com.ydevelop:rxNetWork:0.2.2'

## rxbus

    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    implementation 'com.ydevelop:rxbus:0.0.2'

## rxcache

    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    implementation 'com.google.code.gson:gson:2.8.5'
    implementation 'com.squareup.retrofit2:retrofit:2.6.2'
    implementation 'com.ydevelop:rxcache:0.0.2'

## rxjsoup

    implementation 'com.ydevelop:rxNetWork:0.2.2'
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    implementation 'com.squareup.retrofit2:retrofit:2.6.2'
    implementation "org.jsoup:jsoup:1.12.1"
    implementation 'com.ydevelop:rxjsoup:0.0.2'

#### rxnetwork init

    RxNetWork.initialization(DefaultRxNetOption(
            baseUrl = Api.BASE_API,
            converterFactory = GsonConverterFactory.create()
    ))
    
#### rxnetwork api

    Service::class.java
            .create()
            .getList()
            .cancel(javaClass.simpleName)
            .request(javaClass.simpleName) {
                onNetWorkSuccess {  }
                onNetWorkComplete {  }
                onNetWorkError {  }
                onNetWorkStart {  }
            }
            
#### rxcache init

    class App : Application() {
        override fun onCreate() {
            super.onCreate()
            RxCache.setDiskBuilder(RxCache.DiskBuilder(FileUtils.getDiskCacheDir(context, "RxCache")))
        }
    }

#### rxcache api

    Observable
              .compose(RxCache.getInstance().<T>transformerN())
              
              
    Observable
              .compose(RxCache.getInstance().transformerCN("", true, new TypeToken<Any>() {}))


#### rxbus

    RxBus.instance.post(tag,message)

    RxBus.instance.register(tag,object :RxBusCallBack<Any>{
                override fun onBusError(throwable: Throwable) {
                }

                override fun busOfType(): Class<Any> {
                }

                override fun onBusNext(entity: Any) {
                }
            })

	RxBus.instance.unregister(tag)
	RxBus.instance.unregisterAllBus()
	
#### rxjsoup

    JsoupService
            .createGET(url)
            .cancel("tag")
            .request("tag") {
                onNetWorkError {
                    Log.e("jsoup", it.message.toString())
                }
                onNetWorkSuccess {
                    Log.i("jsoup", it.string())
                }
            }