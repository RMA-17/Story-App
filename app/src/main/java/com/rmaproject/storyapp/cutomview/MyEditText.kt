package com.rmaproject.storyapp.cutomview

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rmaproject.storyapp.R

class MyEditText : TextInputLayout {

    private lateinit var editText: TextInputEditText

    constructor (context:Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        isErrorEnabled = true
        editText = TextInputEditText(context)
        hint =  context.resources.getString(R.string.hint_new_password)
        createEditText(editText)
    }

    private fun createEditText(editText: TextInputEditText) {
        val layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        editText.setPadding(16, 8, 16, 8)
        editText.hint = context.resources.getString(R.string.hint_new_password)
        editText.transformationMethod = PasswordTransformationMethod.getInstance()
        editText.layoutParams = layoutParams
        editText.addTextChangedListener { text ->
            this.error = if (text.toString().length < 8) {
                context.resources.getString(R.string.warn_txt_minimum)
            } else {
                null
            }
        }
        addView(editText)
    }
}