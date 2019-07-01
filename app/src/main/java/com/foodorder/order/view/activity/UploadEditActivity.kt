package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalDataConfiguration.MAX_UPLOAD_INGREDIENT
import com.foodorder.order.model.data.ReturnCode
import com.foodorder.order.model.data.ReturnCode.*
import com.foodorder.order.model.data.Status
import com.foodorder.order.model.data.Status.LOADING
import com.foodorder.order.model.firebase.*
import com.foodorder.order.view.adapter.OverviewFoodItem
import com.foodorder.order.view.componet.*
import com.foodorder.order.viewmodel.UploadEditViewModel
import kotlinx.android.synthetic.main.upload_edit_activity.*

class UploadEditActivity : BaseActivity(), ImageViewHandle, UnifiedSpinnerHandle.HandleSpinnerResult {

    private lateinit var mViewModel: UploadEditViewModel
    override lateinit var mImageView: UnifiedImageView
    lateinit var mUploadBtn: UnifiedButton
    lateinit var mCancelBtn: UnifiedButton
    lateinit var mEditTextName: UnifiedEditText
    lateinit var mEditTextDesc: UnifiedEditText
    lateinit var mEditTextPrice: UnifiedEditText
    lateinit var mScrollView: NestedScrollView
    lateinit var mPriorityPickerView: UnifiedNumberPicker
    private var mPriority: Int = 3
    private val mProgressInfo = ProgressInfo(this)
    private var mLocalImageAddr: String = ""
    private var mRemoteImageAddr: String = ""
    private var mRemoteImagePath: String = ""
    private var mUniqueId: String = ""
    lateinit var mCategorySpinner: UnifiedSpinnerHandle
    var mSpinnerList = ArrayList<UnifiedSpinnerItem>()
    private var mCategory: String = ""

    companion object {
        fun startUploadEditActivity(ctx: Context, cate: String) {
            val intent = Intent(ctx, UploadEditActivity::class.java)
            intent.putExtra("category", cate)
            ctx.startActivity(intent)
        }

        fun decodeCategoryData(it: UploadEditActivity): String {
            val x = it.intent.getStringExtra("category")//need to check null ?
            return x
        }

        fun startUploadEditActivity(ctx: Context, input: OverviewFoodItem) {
            val intent = Intent(ctx, UploadEditActivity::class.java)
            //val bundle = Bundle()
            intent.putExtra("bundleData", input)
            ctx.startActivity(intent)
        }

        fun decodeBundleData(it: UploadEditActivity): OverviewFoodItem? {
            return it.intent.getParcelableExtra<OverviewFoodItem>("bundleData")
        }
    }

    override fun initOnCreateInitialize() {

    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.upload_edit_activity
    }

    override fun initOnCreateToolbar(): Toolbar {
        return upload_edit_tool_bar as Toolbar
    }

    override fun initInjector() {

    }

    override fun initViewModel() {
        mViewModel = ViewModelProviders.of(this@UploadEditActivity).get(UploadEditViewModel::class.java)
        mViewModel.getUploadResult().observe(this, observerResult)
    }

    override fun initToolbarTitle(): String {
        return "Upload New/Edit the food"
    }

    override fun initView() {
        mUploadBtn = upload_submit
        mCancelBtn = upload_cancel
        mImageView = upload_image
        mEditTextName = name_edit_text
        mEditTextDesc = describe_edit_text
        mEditTextPrice = price_edit_text
        mScrollView = upload_scroll_layout
        mPriorityPickerView = display_priority
        //mCategorySpinner = category_edit_spinner
        mProgressInfo.progressInfoInit(R.id.upload_edit_progress_info)

        initSubView()
    }

    private fun initSubView() {
        initializeImageView()
    }

