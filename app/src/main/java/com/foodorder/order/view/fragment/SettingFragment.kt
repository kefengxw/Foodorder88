package com.foodorder.order.view.fragment

import android.os.Bundle
import androidx.preference.*
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalDataConfiguration.setDeviceRole
import com.foodorder.order.view.activity.AboutActivity.Companion.startAboutActivity
import com.foodorder.order.view.activity.EditProfileActivity.Companion.startEditProfileActivity
import com.foodorder.order.view.activity.UploadOverviewActivity.Companion.startUploadOverviewActivity
import com.foodorder.order.view.componet.LogoutDialog.Companion.startLogoutDialog

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)

        val listRole = findPreference<ListPreference>("role")
        val textTable = findPreference<EditTextPreference>("table")
        val editProfile = findPreference<Preference>("profile")
        val groupUpload = findPreference<PreferenceCategory>("upload")
        val groupAllsheet = findPreference<PreferenceCategory>("allSheet")

        buildPreference(textTable, groupUpload, editProfile, groupAllsheet, listRole!!.value)

        listRole?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener() { preference: Preference, any: Any ->
                buildPreference(textTable, groupUpload, editProfile, groupAllsheet, any as String)
                true
            }

        val it = arrayOf(
            "uploadWelcome",
            "uploadSpecial",
            "uploadStarter",
            "uploadMainCourse",
            "uploadDessert",
            "uploadWineDrink",
            "profile",
            "logout"
        )
        it.forEach {
            val pre = findPreference<Preference>(it)
            pre?.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    onUploadPreferenceClick(it)
                    true
                }
        }
    }

    private fun buildPreference(
        textTable: EditTextPreference?,
        groupUpload: PreferenceCategory?,
        editProfile: Preference?,
        groupAllSheet: PreferenceCategory?,
        any: String
    ) {
        textTable?.isVisible = (any == "guest")
        groupUpload?.isVisible = (any == "manager")
        editProfile?.isVisible = (any == "manager")
        groupAllSheet?.isVisible = (any == "guest")
        setDeviceRole(any)
    }
    /*onBuildHeader*/

    //inner Class内部类默认持有外部类的引用，直接访问外部类的方法属性
    //没有inner修饰的，嵌套类不持有外部类的引用，必须通过外部类的对象访问
    private fun onUploadPreferenceClick(it: Preference) {

        when (it.key) {
            "logout" -> {
                startLogoutDialog(activity!!.supportFragmentManager)
            }
            "profile" -> {
//String getUid()	返回用户的uid
//String getDisplayName()	返回用户名
//String getEmail()	返回用户email
//String getPhotoUrl()	返回用户头像
//boolean isAnonymous()	是否匿名
//Task updatePassword(String)	更新密码
//Task updateEmail(String)	更新email
                startEditProfileActivity(activity!!)
            }
            "uploadWelcome" -> {
                startAboutActivity(activity!!)
            }
            "uploadStarter" -> {
                startUploadOverviewActivity(activity!!, "starter")
            }
            "uploadMainCourse" -> {
                startUploadOverviewActivity(activity!!, "mainCourse")
            }
            "uploadDessert" -> {
                startUploadOverviewActivity(activity!!, "dessert")
            }
            "uploadWineDrink" -> {
                startUploadOverviewActivity(activity!!, "wineDrink")
            }
            else -> {
                startUploadOverviewActivity(activity!!)
            }
        }
    }
}