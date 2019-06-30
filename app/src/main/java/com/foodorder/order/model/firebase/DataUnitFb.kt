package com.foodorder.order.model.firebase

class DataUnitFb<RemoteT>(
    var caseKey: DataUnitCase,
    var remote: DataUnitRemoteFb<RemoteT>
)

class DataUnitCase(
    var imageLocalAddr: String = "",
    var imageRemoteAddr: String = "",
    var uniqueId: String = "",
    val folder: FireBaseFolder
)

class DataUnitRemoteFb<RemoteT>(
    var uniqueId: String = "",      //means that, to describe this data at firebase, just for verification
    var remoteInfo: RemoteT,
    var remoteImage: DataUnitRemoteImageFb
)

class DataUnitRemoteImageFb(
    var imageRemoteAddr: String = "",
    var imageRemotePath: String = ""
)

enum class FireBaseFolder(val value: String) {
    UserFolder("user"),
    FoodFolder("food"),
    IngredientFolder("ingredient")
}

//RemoteT of Food
data class FoodDataUnitRemoteFb(    //RemoteT, remotePart
    var name: String = "",
    var price: String = "",
    var description: String = "",
    var category: String = ""
)

//RemoteT of User
data class UserDataUnitRemoteFb(    //RemoteT, remotePart
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
    val employeeNumber: String = ""
)

class DataUnitQueryCase(
    var imageLocalAddr: String = "",
    var imageRemoteAddr: String = "",
    var uniqueId: String = "",
    val folder: FireBaseFolder
)

class GetUserDataUnitRemoteFb {      //same as DataUnitRemoteFb<UserDataUnitRemoteFb>

    var uniqueId: String = ""       //means that, to describe this data at firebase, just for verification
    var remoteInfo: UserDataUnitRemoteFb = UserDataUnitRemoteFb()
    var remoteImage: DataUnitRemoteImageFb = DataUnitRemoteImageFb()

    constructor() {} // Needed for Firebase
}