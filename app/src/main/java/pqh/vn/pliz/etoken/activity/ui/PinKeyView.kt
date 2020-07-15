package pqh.vn.pliz.etoken.activity.ui

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlinx.android.synthetic.main.pin_input_keyboard.view.*
import pqh.vn.pliz.etoken.R
import kotlin.math.roundToInt


/**
 * TODO: document your custom view class.
 */
class PinKeyView : LinearLayout, View.OnClickListener {

    private val DEFAULT_MAX_LENGHT = 0
    private val DEFAULT_CELL_PADDING = 60
    private val DEFAULT_LINE_PADDING = 10
    private val MAX_SIZE = 180
    private val MIN_SIZE = 100

    var keyDelegate: PinKeyDelegate? = null
    private var maxLenght: Int = DEFAULT_MAX_LENGHT
    private var editTextID: Int = -1
    private var _editText: EditText? = null
    private var buttonBackGround: Drawable? = null
    private var buttonColor: Int? = null
    private var deleteText: String = "Xóa"

    var editText: EditText?
        get() = _editText
        set(value) {
            _editText = value
            _editText?.text?.clear()
            configEditText()
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        inflate(context, R.layout.pin_input_keyboard, this)
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.PinKeyView, defStyle, 0
        )

        try {
            maxLenght = a.getInt(R.styleable.PinKeyView_maxLenght, DEFAULT_MAX_LENGHT)
            editTextID = a.getResourceId(R.styleable.PinKeyView_edittext, -1)
            buttonBackGround = a.getDrawable(R.styleable.PinKeyView_buttonBackGround)
            buttonColor = a.getColor(R.styleable.PinKeyView_buttonTextColor, -1)

        } finally {
            a.recycle()
        }
    }

    fun configEditText() {
        _editText?.requestFocus()
        _editText?.isFocusable = true
        _editText?.isFocusableInTouchMode = false

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (editTextID > 0) {
            _editText = (context as? Activity)?.findViewById(editTextID)
            configEditText()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        configureViews()
    }

    private fun configureViews() {
        // this.removeAllViews()

        val arrayString = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", deleteText)

        var adapter = object : RecyclerView.Adapter<PinViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder {
                val view = LayoutInflater.from(parent?.getContext())
                    .inflate(R.layout.pin_item, parent, false)
                return PinViewHolder(view)
            }

            override fun getItemCount(): Int {
                return arrayString.count()
            }

            override fun onBindViewHolder(holder: PinViewHolder, position: Int) {
                val item = arrayString[position]
                holder.textView?.text = item
                if(item.isNullOrEmpty()){
                    holder.itemView.visibility = View.INVISIBLE
                } else {
                    holder.itemView.visibility = View.VISIBLE
                }
                holder.itemView.setOnClickListener {
                    if (!item.isNullOrEmpty()) {
                        if (item.equals(deleteText)) {
                            delChar()
                        } else {
                            addChar(item)
                        }
                    }
                }

            }

        }



        var buttonHeight = height / 4 - DEFAULT_LINE_PADDING * 2
        if (buttonHeight > MAX_SIZE) {
            buttonHeight = MAX_SIZE
        } else if(buttonHeight < MIN_SIZE){
            buttonHeight = MIN_SIZE
        }

        var buttonWidth = width / 3
        if (buttonWidth > MAX_SIZE) {
            buttonWidth = MAX_SIZE
        } else if(buttonHeight < MIN_SIZE){
            buttonWidth = MIN_SIZE
        }

        val buttonSize = if (buttonHeight >= buttonWidth) buttonWidth else buttonHeight
        val itemWidth = buttonSize * 3 + DEFAULT_CELL_PADDING * 2
        val itemHeight = (buttonSize + DEFAULT_LINE_PADDING * 2)* 4
        val gridLayoutManager = object : GridLayoutManager(context, 3) {

            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                // force size of viewHolder here, this will override layout_height and layout_width from xml

                lp.height = buttonSize
                lp.width = buttonSize
                lp.setMargins(0, DEFAULT_LINE_PADDING,0,DEFAULT_LINE_PADDING)
                return true
            }


            override fun getPaddingLeft(): Int {
                return ((this@PinKeyView.width - itemWidth).toFloat() + 0.5).roundToInt() / 2
            }

            override fun getPaddingRight(): Int {
                return paddingLeft
            }

            override fun getPaddingTop(): Int {
                if((this@PinKeyView.height - itemHeight) > MIN_SIZE) {
                    return (this@PinKeyView.height - itemHeight) * 2 / 3
                } else {
                    return this@PinKeyView.height - itemHeight
                }
            }

            override fun getReverseLayout(): Boolean {
                return true
            }

            override fun canScrollVertically(): Boolean {
                return false
            }

        }
        val itemDecoration = object : ItemDecoration(){
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = 5
                outRect.left = 5
            }
        }
        recycle.setHasFixedSize(true)
        recycle.layoutManager = gridLayoutManager
        recycle.adapter = adapter
        recycle.addItemDecoration(itemDecoration)

    }

    fun dpFromPx(context: Context, px: Int): Int {
        return (px / (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()

    }

    fun pxFromDp(context: Context, dp: Int): Int {
        return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    override fun onClick(vIn: View) {
        if (vIn is TextView) {
            val text = vIn.text.toString()
            when (text) {
                deleteText -> delChar()
                else -> addChar(text)
            }
        }

    }

    private fun delChar() {
        var text = _editText?.text?.toString() ?: ""
        val lenght = text.length
        if (lenght > 0) {
            text = text.substring(0..lenght - 2)
            _editText?.setText(text)
        } else {
            keyDelegate?.onTextInputClear(_editText)
        }
    }

    private fun addChar(char: String) {

        if (_editText?.text?.length ?: 0 >= maxLenght && maxLenght > 0) {
            keyDelegate?.onTextInputComplete(_editText, _editText?.text.toString())
        } else {
            var text = _editText?.text?.toString() ?: ""
            text += char
            _editText?.setText(text)
            if (_editText?.text?.length ?: 0 >= maxLenght && maxLenght > 0) {
                keyDelegate?.onTextInputComplete(_editText, _editText?.text.toString())
            }
        }
    }


    class PinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView?

        init {
            textView = itemView.findViewById(R.id.textView)
        }
    }



    open interface PinKeyDelegate {
        fun onTextInputComplete(editText: EditText?, text: String)
        fun onTextInputClear(editText: EditText?)
    }
}
