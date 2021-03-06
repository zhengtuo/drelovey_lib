package com.drelovey.provider.data.model

import com.squareup.moshi.JsonClass

/**
 *

 * @Author: Drelovey
 * @CreateDate: 2020/1/22 16:31
 */
//@JsonClass moshi必备
@JsonClass(generateAdapter = true)
data class BaseEntity<T> (

    val code: Int = 0,
    var data: T? = null,
    val status: Boolean = false

)