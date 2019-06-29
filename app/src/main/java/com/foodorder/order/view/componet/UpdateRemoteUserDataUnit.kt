package com.foodorder.order.view.componet

data class UpdateRemoteUserDataUnit(
    var uniqueId: String = "",      //means that, to describe this data at firebase, just for verification
    val userName: String = "",      //just for verification
    val nickName: String = "",
    val companyName: String = "",
    val restaurantName: String = "",
    val restaurantAddr: String = "",
    val restaurantFloor: String = "",
    val cuisineName: String = "",
    val breakfast: String = "",     //value: breakfast or breakfastBuffet
    val lunch: String = "",         //value: lunch or lunchBuffet
    val dinner: String = "",        //value: dinner or dinnerBuffet
    val square: String = "",
    val tableNumber: String = "",
    val employeeNumber: String = "",
    var imageRemoteAddr: String = "",
    var imageRemotePath: String = ""
)