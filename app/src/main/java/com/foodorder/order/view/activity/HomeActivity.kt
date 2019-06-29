package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.foodorder.order.R
import com.foodorder.order.di.component.HomeActivityComponent
import com.foodorder.order.di.module.HomeActivityModule
import com.foodorder.order.model.data.*
import com.foodorder.order.model.data.InternalStatusConfiguration.getLoginUserEmail
import com.foodorder.order.model.data.Phase.AUTH_LOGIN
import com.foodorder.order.model.data.Phase.FETCH_DATA
import com.foodorder.order.model.data.Status.*
import com.foodorder.order.model.repository.DisplayData
import com.foodorder.order.model.repository.DisplayItem
import com.foodorder.order.view.activity.AboutActivity.Companion.startAboutActivity
import com.foodorder.order.view.activity.InitialActivity.Companion.startInitialActivity
import com.foodorder.order.view.activity.SubmitActivity.Companion.startSubmitActivity
import com.foodorder.order.view.activity.UploadEditActivity.Companion.startUploadEditActivity
import com.foodorder.order.view.activity.UploadOverviewActivity.Companion.startUploadOverviewActivity
import com.foodorder.order.view.componet.*
import com.foodorder.order.view.componet.LoginDialog.Companion.startLoginDialog
import com.foodorder.order.view.fragment.SectionsPagerAdapter
import com.foodorder.order.viewmodel.HomeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.home_activity_main.*
import kotlinx.android.synthetic.main.load_info.*
import kotlinx.android.synthetic.main.progress_info.*
import javax.inject.Inject

