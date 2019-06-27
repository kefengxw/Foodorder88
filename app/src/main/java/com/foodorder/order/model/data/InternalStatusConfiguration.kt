package com.foodorder.order.model.data

import com.google.firebase.auth.FirebaseUser

enum class ReturnCode(val x: Int) {
    ReturnCode_Err_Uri(0x01),
    ReturnCode_Err_Name(0x02),
    ReturnCode_Err_Description(0x03),
    ReturnCode_Err_Price(0x04),
    ReturnCode_Err_Nick_Name(0x05),
    ReturnCode_Err_Company_Name(0x06),
    ReturnCode_Err_Restaurant_Name(0x07),
    ReturnCode_Err_Restaurant_Addr(0x08),
    ReturnCode_Err_Restaurant_Floor(0x09),
    ReturnCode_Err_Cuisine_Name(0x0A),
    ReturnCode_Err_Scope(0x0B),
    ReturnCode_Err_Square(0x0C),
    ReturnCode_Err_Table_Number(0x0D),
    ReturnCode_Err_Employee_Number(0x0E),
    ReturnCode_Success(0xFF),
}

enum class FireBaseFolderType(val x: Int) {
    UserFolder(1),
    FoodFolder(2),
    IngredientFolder(3)
}

object InternalStatusConfiguration {

    const val INDEX_BAR_LETTER_SPLIT = 0.819f//decide by Golden Section 0.618
    const val INIT_VISIT_TOKEN = ""
    var VISIT_TOKEN: String = INIT_VISIT_TOKEN
    var AUTH_TOKEN: String = ""

    fun shouldTakeToken(): Boolean = (VISIT_TOKEN == INIT_VISIT_TOKEN)

    fun getToken(): String = VISIT_TOKEN

    fun updateToken(newToken: String) {
        VISIT_TOKEN = newToken
        AUTH_TOKEN = "Bearer $VISIT_TOKEN"
    }

    fun getCurrentCountry() {

    }

    private var loginStatus: Boolean = false
    fun getLoginStatus(): Boolean = loginStatus
    fun updateLoginStatus(it: Boolean) {
        loginStatus = it
    }

    private var loginUserId: String = ""//this is for Database Connection as connection name
    fun getLoginUserId(): String = loginUserId
    fun updateLoginUserId(it: String) {
        loginUserId = it
    }

    private var loginUser: FirebaseUser? = null
    fun getLoginUserEmail(): String =
        if ((loginUser == null) || (loginUser!!.email == null)) "" else loginUser!!.email!!

    fun updateLoginUser(it: FirebaseUser) {
        loginUser = it
    }
}