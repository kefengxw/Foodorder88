package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalStatusConfiguration.getLoginUserId
import com.foodorder.order.model.data.ReturnCode
import com.foodorder.order.model.data.ReturnCode.*
import com.foodorder.order.model.data.Status
import com.foodorder.order.model.firebase.*
import com.foodorder.order.view.componet.*
import com.foodorder.order.viewmodel.EditProfileViewModel
import kotlinx.android.synthetic.main.edit_profile_activity.*

class EditProfileActivity : BaseActivity(), ImageViewHandle, UnifiedSpinnerHandle.HandleSpinnerResult {

    private lateinit var mViewModel: EditProfileViewModel

    override lateinit var mImageView: UnifiedImageView
    private lateinit var mUpdateBtn: UnifiedButton
    private lateinit var mUseAccount: UnifiedTextView
    private lateinit var mUseName: UnifiedTextView
    private lateinit var mDeleteAccountBtn: UnifiedButton
    private lateinit var mEditTextNickName: UnifiedEditText
    private lateinit var mEditTextCompanyName: UnifiedEditText
    private lateinit var mEditTextRestaurantName: UnifiedEditText
    private lateinit var mEditTextRestaurantAddr: UnifiedEditText
    private lateinit var mEditTextRestaurantFloor: UnifiedEditText
    private lateinit var mEditTextCuisineName: UnifiedEditText
    private val mProgressInfo = ProgressInfo(this)

    private lateinit var mSpinnerSquare: UnifiedSpinnerHandle
    private lateinit var mSpinnerTableNum: UnifiedSpinnerHandle
    private lateinit var mSpinnerEmployeeNum: UnifiedSpinnerHandle
    private var mSpinnerListSquare = ArrayList<UnifiedSpinnerItem>()
    private var mSpinnerListTableNum = ArrayList<UnifiedSpinnerItem>()
    private var mSpinnerListEmployeeNum = ArrayList<UnifiedSpinnerItem>()

    private lateinit var mBreakfastCheck: UnifiedCheckedTextView
    private lateinit var mLunchCheck: UnifiedCheckedTextView
    private lateinit var mDinnerCheck: UnifiedCheckedTextView
    private lateinit var mBreakfastBuffet: SwitchCompat
    private lateinit var mLunchBuffet: SwitchCompat
    private lateinit var mDinnerBuffet: SwitchCompat
    private var mBreakfast: String = ""
    private var mLunch: String = ""
    private var mDinner: String = ""

    private var mSquare: String = "01 ~ 10"
    private var mTableNum: String = "01 ~ 05"
    private var mEmployeeNum: String = "01 ~ 05"

    private var mLocalImageAddr: String = ""
    private var mRemoteImageAddr: String = ""
    private var mRemoteImagePath: String = ""

    private lateinit var mRemoteData: GetUserDataUnitRemoteFb