    override fun enableHomeAsUp(): Boolean {
        return true
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);不是返回按钮，而是叉的关闭按钮
    }

    override fun initListener() {
        mUploadBtn.setOnClickListener(uploadBtnListener)
        //mImageView.setOnClickListener(imageListener)

        for (i in 1..MAX_UPLOAD_INGREDIENT) {

            val itemView: View = provideIngredientView(i)
            val mIngrAddRight: UnifiedImageView = itemView.findViewById(R.id.ingredient_add_right)

            handleLeftAddBtn(itemView, mIngrAddRight)
            handleRightAddBtn(itemView, mIngrAddRight, i)
        }
    }

    private fun initSpinnerList() {
        //可以被优化，后续可以从firebase读取
        mSpinnerList.add(UnifiedSpinnerItem("starter"))//必须小写，和数据库一致，后续可以显示，数据库分开
        mSpinnerList.add(UnifiedSpinnerItem("mainCourse"))//还需要考虑系统语言变化的情况下，怎么办？需要系统资源的配合，而不全部是hard coding
        mSpinnerList.add(UnifiedSpinnerItem("dessert"))
        mSpinnerList.add(UnifiedSpinnerItem("wineDrink"))
    }

    private fun initSpinner() {
        initSpinnerList()
        val x = getSpinnerPosition(mSpinnerList, mCategory)//待完善
        mCategorySpinner = UnifiedSpinnerHandle(this, mSpinnerList, x)
        mCategorySpinner.spinnerHandleInit(R.id.category_edit_spinner)
    }

    override fun initLocalProcess() {

        val it = decodeBundleData(this@UploadEditActivity)
        if (it != null) {
            handleRemoteFoodData(it)
        } else {
            mCategory = decodeCategoryData(this@UploadEditActivity)
        }

        val x = "Upload food now..."
        val y = "Failed to upload the food..."
        mProgressInfo.progressInfoSetText(x, y)

        initNumberPicker()
        initSpinner()
    }

    private fun getSpinnerPosition(list: ArrayList<UnifiedSpinnerItem>, item: String): Int {

        var index: Int = 0
        for (it in list) {
            if (item == it.mSpinnerItem) {
                break
            }
            index++
        }
        return index
    }

    private fun handleRemoteFoodData(it: OverviewFoodItem) {
        mUniqueId = it.uniqueId
        mEditTextName.setText(it.remoteInfo.name)
        mEditTextPrice.setText(it.remoteInfo.price)
        mEditTextDesc.setText(it.remoteInfo.description)
        mCategory = it.remoteInfo.category
        mRemoteImageAddr = it.remoteImage.imageRemoteAddr
        mRemoteImagePath = it.remoteImage.imageRemotePath
        //mPriority = it.priority//后续待优化
        if (mRemoteImageAddr == "") {
            //error here
        }
        displayImageWithAddr(mRemoteImageAddr)
    }

    private fun initNumberPicker() {
        //numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);设置不可编辑
        mPriorityPickerView.minValue = 1
        mPriorityPickerView.maxValue = 10
        mPriorityPickerView.wrapSelectorWheel = false;//循环显示
        mPriorityPickerView.value = 3;//需要重新输入正确的值
    }

    override fun initOnStart() {

    }

    override fun handleOnStop() {

    }

    private val uploadBtnListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            //UploadEditViewModel check的检查，比如名字规范等等，文件名，菜名等等
            //需要增加对于mCategory的检查，放置用户删除了默认值
            val local = DataUnitFb<FoodDataUnitRemoteFb>(
                DataUnitCase(mLocalImageAddr, mRemoteImageAddr, mUniqueId, FireBaseFolder.FoodFolder),
                DataUnitRemoteFb<FoodDataUnitRemoteFb>(
                    mUniqueId,
                    FoodDataUnitRemoteFb(
                        mEditTextName.text.toString().trim(),
                        mEditTextPrice.text.toString().trim(),
                        mEditTextDesc.text.toString().trim(),
                        mCategory
                    ),
                    DataUnitRemoteImageFb(mRemoteImageAddr, mRemoteImagePath)
                )
            )

            val it = mViewModel.inputCheck(local)
            if (ReturnCode_Success != it) {
                showInputCheckResult(it)
                return
            }

            if (mViewModel.taskIsOngoing()) {
                showToast("Uploading Task running now!")
                return
            }

            mViewModel.addOrUpdateToFood(local)
        }
    }

    private fun showInputCheckResult(it: ReturnCode) {
        val msg = when (it) {
            ReturnCode_Err_Uri -> "Please select image!"
            ReturnCode_Err_Name -> "Please input name!"
            ReturnCode_Err_Price -> "Please input price!"
            ReturnCode_Err_Description -> "Please input description!"
            else -> "Unexcept Error!"
        }
        showToast(msg)
    }

    override fun getBaseActivity(): BaseActivity {
        return this@UploadEditActivity
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
        if (it.mStatus != LOADING) {
            showToast("Upload food successful or failed")
            this.finish()
        }
    }

    private fun provideIngredientView(i: Int): View {
        return when (i) {
            2 -> ingredient2
            3 -> ingredient3
            4 -> ingredient4
            5 -> ingredient5
            6 -> ingredient6
            7 -> ingredient7
            8 -> ingredient8
            else -> ingredient1
        }
    }

    private fun handleLeftAddBtn(itemView: View, ingrAddRight: UnifiedImageView) {

        val ingrAddLeft: UnifiedImageView = itemView.findViewById(R.id.ingredient_add_left)
        val ingrImageLeft: UnifiedImageView = itemView.findViewById(R.id.ingredient_image_left)
        val ingrTextLeft: UnifiedEditText = itemView.findViewById(R.id.ingredient_text_left)

        ingrAddLeft.setOnClickListener {
            ingrAddLeft.visibility = View.GONE
            ingrImageLeft.visibility = View.VISIBLE
            ingrTextLeft.visibility = View.VISIBLE

            ingrAddRight.visibility = View.VISIBLE
        }
    }

    private fun handleRightAddBtn(itemView: View, ingrAddRight: UnifiedImageView, i: Int) {

        val ingrImageRight: UnifiedImageView = itemView.findViewById(R.id.ingredient_image_right)
        val ingrTextRight: UnifiedEditText = itemView.findViewById(R.id.ingredient_text_right)

        ingrAddRight.setOnClickListener {
            ingrAddRight.visibility = View.GONE
            ingrImageRight.visibility = View.VISIBLE
            ingrTextRight.visibility = View.VISIBLE

            if (i < MAX_UPLOAD_INGREDIENT) {

                val nextView: View = provideIngredientView(i + 1)
                val ingrAddLeftNext: UnifiedImageView = nextView.findViewById(R.id.ingredient_add_left)

                nextView.visibility = View.VISIBLE
                ingrAddLeftNext.visibility = View.VISIBLE
                //mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        openFileResultHandle(requestCode, resultCode, data)
    }

    override fun openFileResultSuccessHandle(newImageAddr: String) {
        mLocalImageAddr = newImageAddr
    }

    override fun spinnerResult(type: UnifiedSpinnerHandle, item: UnifiedSpinnerItem) {

        if (mCategorySpinner == type) {
            showToast("${item.mSpinnerItem} is selected")
            mCategory = item.mSpinnerItem
        }
    }
}