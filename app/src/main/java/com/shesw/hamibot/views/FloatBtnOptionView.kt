package com.shesw.hamibot.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.hamibot.hamibot.R
import com.hamibot.hamibot.ui.floating.FloatyWindowManger

class FloatBtnOptionView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    companion object {
        fun createWindowLayoutParams(): WindowManager.LayoutParams? {
            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                FloatyWindowManger.getWindowType(), 520, -3)
            layoutParams.gravity = Gravity.LEFT or Gravity.TOP
            return layoutParams
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.float_btn_optn_view, this, true)
    }

}