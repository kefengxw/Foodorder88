package com.foodorder.order.model.data

enum class SectionFragmentManager() {
    SECTION_SUMMARY_DAY1_FRAG1,
    SECTION_SUMMARY_DAY2_FRAG2,
}

enum class SectionFragmentChef() {
    SECTION_COOKING_DAY1_FRAG1,
    SECTION_COOKING_DAY2_FRAG2,
    SECTION_COOKING_DAY3_FRAG3,
}

enum class SectionFragmentGuest() {
    SECTION_WELCOME_FRAG1,
    SECTION_SPECIAL_FRAG2,
    SECTION_STARTER_FRAG3,
    SECTION_MAIN_FRAG4,
    SECTION_DESSERT_FRAG5,
    SECTION_WINE_FRAG6,
    SECTION_CONCEPT_FRAG7,
}

enum class DeviceRole(val x: Int) {
    Manager(1),
    Chef(2),
    Guest(3)
}

object InternalDataConfiguration {

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

    const val MAX_UPLOAD_INGREDIENT = 8

    //current Setting Role(Manager,Chef,Guest)
    var DEVICE_CURRENT_ROLE = DeviceRole.Manager
    var TOTAL_TAB_FRAGMENT_NUMBER_DEFAULT = 2

    fun setDeviceRole(x: String) {

        DEVICE_CURRENT_ROLE = when (x) {
            "manager" -> DeviceRole.Manager
            "chef" -> DeviceRole.Chef
            else -> DeviceRole.Guest
        }
        setTotalTabNumber()
    }

    //the number of Tab_layout sheet
    var TOTAL_TAB_FRAGMENT_NUMBER = TOTAL_TAB_FRAGMENT_NUMBER_DEFAULT//default

    fun getTotalTabNumber(): Int = TOTAL_TAB_FRAGMENT_NUMBER
    fun setTotalTabNumber() {

        TOTAL_TAB_FRAGMENT_NUMBER = when (DEVICE_CURRENT_ROLE) {
            DeviceRole.Manager -> 2
            DeviceRole.Chef -> 3
            else -> 7
        }
    }

    //the number of on Special sheet
    var TOTAL_ITEM_NUMBER_ON_SPECIAL_TAB = 2

    fun getItemNumberSpecial(): Int = TOTAL_ITEM_NUMBER_ON_SPECIAL_TAB
    fun setItemNumberSpecial(it: Int) {
        TOTAL_ITEM_NUMBER_ON_SPECIAL_TAB = it
    }

    //the number of on Starter sheet
    var TOTAL_ITEM_NUMBER_ON_STARTER_TAB = 3

    fun getItemNumberStarter(): Int = TOTAL_ITEM_NUMBER_ON_STARTER_TAB
    fun setItemNumberStarter(it: Int) {
        TOTAL_ITEM_NUMBER_ON_STARTER_TAB = it
    }

    //the number of on Main Course sheet
    var TOTAL_ITEM_NUMBER_ON_MAIN_COURSE_TAB = 4

    fun getItemNumberMainCourse(): Int = TOTAL_ITEM_NUMBER_ON_MAIN_COURSE_TAB
    fun setItemNumberMainCourse(it: Int) {
        TOTAL_ITEM_NUMBER_ON_MAIN_COURSE_TAB = it
    }

    //the number of on Dessert sheet
    var TOTAL_ITEM_NUMBER_ON_DESSERT_TAB = 5

    fun getItemNumberDessert(): Int = TOTAL_ITEM_NUMBER_ON_DESSERT_TAB
    fun setItemNumberDessert(it: Int) {
        TOTAL_ITEM_NUMBER_ON_DESSERT_TAB = it
    }

    //the number of on WineDrink sheet
    var TOTAL_ITEM_NUMBER_ON_WINED_RINK_TAB = 6

    fun getItemNumberWineDrink(): Int = TOTAL_ITEM_NUMBER_ON_WINED_RINK_TAB
    fun setItemNumberWineDrink(it: Int) {
        TOTAL_ITEM_NUMBER_ON_WINED_RINK_TAB = it
    }
}