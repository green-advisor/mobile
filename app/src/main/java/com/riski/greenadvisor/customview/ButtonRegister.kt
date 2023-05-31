package com.riski.greenadvisor.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.riski.greenadvisor.R

class ButtonRegister: AppCompatButton {
    private lateinit var yesBackground : Drawable
    private lateinit var noBackground : Drawable
    private lateinit var yesFill : String
    private lateinit var noFill : String
    private var colorText : Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        init()
    }

    constructor(context: Context, attr: AttributeSet, defStyleSet: Int) : super(context, attr, defStyleSet) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isEnabled) yesBackground else noBackground

        setTextColor(colorText)
        textSize = 20f
        gravity = Gravity.CENTER
        text = if (isEnabled) yesFill else noFill
    }

    private fun init() {
        colorText = ContextCompat.getColor(context, android.R.color.background_light)
        yesBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_yes) as Drawable
        noBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_no) as Drawable
        yesFill = resources.getString(R.string.register_btn)
        noFill = resources.getString(R.string.register_btn)
    }
}