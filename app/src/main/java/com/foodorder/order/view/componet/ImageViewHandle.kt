package com.foodorder.order.view.componet

import android.app.Activity
import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.foodorder.order.model.data.GlideApp
import com.foodorder.order.model.data.InternalConstantData
import com.foodorder.order.view.activity.BaseActivity

interface ImageViewHandle {

    var mImageView: UnifiedImageView

    fun openFileResultSuccessHandle(newImageAddr: String)
    fun getBaseActivity(): BaseActivity

    fun initializeImageView() {
        initLocalProcess()
    }

    private fun initLocalProcess() {

        val imageListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                openFileChooser(getBaseActivity())
            }
        }
        mImageView.setOnClickListener(imageListener)
    }

    private fun openFileChooser(it: BaseActivity) {

        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        it.startActivityForResult(intent, InternalConstantData.PICK_IMAGE_REQUEST)
    }

    fun openFileResultHandle(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == InternalConstantData.PICK_IMAGE_REQUEST)
            && (resultCode == Activity.RESULT_OK)
            && (data != null) && (data.data != null)
        ) {
            val it = data.data!!.toString() //mLocalImageAddr must not be null, because if() already checked
            //如果客户选择了本地照片，需要更新mRemoteImageAddr
            openFileResultSuccessHandle(it)
            //需要把glide进行dagger的导入
            Glide.with(getBaseActivity()).load(it).into(mImageView)
        }
    }

    fun displayImageWithAddr(it: String) {
        GlideApp.with(getBaseActivity()).load(it).into(mImageView)//显示当前的图片
    }
}