package com.foodorder.order.view.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.foodorder.order.R
import com.foodorder.order.view.componet.UnifiedEditText

class ItemDetailDialog() : AppCompatDialogFragment() {

    private lateinit var mListener: LoginDialogListener
    private lateinit var mUsername: UnifiedEditText
    private lateinit var mPassword: UnifiedEditText

    companion object {
        fun startItemDetailDialog(sfm: FragmentManager) {
            val it = ItemDetailDialog()
            it.show(sfm, "ItemDetailDialog")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.item_detail_dialog, null)

        builder.run {

            setView(view)
                .setTitle("Detail information")
                .setPositiveButton("Back") { dialogInterface: DialogInterface, i: Int ->
                    run {
                        //                        val username = mUsername.text.toString()
//                        val password = mPassword.text.toString()
                        //mListener.ApplyLogin(username, password)
                        //all the logical will be handled on activity, not at Dialog, Dialog just for display&transmit data
                    }
                }
        }

        //mUsername = view.findViewById(R.uniqueId.username_text)
        //mPassword = view.findViewById(R.uniqueId.password_text)

        return builder.create()
    }

    override fun onAttach(ctx: Context) {
        super.onAttach(ctx)
        //mListener = ctx as LoginDialogListener
    }

    interface LoginDialogListener {
        fun ApplyLogin(username: String, password: String);
    }
}