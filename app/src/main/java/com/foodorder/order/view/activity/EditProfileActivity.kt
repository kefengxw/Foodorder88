package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalStatusConfiguration.getLoginUserId
import com.foodorder.order.model.data.ReturnCode
import com.foodorder.order.model.data.ReturnCode.*
import com.foodorder.order.model.data.Status
import com.foodorder.order.model.firebase.FirebaseResult
import com.foodorder.order.view.componet.*
import com.foodorder.order.viewmodel.EditProfileViewModel
import kotlinx.android.synthetic.main.edit_profile_activity.*

class EditProfileActivity : BaseActivity(), ImageViewHandle, UnifiedSpinnerHandle.HandleSpinnerResult {

    private lateinit var mViewModel: EditProfileViewModel

    override lateinit var mImageView: UnifiedImageView
    private lateinit var mUpdateBtn: UnifiedButton
    private lateinit var mEditTextNickName: UnifiedEditText
    private lateinit var mEditTextCompanyName: UnifiedEditText
    private lateinit var mEditTextRestaurantName: UnifiedEditText
    private lateinit var mEditTextRestaurantAddr: UnifiedEditText
    private lateinit var mEditTextRestaurantFloor: UnifiedEditText
    private lateinit var mEditTextCuisineName: UnifiedEditText

    private lateinit var mSpinnerSquare: UnifiedSpinnerHandle
    private lateinit var mSpinnerTableNum: UnifiedSpinnerHandle
    private lateinit var mSpinnerEmployeeNum: UnifiedSpinnerHandle
    private var mSpinnerListSquare = ArrayList<UnifiedSpinnerItem>()
    private var mSpinnerListTableNum = ArrayList<UnifiedSpinnerItem>()
    private var mSpinnerListEmployeeNum = ArrayList<UnifiedSpinnerItem>()

    private lateinit var mBreakfastCheck: UnifiedCheckedTextView
    private lateinit var mLunchCheck: UnifiedCheckedTextView
    private lateinit var mDinnerCheck: UnifiedCheckedTextView
    private var mBreakfast: String = ""
    private var mLunch: String = ""
    private var mDinner: String = ""
    private lateinit var mBreakfastBuffet: SwitchCompat
    private lateinit var mLunchBuffet: SwitchCompat
    private lateinit var mDinnerBuffet: SwitchCompat

    private val mProgressInfo = ProgressInfo(this)
    private var mLocalImageAddr: String = ""
    private var mRemoteImageAddr: String = ""
    private var mRemoteImagePath: String = ""