    companion object {
        fun startEditProfileActivity(ctx: Context) {
            val intent = Intent(ctx, EditProfileActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun initOnCreateInitialize() {

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
        mViewModel.getQueryUserResult().observe(this, observerResult)
    }

    override fun initView() {
        mUpdateBtn = update_button
        mUseAccount = user_account
        mUseName = user_name
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

        mDeleteAccountBtn = user_account_delete

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
        mDeleteAccountBtn.setOnClickListener(deleteBtnListener)

        mBreakfastCheck.setOnClickListener(checkListener)
        mLunchCheck.setOnClickListener(checkListener)
        mDinnerCheck.setOnClickListener(checkListener)

        mBreakfastBuffet.setOnCheckedChangeListener(switchListenerChange)
        mLunchBuffet.setOnCheckedChangeListener(switchListenerChange)
        mDinnerBuffet.setOnCheckedChangeListener(switchListenerChange)
    }

    override fun initLocalProcess() {

        val x = "Update profile now..."
        val y = "Failed to update the profile..."
        mProgressInfo.progressInfoSetText(x, y)

        initSpinner()

        getUserProfileInfo()
    }

    override fun initOnStart() {

    }

    override fun handleOnStop() {

    }

    private fun getUserProfileInfo() {
        mViewModel.queryUserProfile()
    }

    private fun initSpinnerList() {
        //可以被优化，后续可以从firebase读取
        mSpinnerListSquare.add(UnifiedSpinnerItem("01 ~ 10"))//必须小写，和数据库一致，后续可以显示，数据库分开
        mSpinnerListSquare.add(UnifiedSpinnerItem("10 ~ 30"))//还需要考虑系统语言变化的情况下，怎么办？需要系统资源的配合，而不全部是hard coding
        mSpinnerListSquare.add(UnifiedSpinnerItem("30 ~ "))
        mSpinnerListTableNum.add(UnifiedSpinnerItem("01 ~ 05"))//必须小写，和数据库一致，后续可以显示，数据库分开
        mSpinnerListTableNum.add(UnifiedSpinnerItem("06 ~ 20"))//还需要考虑系统语言变化的情况下，怎么办？需要系统资源的配合，而不全部是hard coding
        mSpinnerListTableNum.add(UnifiedSpinnerItem("21 ~"))
        mSpinnerListEmployeeNum.add(UnifiedSpinnerItem("01 ~ 05"))//必须小写，和数据库一致，后续可以显示，数据库分开
        mSpinnerListEmployeeNum.add(UnifiedSpinnerItem("06 ~ 20"))//还需要考虑系统语言变化的情况下，怎么办？需要系统资源的配合，而不全部是hard coding
        mSpinnerListEmployeeNum.add(UnifiedSpinnerItem("21 ~"))
    }

    private fun initSpinner() {
        //must done after mSquare,mTableNum,mEmployeeNum has the value
        initSpinnerList()
        val x = getSpinnerPosition(mSpinnerListSquare, mSquare)
        val y = getSpinnerPosition(mSpinnerListTableNum, mTableNum)
        val z = getSpinnerPosition(mSpinnerListEmployeeNum, mEmployeeNum)
        mSpinnerSquare = UnifiedSpinnerHandle(this, mSpinnerListSquare, x)
        mSpinnerTableNum = UnifiedSpinnerHandle(this, mSpinnerListTableNum, y)
        mSpinnerEmployeeNum = UnifiedSpinnerHandle(this, mSpinnerListEmployeeNum, z)
        mSpinnerSquare.spinnerHandleInit(R.id.restaurant_square)
        mSpinnerTableNum.spinnerHandleInit(R.id.restaurant_table_number)
        mSpinnerEmployeeNum.spinnerHandleInit(R.id.restaurant_employee_number)
    }

    private fun getSpinnerPosition(list: ArrayList<UnifiedSpinnerItem>, item: String): Int {

        var index: Int = 0
        for (it in list) {
            if (item == it.mSpinnerItem) {
                break
            }
            index++
        }
        if (index == list.size) {
            index = 0//first one is the default value
        }
        return index
    }

    private val observerResult = object : Observer<FirebaseUserResult> {
        override fun onChanged(it: FirebaseUserResult) {
            handleOnChanged(it)
        }
    }

    private fun handleOnChanged(it: FirebaseUserResult) {
        updateStatus(it.mStatus)
        updateData(it)
    }

    private fun updateStatus(x: Status) {
        mProgressInfo.progressInfoSetStatus(x)
    }

    private fun updateData(it: FirebaseUserResult) {
        if (it.mStatus != Status.LOADING) {//Success&failed
            showToast("Upload food successful or failed")
        }

        if (it.mStatus == Status.SUCCESS) {
            mRemoteData = it.mUser!!
            handleRemoteUserProfile()
        }
    }

    private fun getUserProfileFromRemote(): Boolean {

        //mRemoteData =
        //校验数据 addSnapshotListener documentSnapshot.exists()
        //noteRef.set(note, SetOptions.merge());
        //Map<String, Object> note = new HashMap<>();
        //note.put(KEY_DESCRIPTION, description);
        //noteRef.set(note, SetOptions.merge());
        //noteRef.update(KEY_DESCRIPTION, description);
        return true
    }

    private fun handleRemoteUserProfile() {
        //校验数据和修复
        mEditTextNickName.setText(mRemoteData.remoteInfo.nickName)
        mEditTextCompanyName.setText(mRemoteData.remoteInfo.companyName)
        mEditTextRestaurantName.setText(mRemoteData.remoteInfo.restaurantName)
        mEditTextRestaurantAddr.setText(mRemoteData.remoteInfo.restaurantAddr)
        mEditTextRestaurantFloor.setText(mRemoteData.remoteInfo.restaurantFloor)
        mEditTextCuisineName.setText(mRemoteData.remoteInfo.cuisineName)

        mBreakfast = handleRemoteCheck(mRemoteData.remoteInfo.breakfast, mBreakfastCheck, mBreakfastBuffet)
        mLunch = handleRemoteCheck(mRemoteData.remoteInfo.lunch, mLunchCheck, mLunchBuffet)
        mDinner = handleRemoteCheck(mRemoteData.remoteInfo.dinner, mDinnerCheck, mDinnerBuffet)

        mSquare = mRemoteData.remoteInfo.square
        mTableNum = mRemoteData.remoteInfo.tableNumber
        mEmployeeNum = mRemoteData.remoteInfo.employeeNumber

        val x = getSpinnerPosition(mSpinnerListSquare, mSquare)
        val y = getSpinnerPosition(mSpinnerListTableNum, mTableNum)
        val z = getSpinnerPosition(mSpinnerListEmployeeNum, mEmployeeNum)

        mSpinnerSquare.updateTheSpinnerPosition(x)
        mSpinnerTableNum.updateTheSpinnerPosition(y)
        mSpinnerEmployeeNum.updateTheSpinnerPosition(z)

        mRemoteImageAddr = mRemoteData.remoteImage.imageRemoteAddr
        mRemoteImagePath = mRemoteData.remoteImage.imageRemotePath

        if (mRemoteImageAddr != "") {
            displayImageWithAddr(mRemoteImageAddr)
        }
    }

    private fun handleRemoteCheck(type: String, check: UnifiedCheckedTextView, buffet: SwitchCompat): String {

        if (type == "") {
            check.isChecked = false
            buffet.isEnabled = check.isChecked
        } else {
            check.isChecked = true
            buffet.isEnabled = check.isChecked
            buffet.isChecked = type.contains("Buffet")
        }

        return type
    }

    private val updateBtnListener = object : View.OnClickListener {
        override fun onClick(v: View?) {

            showToast("Updating Clicked now ... !")

            //UploadEditViewModel check的检查，比如名字规范等等，文件名，菜名等等
            val local = DataUnitFb<UserDataUnitRemoteFb>(
                DataUnitCase(
                    mLocalImageAddr,
                    mRemoteImageAddr,
                    getLoginUserId(),
                    FireBaseFolder.UserFolder
                ),
                DataUnitRemoteFb<UserDataUnitRemoteFb>(
                    getLoginUserId(),
                    UserDataUnitRemoteFb(
                        "kefengxw@qq.com",
                        mEditTextNickName.text.toString().trim(),
                        mEditTextCompanyName.text.toString().trim(),
                        mEditTextRestaurantName.text.toString().trim(),
                        mEditTextRestaurantAddr.text.toString().trim(),
                        mEditTextRestaurantFloor.text.toString().trim(),
                        mEditTextCuisineName.text.toString().trim(),
                        mBreakfast,
                        mLunch,
                        mDinner,
                        mSquare,
                        mTableNum,
                        mEmployeeNum
                    ),
                    DataUnitRemoteImageFb(
                        mRemoteImageAddr,
                        mRemoteImagePath
                    )
                )
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

            mViewModel.updateToUser(local)
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

    private val switchListenerChange = object : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            val it = buttonView as SwitchCompat
            it.isChecked = isChecked
            when (it) {
                mBreakfastBuffet -> {
                    mBreakfast = getSwitchResult(it, "breakfast")
                    showToast("${mBreakfast} is selected")
                }
                mLunchBuffet -> {
                    mLunch = getSwitchResult(it, "lunch")
                    showToast("${mLunch} is selected")
                }
                mDinnerBuffet -> {
                    mDinner = getSwitchResult(it, "dinner")
                    showToast("${mDinner} is selected")
                }
            }
        }
    }

    private fun getSwitchResult(buffet: SwitchCompat, type: String): String {
        return if (buffet.isChecked) (type + "Buffet") else type
    }

    private val deleteBtnListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            showToast("delete Btn Listener started!")
        }
    }

    override fun spinnerResult(type: UnifiedSpinnerHandle, item: UnifiedSpinnerItem) {
        when (type) {
            mSpinnerSquare -> {
                mSquare = item.mSpinnerItem
            }
            mSpinnerTableNum -> {
                mTableNum = item.mSpinnerItem
            }
            mSpinnerEmployeeNum -> {
                mEmployeeNum = item.mSpinnerItem
            }
        }
    }
}