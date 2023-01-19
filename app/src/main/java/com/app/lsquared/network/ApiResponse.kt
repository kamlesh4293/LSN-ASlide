package com.app.lsquared.network

data class ApiResponse(val status: Status, val data: String?, val message: String, val pos: Int = 0)