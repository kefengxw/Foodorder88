package com.foodorder.order.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object SecuritySharedPreferencesFactory {

    @Volatile
    private var mInstanceSSharedPre: SharedPreferences? = null

    @Synchronized
    fun getInstanceSSharedPre(ctx: Context): SharedPreferences {

        if (mInstanceSSharedPre == null) {

            val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            mInstanceSSharedPre = EncryptedSharedPreferences.create(
                "foodorder_user",
                masterKeyAlias,
                ctx,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
        return mInstanceSSharedPre!!
    }

    fun destroyInstanceRepos() {
        mInstanceSSharedPre = null
    }
}