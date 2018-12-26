package com.rxnetwork.sample

/**
 * by y on 2016/8/7.
 */
class ListModel {

    var title: String = ""
    var titleImage: String = ""
    var slug: Int = 0
    lateinit var author: Author

    class Author {
        var profileUrl: String = ""
        var bio: String = ""
        var name: String = ""

    }
}
