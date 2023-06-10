package com.riski.greenadvisor.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.riski.greenadvisor.R

class EditTextRequiredPassword: TextInputEditText, View.OnTouchListener {
    private var isFocusedAndEdited = false

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStykeAttr: Int): super(context, attrs, defStykeAttr) {
        init()
    }


    private fun init() {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length < 8) errorInput()
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.register_valid_password)
        transformationMethod = PasswordTransformationMethod.getInstance()
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun errorInput() {
        error = context.getString(R.string.register_password_notvalid)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isFocused) {
                    setBackgroundResource(R.drawable.ic_bg_email_click)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isFocusedAndEdited) {
                    setBackgroundResource(R.drawable.ic_bg_email)
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!isFocused && !isFocusedAndEdited) {
            setBackgroundResource(R.drawable.ic_bg_email)
        }
    }
}