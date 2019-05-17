package io.reactivex.network

import androidx.fragment.app.Fragment
import java.util.*

/**
 * @author y
 * @create 2019-05-15
 */
class RxNetWorkTagFragment : Fragment() {

    companion object {

        const val TAG = "RxNetWorkTagFragment"

        fun get(): RxNetWorkTagFragment {
            return RxNetWorkTagFragment()
        }
    }

    private val netWorkTags = ArrayList<Any>()

    fun addNetWorkTags(tag: Any) {
        if (!netWorkTags.contains(tag)) {
            netWorkTags.add(tag)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxNetWork.cancelListTag(netWorkTags)
        netWorkTags.clear()
    }
}
