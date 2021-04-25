package com.drelovey.realize.data.error

/**
 * Created by AhmedEltaher on 5/12/2016
 */

@Suppress("unused")
class Error(val code: Int, val description: String) {
    constructor(exception: Exception) : this(
        code = UN_KNOW, description = exception.message
            ?: ""
    )

    companion object {
        const val NO_INTERNET_CONNECTION = 1
        const val UN_KNOW = -1
    }
}