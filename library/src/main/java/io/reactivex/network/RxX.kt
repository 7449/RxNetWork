@file:Suppress("NOTHING_TO_INLINE")

package io.reactivex.network

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable

inline fun <T> Observable<T>.getApi(tag: Any, noinline rxNetWorkListenerKt: SimpleRxNetWorkListenerKt<T>.() -> Unit) {
    RxNetWork.getApi(tag, this, rxNetWorkListenerKt)
}

inline fun <T> Observable<T>.getApi(tag: Any, rxNetWorkListener: RxNetWorkListener<T>) {
    RxNetWork.instance.getApi(tag, this, rxNetWorkListener)
}

inline fun <T> Observable<T>.cancelTag(tag: Any) = also { RxNetWork.cancelTag(tag) }

inline fun <T> Observable<T>.with(activity: FragmentActivity, tag: Any) = also {
    val supportFragmentManager = activity.supportFragmentManager
    var tagFragment = supportFragmentManager.findFragmentByTag(RxNetWorkTagFragment.TAG)
    if (tagFragment == null) {
        tagFragment = RxNetWorkTagFragment.get()
        supportFragmentManager.beginTransaction().add(tagFragment, RxNetWorkTagFragment.TAG).commitAllowingStateLoss()
    }
    if (tagFragment is RxNetWorkTagFragment) {
        tagFragment.addNetWorkTags(tag)
    }
}

inline fun <T> Observable<T>.with(fragment: Fragment, tag: Any) = also {
    val activity = fragment.activity ?: return@also
    with(activity, tag)
}