class HomeActivity : BaseActivity(),
    LoginDialog.LoginDialogListener {

    private lateinit var mHomeActivityComponent: HomeActivityComponent
    private lateinit var mGlide: GlideRequests
    private lateinit var mCtx: Context
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mToolBar: Toolbar
    private lateinit var mNaviView: NavigationView

    //private val mItemList = ArrayList<ItemRecyclerDisplayData>()
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mIndexBarView: IndexBarView
    private lateinit var mIndexBarText: UnifiedTextView
    private lateinit var mPageView: ViewPager
    private lateinit var mPageViewAdapter: SectionsPagerAdapter
    private lateinit var mTabLayout: TabLayout
    private lateinit var mFabtn: FloatingActionButton
    //private lateinit var mTitle: TitleDecoration
    private lateinit var mLoginHandle: BaseLoginHandle

    companion object {
        fun startHomeActivity(ctx: Context) {
            val intent = Intent(ctx, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Inject
    fun setContext(activity: HomeActivity) {
        mCtx = activity//mCtx = this;
    }

    @Inject
    fun setGlide(glide: GlideRequests) {
        mGlide = glide
        mBaseGlide = glide
    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.home_activity
    }

    override fun initOnCreateToolbar(): Toolbar {
        return home_tool_bar as Toolbar
    }

    override fun initInjector() {
        mHomeActivityComponent = getApplicationComponent()
            .homeActivityComponent()
            .homeActivityModule(HomeActivityModule(this))
            .build()
        mHomeActivityComponent.inject(this)
    }

    override fun initViewModel() {
/*        mViewModel = ViewModelProviders.of(this@HomeActivity).get(RecyListDataViewModel::class.java)
        mViewModel.getAuthData().observe(this, observerAuthData)
        mViewModel.getFilterData().observe(this, observerFilterData)
        mViewModel.getNetworkState().observe(this, observerNetworkState)*/
        mViewModel = ViewModelProviders.of(this@HomeActivity).get(HomeViewModel::class.java)
        //mViewModel.getLoginResult().observe(this, observerLoginResult)
        mLoginHandle = HomeLoginHandle(mBaseCtx, this@HomeActivity, this@HomeActivity, mViewModel)
    }

    override fun initView() {
        buildDrawerView()
        //buildRecyclerView()
        //buildIndexBarView()
        //buildStateView()
        buildPageView()
        buildTabLayoutItem()
    }

    override fun initToolbarTitle(): String {
        return "Test1234567890"
    }

    private fun buildDrawerView() {
        mToolBar = findViewById(R.id.home_tool_bar)
        //setSupportActionBar(mToolBar)
        //mToolBar.title = "Foodorder1" //did not work

        //mDrawerLayout = findViewById(R.uniqueId.home_activity_layout)

        mDrawerLayout = home_activity_layout

        val toggle = ActionBarDrawerToggle(
            this, mDrawerLayout, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        mNaviView = home_activity_navi
        mNaviView.setNavigationItemSelectedListener(naviListener)

        mFabtn = findViewById(R.id.fab)
        mFabtn.setOnClickListener { view ->
            //            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            showToast("Float Button")
            startSubmitActivity(this)
        }
    }

    private fun buildPageView() {
        mPageView = page_view_container

//        mPageViewAdapter = SectionsPagerAdapter(supportFragmentManager, mFabtn)
//        mPageView.adapter = mPageViewAdapter

        mTabLayout = findViewById(R.id.tab_layout)

        val pageViewListener = TabLayout.TabLayoutOnPageChangeListener(mTabLayout)
        mPageView.addOnPageChangeListener(pageViewListener)

        val onTabListener = TabLayout.ViewPagerOnTabSelectedListener(mPageView)
        mTabLayout.addOnTabSelectedListener(onTabListener)
    }

    private fun buildTabLayoutItem() {

        mTabLayout.setupWithViewPager(mPageView);

/*        val welcomeTab = mTabLayout.newTab()
        val specialTab = mTabLayout.newTab()
        val conceptTab = mTabLayout.newTab()

        welcomeTab.text = "Welcome"
        specialTab.text = "Special"
        conceptTab.text = "Concept"

        mTabLayout.addTab(welcomeTab)
        mTabLayout.addTab(specialTab)
        mTabLayout.addTab(conceptTab)*/
    }

    private fun updateTabLayoutItem() {

    }

    override fun enableHomeAsUp(): Boolean {
        return false
    }

    override fun initListener() {

    }

    override fun initLocalProcess() {

    }

    override fun initOnStart() {
        mPageViewAdapter = SectionsPagerAdapter(supportFragmentManager, mFabtn)
        mPageView.adapter = mPageViewAdapter
    }

    private val naviListener = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                //startUploadEditActivity(this, )
                startUploadEditActivity(this, "dessert")
                Toast.makeText(this, "hello nav_home", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_game -> {
                Toast.makeText(this, "hello nav_gallery", Toast.LENGTH_SHORT).show()
                startInitialActivity(this)
            }
            R.id.nav_theme -> {
                Toast.makeText(this, "hello nav_theme", Toast.LENGTH_SHORT).show()
                //startSettingActivity(this)
            }
            R.id.nav_setting -> {
                startLoginDialog(supportFragmentManager, getLoginUserEmail())
            }
            R.id.nav_feedback -> {
                Toast.makeText(this, "hello UploadOverviewActivity", Toast.LENGTH_SHORT).show()
                startUploadOverviewActivity(this)
            }
            R.id.nav_about -> {
                Toast.makeText(this, "hello nav_about", Toast.LENGTH_SHORT).show()
                startAboutActivity(this)
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START)
        true
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun ApplyLogin(username: String, password: String) {
        if (!mViewModel.inputCheck(username)) {
            showToast("Wrong Username!")
            return
        }
        mViewModel.doLogin(AuthData(username, password))
    }


    /*    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.uniqueId) {
            R.uniqueId.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }*/

/*    private fun buildRecyclerView() {
        mRecyclerView = findViewById(R.uniqueId.recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(mCtx)

        mAdapter = RecyclerAdapter(mCtx, mGlide)
        mAdapter.setItemClickListener(itemListener)
        mRecyclerView.adapter = mAdapter

        //mTitle = TitleDecoration(mCtx)

        mRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        //mRecyclerView.addItemDecoration(mTitle)

        mRecyclerView.visibility = INVISIBLE//Default
    }

    private fun buildIndexBarView() {
        mIndexBarView = findViewById(R.uniqueId.index_bar)
        mIndexBarText = findViewById(R.uniqueId.index_bar_text)
        mIndexBarView.visibility = INVISIBLE//Default
        mIndexBarText.visibility = INVISIBLE//Default

        mIndexBarView.setHintText(mIndexBarText)
        mIndexBarView.setOnTouchEventListener(indexListener)
    }

    private fun buildStateView() {
        auth_text.visibility = VISIBLE  //default
        auth_fail_text.visibility = INVISIBLE

        load_text.visibility = INVISIBLE
        load_fail_text.visibility = INVISIBLE

        progress_bar.visibility = INVISIBLE
    }*/

    //search bar
/*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.uniqueId.app_bar_search)
        val searchView = searchItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(searchListener)
        //searchView.setQueryHint(DEFAULT_SEARCH_VIEW_HINT);
        //searchView.setIconifiedByDefault(false);//Icon always display
        return true
    }*/

    private val itemListener = object : RecyclerAdapter.OnItemClickListener {
        override fun onItemClick(position: Int) {
            if (position != RecyclerView.NO_POSITION) {
                //To Be extension
            }
        }
    }

    private val indexListener = object : IndexBarView.OnTouchEventListener {
        override fun onTouchListener(it: String) {
//            val position = mAdapter.getPositionByIndex(it)
//            if (position != RecyclerView.NO_POSITION) {
//                //mRecyclerView.scrollToPosition(position);
//                (mRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
//            }
        }
    }

    private val searchListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(s: String): Boolean {
            return false
        }

        override fun onQueryTextChange(s: String): Boolean {
            //Toast.makeText(mCtx, "Start to Search " + s, Toast.LENGTH_SHORT).show();
            mViewModel.setFilter(s)
            return false
        }
    }

    private val observerAuthData = Observer<Resource<String>> {
        updateAuthData(it)
    }

    private val observerFilterData = Observer<PagedList<DisplayItem>> {
        mAdapter.submitList(it)
    }

    private val observerNetworkState = Observer<NetworkState> {
        setNetworkState(it.mStatus, FETCH_DATA)
    }

    private fun setNetworkState(it: Status, ph: Phase) {
        when (ph) {
            AUTH_LOGIN -> {
                progress_text.visibility = if (it == LOADING) View.VISIBLE else INVISIBLE
                progress_fail_text.visibility = if (it == ERROR) View.VISIBLE else INVISIBLE

                //load_text.visibility = if (it == SUCCESS) View.VISIBLE else INVISIBLE
                //progress_bar.visibility = if (it == SUCCESS) View.VISIBLE else INVISIBLE
            }
            FETCH_DATA -> {
                //progress_bar.visibility = if (it == LOADING) View.VISIBLE else INVISIBLE
                load_text.visibility = if (it == LOADING) View.VISIBLE else INVISIBLE
                load_fail_text.visibility = if (it == ERROR) View.VISIBLE else INVISIBLE

                mRecyclerView.visibility = if (it == SUCCESS) View.VISIBLE else INVISIBLE
                //mIndexBarText is decided by ACTION UP DOWN or MOVE
                mIndexBarView.visibility = if (it == SUCCESS) View.VISIBLE else INVISIBLE
            }
        }
    }

    private fun updateAuthData(it: Resource<String>) {
        setNetworkState(it.mStatus, AUTH_LOGIN)
    }

    private fun updateViewData(listData: DisplayData?) {
        listData?.let {
            prepareItemListData(listData)
            //mAdapter.setData(mItemList)
            //mTitle.setData(mItemList)
        }
    }

    private fun prepareItemListData(data: DisplayData) {
/*        mItemList.clear()
        data.mItem?.let {
            for (i in it) {
                mItemList.add(ItemRecyclerDisplayData(i.mName, i.mImagesUrl))
            }
        }
        mItemList.sortBy { it.mName }//ascending order*/
    }
}