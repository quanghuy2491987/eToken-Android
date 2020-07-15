//package pqh.vn.pliz.etoken.activity.ui
//
//import android.app.Activity
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.graphics.drawable.Drawable
//import android.text.TextPaint
//import android.util.AttributeSet
//import android.view.KeyEvent
//import android.view.View
//import android.widget.EditText
//import android.widget.LinearLayout
//import android.widget.TextView
//import kotlinx.android.synthetic.main.pin_input_keyboard.view.*
//import pqh.vn.pliz.etoken.R
//
///**
// * TODO: document your custom view class.
// */
//class PinKeyView_bk : LinearLayout, View.OnClickListener {
//
//    private val DEFAULT_MAX_LENGHT = 0
//    var keyDelegate: PinKeyDelegate? = null
//    private var maxLenght: Int = DEFAULT_MAX_LENGHT
//    private var editTextID: Int = -1
//    private var _editText: EditText? = null
//
//    var editText: EditText?
//        get() = _editText
//        set(value) {
//            _editText = value
//            _editText?.text?.clear()
//            configEditText()
//        }
//
//    constructor(context: Context) : super(context) {
//        init(null, 0)
//    }
//
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        init(attrs, 0)
//    }
//
//    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
//        context,
//        attrs,
//        defStyle
//    ) {
//        init(attrs, defStyle)
//    }
//
//    private fun init(attrs: AttributeSet?, defStyle: Int) {
//        // Load attributes
//        inflate(context, R.layout.pin_input_keyboard, this)
//        val a = context.obtainStyledAttributes(
//            attrs, R.styleable.PinKeyView, defStyle, 0
//        )
//
//        maxLenght = a.getInt(R.styleable.PinKeyView_maxLenght, 0)
//
//        editTextID = a.getResourceId(R.styleable.PinKeyView_edittext, -1)
//
//        a.recycle()
//        configureViews()
//    }
//
//    fun configEditText() {
//        _editText?.isFocusable = false
//
//    }
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        if (editTextID > 0) {
//            _editText = (context as? Activity)?.findViewById(editTextID)
//            configEditText()
//        }
//    }
//
//    private fun configureViews() {
//
//        this.one_button.setOnClickListener(this)
//        this.two_button.setOnClickListener(this)
//        this.three_button.setOnClickListener(this)
//        this.four_button.setOnClickListener(this)
//        this.five_button.setOnClickListener(this)
//        this.six_button.setOnClickListener(this)
//        this.seven_button.setOnClickListener(this)
//        this.eight_button.setOnClickListener(this)
//        this.nine_button.setOnClickListener(this)
//        this.zero_button.setOnClickListener(this)
//        this.delete_button.setOnClickListener(this)
//    }
//
//    override fun onClick(vIn: View) {
//        when (vIn.getId()) {
//            R.id.delete_button -> {
//                delChar()
//            }
//            else -> {
//                if (vIn is TextView) {
//                    addChar(vIn.text.toString())
//                }
//            }
//
//        }
//    }
//
//    private fun delChar() {
//        var text = _editText?.text?.toString() ?: ""
//        val lenght = text.length
//        if (lenght > 0) {
//            text = text.substring(0..lenght - 2)
//            _editText?.setText(text)
//        } else {
//            keyDelegate?.onTextInputClear(_editText)
//        }
//    }
//
//    private fun addChar(char: String) {
//
//        if (_editText?.text?.length ?: 0 >= maxLenght && maxLenght > 0) {
//            keyDelegate?.onTextInputComplete(_editText, _editText?.text.toString())
//        } else {
//            var text = _editText?.text?.toString() ?: ""
//            text += char
//            _editText?.setText(text)
//            if (_editText?.text?.length ?: 0 >= maxLenght && maxLenght > 0) {
//                keyDelegate?.onTextInputComplete(_editText, _editText?.text.toString())
//            }
//        }
//    }
//
//    open interface PinKeyDelegate {
//        fun onTextInputComplete(editText: EditText?, text: String)
//        fun onTextInputClear(editText: EditText?)
//    }
//}
