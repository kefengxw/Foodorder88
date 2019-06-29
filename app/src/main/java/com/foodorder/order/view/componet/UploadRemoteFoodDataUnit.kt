package com.foodorder.order.view.componet

data class UploadRemoteFoodDataUnit(
    var uniqueId: String = "",      //means that, to describe this data at firebase, and can be the unique name of food
    val name: String = "",
    var imageRemoteAddr: String = "",
    var imageRemotePath: String = "",
    val price: String = "",
    val description: String = "",
    val category: String = ""
)