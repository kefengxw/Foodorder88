package com.foodorder.order.view.componet

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager

class LogoutDialog : AppCompatDialogFragment() {

    private lateinit var mListener: LogoutDialogListener

    companion object {
        fun startLogoutDialog(sfm: FragmentManager) {
            val it = LogoutDialog()
            it.show(sfm, "LogoutDialog")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        builder.run {

            setTitle("Logout")
                .setMessage("Are you sure to logout?")
                .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                    { /*nothing to do, just close this dialog*/ }
                }
                .setPositiveButton("Logout") { dialogInterface: DialogInterface, i: Int ->
                    run {
                        mListener.ApplyLogout()
                        //all the logical will be handled on activity, not at Dialog, Dialog just for display&transmit data
                    }
                }
        }

        return builder.create()
    }

    override fun onAttach(ctx: Context) {
        super.onAttach(ctx)
        mListener = ctx as LogoutDialogListener
    }

    interface LogoutDialogListener {
        fun ApplyLogout()
    }
}