    companion object {
        fun startEditProfileActivity(ctx: Context) {
            val intent = Intent(ctx, EditProfileActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.edit_profile_activity
    }

    override fun initOnCreateToolbar(): Toolbar? {
        return edit_profile_tool_bar as Toolbar
    }

    override fun initToolbarTitle(): String {
        return "Edit/Upload Profile"
    }

    override fun initInjector() {

    }

    override fun initViewModel() {
        mViewModel = ViewModelProviders.of(this@EditProfileActivity).get(EditProfileViewModel::class.java)
        mViewModel.getUpdateResult().observe(this, observerResult)
    }

    override fun initView() {
        mUpdateBtn = update_button
        mImageView = logo_image
        mEditTextNickName = nickname
        mEditTextCompanyName = enterprise_name
        mEditTextRestaurantName = restaurant_name
        mEditTextRestaurantAddr = restaurant_addr
        mEditTextRestaurantFloor = restaurant_floor
        mEditTextCuisineName = cuisine

        mBreakfastCheck = breakfast_check
        mLunchCheck = lunch_check
        mDinnerCheck = dinner_check

        mBreakfastBuffet = breakfast_switch
        mLunchBuffet = lunch_switch
        mDinnerBuffet = dinner_switch

        mProgressInfo.progressInfoInit(R.id.update_profile_progress_info)

        initSubView()
    }

    private fun initSubView() {
        initializeImageView()
    }

    override fun enableHomeAsUp(): Boolean {
        return true
    }

    override fun initListener() {
        mUpdateBtn.setOnClickListener(updateBtnListener)

        mBreakfastCheck.setOnClickListener(checkListener)
        mLunchCheck.setOnClickListener(checkListener)
        mDinnerCheck.setOnClickListener(checkListener)

        mBreakfastBuffet.setOnClickListener(switchListener)
        mLunchBuffet.setOnClickListener(switchListener)
        mDinnerBuffet.setOnClickListener(switchListener)
    }

    override fun initLocalProcess() {
        initSpinner()

        val x = "Update profile now..."
        val y = "Failed to update the profile..."
        mProgressInfo.progressInfoSetText(x, y)

        getUserProfileFromRemote()
    }

    override fun initOnStart() {

    }

    private fun initSpinnerList() {
        //可以被优化，后续可以从firebase读取
        mSpinnerListSquare.add(UnifiedSpinnerItem("01 to 10"))//必须小写，和数据库一致，后续可以显示，数据库分开
        mSpinnerListSquare.add(UnifiedSpinnerItem("10 to 30"))//还需要考虑系统语言变化的情况下，怎么办？需要系统资源的配合，而不全部是hard coding
        mSpinnerListSquare.add(UnifiedSpinnerItem("30 to ~~~"))
        mSpinnerListTableNum.add(UnifiedSpinnerItem("01 to 05"))//必须小写，和数据库一致，后续可以显示，数据库分开
        mSpinnerListTableNum.add(UnifiedSpinnerItem("06 to 20"))//还需要考虑系统语言变化的情况下，怎么办？需要系统资源的配合，而不全部是hard coding
        mSpinnerListTableNum.add(UnifiedSpinnerItem("21 to ~~~"))
        mSpinnerListEmployeeNum.add(UnifiedSpinnerItem("01 to 05"))//必须小写，和数据库一致，后续可以显示，数据库分开
        mSpinnerListEmployeeNum.add(UnifiedSpinnerItem("06 to 20"))//还需要考虑系统语言变化的情况下，怎么办？需要系统资源的配合，而不全部是hard coding
        mSpinnerListEmployeeNum.add(UnifiedSpinnerItem("21 to ~~~"))
    }

    private fun initSpinner() {
        initSpinnerList()
        mSpinnerSquare = UnifiedSpinnerHandle(this, mSpinnerListSquare)
        mSpinnerTableNum = UnifiedSpinnerHandle(this, mSpinnerListTableNum)
        mSpinnerEmployeeNum = UnifiedSpinnerHandle(this, mSpinnerListEmployeeNum)
        mSpinnerSquare.spinnerHandleInit(R.id.restaurant_square)
        mSpinnerTableNum.spinnerHandleInit(R.id.restaurant_table_number)
        mSpinnerEmployeeNum.spinnerHandleInit(R.id.restaurant_employee_number)
    }

    private val observerResult = object : Observer<FirebaseResult> {
        override fun onChanged(it: FirebaseResult) {
            handleOnChanged(it)
        }
    }

    private fun handleOnChanged(it: FirebaseResult) {
        updateStatus(it.mStatus)
        updateData(it)
    }

    private fun updateStatus(x: Status) {
        mProgressInfo.progressInfoSetStatus(x)
    }

    private fun updateData(it: FirebaseResult) {
        if (it.mStatus != Status.LOADING) {
            showToast("Upload food successful or failed")
            this.finish()
        }
    }

    private fun getUserProfileFromRemote() {

        //校验数据
    }

    private fun getHnadleRemoteUserProfile() {

        //校验数据

        if (true) {

            displayImageWithAddr(mRemoteImageAddr)
        }
    }

    private val updateBtnListener = object : View.OnClickListener {
        override fun onClick(v: View?) {

            showToast("Updating Clicked now ... !")

            //UploadEditViewModel check的检查，比如名字规范等等，文件名，菜名等等
            val local = UpdateLocalUserDataUnit(
                UpdateRemoteUserDataUnit(
                    getLoginUserId(),
                    "kefengxw@qq.com",
                    mEditTextNickName.text.toString().trim(),
                    mEditTextCompanyName.text.toString().trim(),
                    mEditTextRestaurantName.text.toString().trim(),
                    mEditTextRestaurantAddr.text.toString().trim(),
                    mEditTextRestaurantFloor.text.toString().trim(),
                    mEditTextCuisineName.text.toString().trim(),
                    mBreakfastCheck.text.toString().trim(),
                    mLunchCheck.text.toString().trim(),
                    mDinnerCheck.text.toString().trim(),
                    "01 to 10",
                    "01 to 05",
                    "01 to 05",
                    "",
                    ""
                ),
                mLocalImageAddr
            )

            val it = mViewModel.inputCheck(local)
            if (ReturnCode.ReturnCode_Success != it) {
                showInputCheckResult(it)
                return
            }

            if (mViewModel.taskIsOngoing()) {
                showToast("Updating Task running now!")
                return
            }

            //mViewModel.updateUser(local)
        }
    }

    private fun showInputCheckResult(it: ReturnCode) {
        val msg = when (it) {
            ReturnCode_Err_Nick_Name -> "Please input nick name!"
            ReturnCode_Err_Company_Name -> "Please input company name!"
            ReturnCode_Err_Restaurant_Name -> "Please input restaurant name!"
            ReturnCode_Err_Restaurant_Addr -> "Please input restaurant address!"
            ReturnCode_Err_Restaurant_Floor -> "Please input restaurant floor!"
            ReturnCode_Err_Cuisine_Name -> "Please input Cuisine name!"
            ReturnCode_Err_Scope -> "Please input select at least scope!"
            ReturnCode_Err_Square -> "Please input square!"
            ReturnCode_Err_Table_Number -> "Please input table numbers!"
            ReturnCode_Err_Employee_Number -> "Please input employee numbers!"
            else -> "Unexcept Error!"
        }
        showToast(msg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        openFileResultHandle(requestCode, resultCode, data)
    }

    override fun openFileResultSuccessHandle(newImageAddr: String) {
        mLocalImageAddr = newImageAddr
        mRemoteImageAddr = ""
        mRemoteImagePath = ""
    }

    override fun getBaseActivity(): BaseActivity {
        return this@EditProfileActivity
    }

    private val checkListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            val it = view as UnifiedCheckedTextView
            it.isChecked = !it.isChecked
            when (it) {
                mBreakfastCheck -> {
                    mBreakfast = setAndGetCheckResult(it, "breakfast", mBreakfastBuffet)
                }
                mLunchCheck -> {
                    mLunch = setAndGetCheckResult(it, "lunch", mLunchBuffet)
                }
                mDinnerCheck -> {
                    mDinner = setAndGetCheckResult(it, "dinner", mDinnerBuffet)
                }
            }
        }
    }

    private fun setAndGetCheckResult(check: UnifiedCheckedTextView, type: String, buffet: SwitchCompat): String {
        buffet.isEnabled = check.isChecked
        if (!check.isChecked) {
            buffet.isChecked = false
            return ""
        }
        return if (buffet.isChecked) (type + "Buffet") else type
    }

    private val switchListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            val it = view as SwitchCompat
            it.isChecked = !it.isChecked
            when (it) {
                mBreakfastBuffet -> {
                    mBreakfast = getSwitchResult(it, "breakfast")
                }
                mLunchBuffet -> {
                    mLunch = getSwitchResult(it, "lunch")
                }
                mDinnerBuffet -> {
                    mDinner = getSwitchResult(it, "dinner")
                }
            }
        }
    }

    private fun getSwitchResult(buffet: SwitchCompat, type: String): String {
        return if (buffet.isChecked) (type + "Buffet") else type
    }

    override fun spinnerResult(type: UnifiedSpinnerHandle, item: UnifiedSpinnerItem) {
        when (type) {
            mSpinnerSquare -> {
                showToast("---${item.mSpinnerItem}--- is selected")
            }
            mSpinnerTableNum -> {
                showToast("---${item.mSpinnerItem}--- is selected")
            }
            mSpinnerEmployeeNum -> {
                showToast("---${item.mSpinnerItem}--- is selected")
            }
        }
    }
}