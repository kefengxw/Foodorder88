package com.foodorder.order.view.componet

data class UploadRemoteFoodDataUnit(
    var uniqueId: String = "",
    val name: String = "",
    var imageRemoteAddr: String = "",
    var imageRemotePath: String = "",
    val price: String = "",
    val description: String = "",
    val category: String = ""
)