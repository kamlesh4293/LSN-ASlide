package com.app.lsquared.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.lsquared.R
import com.app.lsquared.utils.DeviceInfo
import com.app.lsquared.utils.Utility


class DialogView {


    companion object{

        var dialog: Dialog? = null
        var keyboard_isvisible = false
        var inputMethodManager: InputMethodManager? = null

        fun showNotRegisterDialog(
            ctx: Context,
            listener: NotRegisterDalogListener,
            mainLayout: ConstraintLayout
        ) {
            val metrics = DisplayMetrics()
            (ctx as Activity).getWindowManager().getDefaultDisplay().getMetrics(metrics)
            val yInches = metrics.heightPixels / metrics.ydpi
            val xInches = metrics.widthPixels / metrics.xdpi
            val diagonalInches = Math.sqrt((xInches * xInches + yInches * yInches).toDouble())
            if (dialog == null || dialog?.isShowing == false) {
                dialog = Dialog(ctx)
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog?.setCancelable(false)
                dialog?.setContentView(R.layout.dialog_not_register)
                dialog?.findViewById<TextView>(R.id.tv_dialog_deviceid)?.text = DeviceInfo.getDeviceIdFromDevice(ctx)
                dialog?.findViewById<TextView>(R.id.text_dia_desc)?.text =
                    if (diagonalInches >= 6.5) "This media player is not registered to the L Squared Hub." // 6.5inch device or bigger
                    else "This media player is not registered to the \nL Squared Hub."

                dialog?.findViewById<TextView>(R.id.tv_dialog_detail)
                    ?.setText(Utility.getDetailsText(), TextView.BufferType.SPANNABLE)
                dialog?.show()
                var checkbox = dialog?.findViewById<CheckBox>(R.id.rb_main_autoregister)
                var input_ll = dialog?.findViewById<LinearLayout>(R.id.ll_notregister_manudevice)
                var input_et = dialog?.findViewById<EditText>(R.id.et_dialog_deviceid)
                var register_bt = dialog?.findViewById<TextView>(R.id.tv_notregisterdialog_register)
                var error_tv = dialog?.findViewById<TextView>(R.id.tv_notregisterdialog_error)


//                input_et?.setOnKeyListener { arg0, keyCode, event ->
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        dialog?.dismiss()
//                        (ctx as Activity).finish()
//                    }
//                    false
//                }


                inputMethodManager = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?


                register_bt?.setOnClickListener{
                    if(!input_et?.text.toString().equals("")){
                        listener.clickOnRegister(input_et?.text.toString())
                        if (inputMethodManager != null) inputMethodManager?.toggleSoftInput(
                            InputMethodManager.RESULT_HIDDEN, 0
                        )
                    }
                }

                mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(OnGlobalLayoutListener {
                    val r = Rect()
                    mainLayout.getWindowVisibleDisplayFrame(r)
                    val screenHeight: Int = mainLayout.getRootView().getHeight()
                    val keypadHeight: Int = screenHeight - r.bottom
                    keyboard_isvisible = keypadHeight > screenHeight * 0.15
                })

                checkbox?.setOnClickListener {
                    error_tv?.setText("")
                    error_tv?.visibility = View.GONE
                    if (checkbox.isChecked){
                        input_et?.setSelection(input_et?.text.toString().length)
                        if (inputMethodManager != null) inputMethodManager?.toggleSoftInput(
                            InputMethodManager.SHOW_FORCED, 0
                        )
                        input_ll?.visibility = View.VISIBLE
                        input_et?.setText("")
                        input_et?.requestFocus()
                    } else {
                        input_ll?.visibility = View.GONE
                        hideKeyBoardShowing()
                    }
                }
//                doSomething(input_et!!,ctx)
            }
        }

        fun hideKeyBoardShowing(){
            if (inputMethodManager != null && keyboard_isvisible) {
                inputMethodManager?.toggleSoftInput(
                    InputMethodManager.RESULT_HIDDEN, 0
                )
            }
        }


//        private fun doSomething(search: EditText,ctx: Context){
//            search.setOnEditorActionListener(TextView.OnEditorActionListener{ _, actionId, _ ->
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                     Do something of your interest.f
//                     We in this examples created the following Toasts
//                    Toast.makeText(ctx, "done press", Toast.LENGTH_SHORT).show()
//                    if(search.text.toString() == "geeksforgeeks"){
//                        Toast.makeText(ctx, "Welcome to GFG", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(ctx, "Invalid Input", Toast.LENGTH_SHORT).show()
//                    }
//                    return@OnEditorActionListener true
//                }
//                false
//            })
//        }

        fun hideDialog() {
            if (dialog != null && dialog?.isShowing == true) {
                dialog?.dismiss()
                dialog = null
            }
        }
        fun showError(error_msg: String) {
            if (dialog != null && dialog?.isShowing == true) {
                var error_tv = dialog?.findViewById<TextView>(R.id.tv_notregisterdialog_error)
                error_tv?.text = error_msg
                error_tv?.visibility = View.VISIBLE

            }
        }

    }
}

interface NotRegisterDalogListener{

    fun clickOnRegister(device_id: String)
}
