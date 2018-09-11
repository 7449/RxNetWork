package com.rxnetwork.sample;

import io.reactivex.network.RxNetWork;

/**
 * @author y
 */
public class SimpleJava {

    public void start() {
        RxNetWork instance = RxNetWork.Companion.getInstance();
        instance.setBaseUrl("");
        instance.setLogInterceptor(new SimpleLogInterceptor());
        instance.setHeaderInterceptor(new SimpleHeaderInterceptor());
    }
}
