package com.foodorder.order.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.foodorder.order.viewmodel.HomeViewModel

abstract class BaseFragment : Fragment() {
    //protected var mViewModel: ShareViewModel? = null
    protected var mBaseCtx: Context? = null
    protected var mBaseView: View? = null
    protected lateinit var mViewModel: HomeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mBaseCtx = context
        getViewModel()//improve query 2 times info from network at initial phase
    }

    private fun getViewModel() {
        //Notice, this is the shared view model, share data between these fragments,owner is getActivity()
        //Only get one/same instance of mViewModel
        mViewModel = ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)
    }

    protected abstract fun providedFragmentLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val id = providedFragmentLayoutId()

        if (mBaseView == null) {
            mBaseView = inflater.inflate(id, container, false)
        }

        if (mBaseView!!.parent != null) {
            val viewGroup = (mBaseView!!.parent) as ViewGroup
            viewGroup.removeView(mBaseView)
        }

        return mBaseView as View//just for improve the performance
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView(mBaseView!!)
    }

    private fun initView(view: View) {
        initViewComm(view)
        initViewModelObserver(mViewModel)
        initViewListener(view)
        initLocalProcess()
    }

    abstract fun initViewComm(view: View)
    abstract fun initViewModelObserver(viewModel: HomeViewModel)
    abstract fun initViewListener(view: View)
    abstract fun initLocalProcess()

    fun showToast(msg: String) {
        Toast.makeText(mBaseCtx!!, msg, Toast.LENGTH_SHORT).show()
    }

    fun <T : View> baseFragFindViewById(id: Int): T {
        return mBaseView!!.findViewById<T>(id)
    }

    protected abstract fun initOnStart()
    protected abstract fun handleOnStop()

    override fun onStart() {
        super.onStart()
        initOnStart()
    }

    override fun onStop() {
        super.onStop()
        handleOnStop()
    }
}