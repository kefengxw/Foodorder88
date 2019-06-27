package com.foodorder.order.view.componet

data class UploadLocalFoodDataUnit(
    val uploadRemoteFood: UploadRemoteFoodDataUnit,
    var imageLocalAddr: String = ""
)