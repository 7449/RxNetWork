# RxNetWork
this is android network ,and RxBus

android 网络请求简化库

> RxNetWork项目试用：

~~[https://github.com/7449/ZLSimple](https://github.com/7449/ZLSimple)~~

> RxJsoupNetWork项目试用

[https://github.com/7449/JsoupSample](https://github.com/7449/JsoupSample)

> 
    
    implementation 'com.ydevelop:rxNetWork:0.1.97'

    implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.5.0'

    // json
    implementation 'com.squareup.retrofit2:converter-jackson:2.5.0'

    // 如果使用`RxCache`,则使用`gson`
    implementation 'com.squareup.retrofit2:converter-gson:2.5.0'

> 建议初始化:

    class App : Application() {
    
        override fun onCreate() {
            super.onCreate()
            RxNetWork.initOption {
                superBaseUrl {  }
                superConverterFactory { GsonConverterFactory.create() }
                superLogInterceptor { SimpleLogInterceptor() }
            }
            RxCache.setDiskBuilder(RxCache.DiskBuilder(FileUtils.getDiskCacheDir(context, "RxCache")))
        }
    }


> 支持以下自定义（如果不想使用内置类库可自定义）：

OkHttpClient

Converter.Factory

CallAdapter.Factory

> 使用方法：

这里假设在`Application`里已经自定义好`BaseUrl`;

参数一： tag，用于取消网络请求
参数二：observable
参数三： 回调，网络请求开始，异常，结束，成功之类的状态


* 取消网络请求：

	    RxNetWork.cancel()

        RxNetWork
                .observable(class.java)
                .get()
                .getApi(javaClass.simpleName) {
                    onNetWorkSuccess {}
                    onNetWorkComplete {}
                    onNetWorkError {}
                    onNetWorkStart {}
                }

> RxCache

`okhttp`不支持post缓存，所以新增`RxCache`对缓存的支持

* 只走网络 

    Observable
              .compose(RxCache.getInstance().<T>transformerN())
              
              
* 走缓存

true ： 有网的情况下 网络优先，否则 缓存优先

    Observable
              .compose(RxCache.getInstance().transformerCN("", true, new TypeToken<Any>() {}))



> 配置Header and Log:

如果使用默认的`okhttp`,配置Header需要如下操作：

        RxNetWork.initOption {
            superLogInterceptor { }
            superHeaderInterceptor {  }
        }

> RxBus使用：

#### 发送消息：

        RxBus.post(tag,message)
        RxBus.post(tag)

#### 注册消息体：

        RxBus.register(tag,object :RxBusCallBack<Any>{
                    override fun onBusError(throwable: Throwable) {
                    }

                    override fun busOfType(): Class<Any> {
                    }

                    override fun onBusNext(entity: Any) {
                    }
                })

#### 解绑：

	RxBus.unregister(tag)
	
## License

    Copyright (C) 2017 yuebigmeow@gamil.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.




