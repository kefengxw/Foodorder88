package com.foodorder.order.view.componet

data class UpdateLocalUserDataUnit(
    val updateRemoteUser: UpdateRemoteUserDataUnit,
    var imageLocalAddr: String = ""
)