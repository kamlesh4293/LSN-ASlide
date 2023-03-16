package com.app.lsquared.model/*
Copyright (c) 2022 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */

import com.google.gson.annotations.SerializedName

data class Frame (

	@SerializedName("sort") val sort : String = "",
	@SerializedName("id") val id : Int = 0,
	@SerializedName("name") val name : String = "",
	@SerializedName("w") val w : Int = 0,
	@SerializedName("h") val h : Int = 0,
	@SerializedName("x") val x : Int = 0,
	@SerializedName("y") val y : Int = 0,
	@SerializedName("z") val z : Int = 0,
	@SerializedName("r") val r : Int = 0,
	@SerializedName("bg") val bg : String = "",
	@SerializedName("align") val align : String = "",
	@SerializedName("a") val a : String = "",
	@SerializedName("bga") val bga : String = "",
	@SerializedName("timeRange") val timeRange : String = "",
	@SerializedName("tr") val tr : String = "",
	@SerializedName("st") val st : String = "",
	@SerializedName("et") val et : String = "",
	@SerializedName("br") val br : String = "",
	@SerializedName("item") val item : List<Item> = arrayListOf(),
	var position : Int = 0
)

