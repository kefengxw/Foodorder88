package com.foodorder.order.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    //protected var mViewModel: ShareViewModel? = null
    protected var mCtx: Context? = null
    protected var mBaseView: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mCtx = context
        getViewModel()//improve query 2 times info from network at initial phase
    }

    private fun getViewModel() {
        //Notice, this is the shared view model, share data between these fragments,owner is getActivity()
        //Only get one/same instance of mViewModel
        //mViewModel = ViewModelProviders.of(activity!!).get(ShareViewModel::class.java)
    }

    protected abstract fun ProvidedFragmentLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val id = ProvidedFragmentLayoutId()

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
        initObserve()
    }

    private fun initView(view: View) {
        initViewComm(view)
        //init Data, init Listener if need
        initViewListener(view)
    }

    protected open fun initViewComm(view: View) {}

    protected open fun initViewListener(view: View) {}

    protected open fun initObserve() {}

    fun showToast(msg: String) {
        Toast.makeText(mCtx!!, msg, Toast.LENGTH_SHORT).show()
    }
}