package visual.camp.sample.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

class PointView : View {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private val defaultColor = Color.rgb(0x00, 0x00, 0xff)
    private val outOfScreenColor = Color.rgb(0xff, 0x00, 0x00)
    private val type = TYPE_DEFAULT
    private var paint: Paint? = null
    private var showLine = true
    private fun init() {
        paint = Paint()
        paint!!.color = defaultColor
        paint!!.strokeWidth = 2f
    }

    private var offsetX = 0f
    private var offsetY = 0f
    private val position = PointF()
    fun setOffset(x: Int, y: Int) {
        offsetX = x.toFloat()
        offsetY = y.toFloat()
    }

    fun setPosition(x: Float, y: Float) {
        position.x = x - offsetX
        position.y = y - offsetY
        invalidate()
    }

    fun hideLine() {
        showLine = false
    }

    fun showLine() {
        showLine = true
    }

    fun setType(type: Int) {
        paint!!.color =
            if (type == TYPE_DEFAULT) defaultColor else outOfScreenColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(position.x, position.y, 10f, paint!!)
        if (showLine) {
            canvas.drawLine(0f, position.y, width.toFloat(), position.y, paint!!)
            canvas.drawLine(position.x, 0f, position.x, height.toFloat(), paint!!)
        }
    }

    companion object {
        const val TYPE_DEFAULT: Int = 0
        const val TYPE_OUT_OF_SCREEN: Int = 1
    }
}
