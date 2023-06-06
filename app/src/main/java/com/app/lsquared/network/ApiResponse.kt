package com.app.lsquared.network

import com.app.lsquared.model.Item

data class ApiResponse(val status: Status, val data: String?, val message: String, val pos: Int = 0, val item: Item? = null)

data class ApiResponseVimeo(val status: Status, val url: String?, val item: Item?, val pos: Int = 0)