package com.rxnetwork.sample

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * by y on 2016/8/7.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class ListModel {
    lateinit var data: List<DataModel>
}

@JsonIgnoreProperties(ignoreUnknown = true)
class DataModel {
    var title: String = ""
    var title_image: String = ""
}
