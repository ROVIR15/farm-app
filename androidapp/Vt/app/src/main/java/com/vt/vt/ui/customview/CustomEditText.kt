package com.vt.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.vt.vt.R
import com.vt.vt.utils.*

class CustomEditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var eyeButtonIcon: Drawable
    private var isVisibilityOff = true

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        eyeButtonIcon =
            ContextCompat.getDrawable(context, R.drawable.ic_round_eye_on_24) as Drawable
        setBackground(EDIT_TEXT_BLACK)
        var flag = 0
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0)
            .apply {
                try {
                    flag = getInteger(R.styleable.CustomEditText_flagType, 0)
                } finally {
                    recycle()
                }
            }
        when (flag) {
            EDIT_TEXT_NAME -> fullNameEditText()
            EDIT_TEXT_EMAIL -> emailEditText()
            EDIT_TEXT_PHONE_NUMBER -> phoneNumberEditText()
            EDIT_TEXT_SIGN_IN_PASS -> {
                passwordLoginEditText()
                setDrawables(endOfTheText = eyeButtonIcon)
                setOnTouchListener(this)
            }

            EDIT_TEXT_SIGN_UP_PASS -> {
                passwordRegisterEditText()
                setDrawables(endOfTheText = eyeButtonIcon)
                setOnTouchListener(this)
            }
        }
    }

    private fun setBackground(color: String) {
        background = when (color) {
            EDIT_TEXT_BLACK -> ContextCompat.getDrawable(context, R.drawable.bg_edt_normal)
            EDIT_TEXT_GREEN -> ContextCompat.getDrawable(context, R.drawable.bg_edt_accepted)
            EDIT_TEXT_RED -> ContextCompat.getDrawable(context, R.drawable.bg_edt_error)
            else -> null
        }
    }

    private fun setDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun fullNameEditText() {
        //val regex = "[a-zA-Z]+( +[a-zA-Z]+)*".toRegex()
        val regex2 = "^\\p{L}+(?: \\p{L}+)*\$".toRegex()

        doAfterTextChanged {
            error = when {
                text.isNullOrBlank() -> {
                    setBackground(EDIT_TEXT_RED)
                    resources.getString(R.string.empty_full_name)
                }

                !text.toString().matches(regex2) -> {
                    setBackground(EDIT_TEXT_RED)
                    resources.getString(R.string.invalid_full_name)
                }

                else -> {
                    setBackground(EDIT_TEXT_GREEN)
                    null
                }
            }
        }
    }

    private fun emailEditText() {
        doAfterTextChanged {
            error = when {
                text.isNullOrBlank() -> {
                    setBackground(EDIT_TEXT_RED)
                    resources.getString(R.string.empty_email)
                }

                !Patterns.EMAIL_ADDRESS.matcher(text!!).matches() -> {
                    setBackground(EDIT_TEXT_RED)
                    resources.getString(R.string.invalid_email)
                }

                else -> {
                    setBackground(EDIT_TEXT_GREEN)
                    null
                }
            }
        }
    }

    private fun phoneNumberEditText() {
        doAfterTextChanged {
            error = when {
                text.isNullOrBlank() -> {
                    setBackground(EDIT_TEXT_RED)
                    resources.getString(R.string.empty_pn)
                }

                text!!.length < 5 -> {
                    setBackground(EDIT_TEXT_RED)
                    resources.getString(R.string.length_number)
                }

                else -> {
                    setBackground(EDIT_TEXT_GREEN)
                    null
                }
            }
        }
    }

    private fun passwordLoginEditText() {
        doAfterTextChanged {
            when {
                text.isNullOrBlank() -> {
                    setBackground(EDIT_TEXT_RED)
                    setError(resources.getString(R.string.empty_password), null)
                }

                text!!.length < 8 -> {
                    setBackground(EDIT_TEXT_RED)
                    setError(resources.getString(R.string.length_password), null)
                }

                else -> {
                    setBackground(EDIT_TEXT_GREEN)
                    setError(null, null)
                }
            }
        }
    }

    private fun passwordRegisterEditText() {
        val regex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-,.@#$%^&*()_!+=])(?=\\S+$).{4,}$".toRegex()

        doAfterTextChanged {
            when {
                text.isNullOrBlank() -> {
                    setBackground(EDIT_TEXT_RED)
                    setError(resources.getString(R.string.empty_password), null)
                }

                !text.toString().matches(regex) -> {
                    setBackground(EDIT_TEXT_RED)
                    setError(resources.getString(R.string.rule_password), null)
                }

                text!!.length < 8 -> {
                    setBackground(EDIT_TEXT_RED)
                    setError(resources.getString(R.string.length_password), null)
                }

                else -> {
                    setBackground(EDIT_TEXT_GREEN)
                    setError(null, null)
                }
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val eyeButtonStart: Float
            val eyeButtonEnd: Float
            var isEyeButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                eyeButtonEnd = (eyeButtonIcon.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < eyeButtonEnd -> isEyeButtonClicked = true
                }
            } else {
                eyeButtonStart = (width - paddingEnd - eyeButtonIcon.intrinsicWidth).toFloat()
                when {
                    event.x > eyeButtonStart -> isEyeButtonClicked = true
                }
            }
            if (isEyeButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        if (isVisibilityOff) {
                            resources.getString(R.string.hide)
                            inputType =
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            isVisibilityOff = false
                        } else {
                            resources.getString(R.string.show)
                            inputType =
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            isVisibilityOff = true
                        }
                        setDrawables(endOfTheText = eyeButtonIcon)
                        true
                    }

                    else -> false
                }
            } else return false
        }
        return false
    }
}
