package io.reactivex.network

/**
 * @author y
 */
class RxNetWorkTask<T> {

    var data: T
    var tag: Any

    constructor(data: T, tag: Any) {
        this.data = data
        this.tag = tag
    }
}