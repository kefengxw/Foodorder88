package com.foodorder.order.view.componet

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.foodorder.order.R

class LoginDialog(email: String) : AppCompatDialogFragment() {

    private lateinit var mListener: LoginDialogListener
    private lateinit var mUsername: UnifiedEditText
    private lateinit var mPassword: UnifiedEditText
    private var mHint = ""

    companion object {
        fun startLoginDialog(sfm: FragmentManager, email: String) {
            val it = LoginDialog(email)
            it.show(sfm, "LoginDialog")
        }
    }

    init {
        mHint = email
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.login_dialog, null)

        builder.run {

            setView(view)
                .setTitle("Login")
                .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                    { /*nothing to do, just close this dialog*/ }
                }
                .setPositiveButton("SignIn") { dialogInterface: DialogInterface, i: Int ->
                    run {
                        val username = mUsername.text.toString()
                        val password = mPassword.text.toString()
                        mListener.ApplyLogin(username, password)
                        //all the logical will be handled on activity, not at Dialog, Dialog just for display&transmit data
                    }
                }
        }

        mUsername = view.findViewById(R.id.username_text)
        mPassword = view.findViewById(R.id.password_text)

        //mUsername.setText(mHint)
        mUsername.setText("kefengxw@qq.com")

        return builder.create()
    }

    override fun onAttach(ctx: Context) {
        super.onAttach(ctx)
        mListener = ctx as LoginDialogListener
    }

    interface LoginDialogListener {
        fun ApplyLogin(username: String, password: String)
    }
}