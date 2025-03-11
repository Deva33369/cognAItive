package visual.camp.sample.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import kotlin.math.abs
import kotlin.math.pow

class CalibrationViewer : ViewGroup {
    private lateinit var pointColors: IntArray
    private var index = 0
    private val x = -999f
    private val y = -999f
    private var calibPoint: Paint? = null
    private var toDraw = true
    private var calibrationPoint: CalibrationPoint? = null
    private val backgroundColor = Color.rgb(0x64, 0x5E, 0x5E)

    //  private int backgroundColor = Color.argb(0x88,0x64,0x5E, 0x5E);
    private val redColor = Color.rgb(0xEF, 0x53, 0x50)
    private val purpleColor = Color.rgb(0xAB, 0x47, 0xBC)
    private val orangeColor = Color.rgb(0xFF, 0xA7, 0x26)
    private val blueColor = Color.rgb(0x42, 0xA5, 0xF5)
    private val greenColor = Color.rgb(0x66, 0xBB, 0x6A)
    private val brownColor = Color.rgb(0xCA, 0x9A, 0x00)
    private val yellowColor = Color.rgb(0xFF, 0xFD, 0x00)

    private val textPaint = TextPaint()

    private var msg: String? = null

    companion object {
        private const val default_radius = 20f
        private const val default_rotate = 50f
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        textPaint.isAntiAlias = true
        textPaint.textSize = 16 * resources.displayMetrics.density
        textPaint.color = -0x1
        textPaint.textAlign = Paint.Align.CENTER

        setBackgroundColor(backgroundColor)
        pointColors = intArrayOf(
            redColor, purpleColor, orangeColor, blueColor, greenColor, brownColor, yellowColor
        )
        calibPoint = Paint()
        calibPoint!!.isAntiAlias = true
        calibPoint!!.color = pointColors[index]
        calibrationPoint = CalibrationPoint(context)
        addView(calibrationPoint)
    }

    private var offsetX = 0f
    private var offsetY = 0f
    fun setOffset(x: Int, y: Int) {
        offsetX = x.toFloat()
        offsetY = y.toFloat()
    }

    private fun drawText(canvas: Canvas) {
        val xPos = (canvas.width / 2)
        var yPos = ((canvas.height / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)).toInt()

        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.
        val r = Rect()
        textPaint.getTextBounds(msg, 0, msg!!.length, r)
        yPos -= (abs(r.height()) / 2)

        canvas.drawText(msg!!, xPos.toFloat(), yPos.toFloat(), textPaint)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (toDraw) {
            canvas.drawCircle(x, y, 10f, calibPoint!!)
        }
        if (msg != null) {
            drawText(canvas)
        }
    }

    fun setPointAnimationPower(power: Float) {
        calibrationPoint!!.setPower(power)
    }

    fun setPointPosition(x: Float, y: Float) {
        val px = x - offsetX
        val py = y - offsetY
        calibrationPoint!!.layout(
            px.toInt() - 20,
            py.toInt() - 20,
            px.toInt() + 20,
            py.toInt() + 20
        )
        invalidate()
    }

    fun changeColor() {
        index += 1
        if (index == pointColors.size) {
            index = 0
        }
        calibPoint!!.color = index
        invalidate()
    }

    fun changeDraw(isDrawPoint: Boolean, msgToShow: String?) {
        this.toDraw = isDrawPoint
        this.msg = msgToShow
        invalidate()
    }

    //  public void changeBackgroundColor(int color) {
    //    setBackgroundColor(color);
    //    invalidate();
    //  }
    //
    //  public void setDrawGazePoint(boolean toDraw) {
    //    this.toDraw = toDraw;
    //    invalidate();
    //  }
    //
    //  public void setMessage(String msg) {
    //    this.msg = msg;
    //    invalidate();
    //  }
    //  @Override
    //  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    //    super.onLayout(changed, left, top, right, bottom);
    //  }
    private inner class CalibrationPoint(context: Context?) : View(context) {
        private var animation_power = 0f
        private val point_paint = Paint()
        private var center_x = 0f
        private var center_y = 0f
        private val oval: RectF
        private var rotateAnimation: RotateAnimation? = null
        private var last_end_degree: Float = 0f
        private var next_end_degree: Float = 0f

        var listener: Animation.AnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                last_end_degree = next_end_degree
                next_end_degree += (1 - (1 - animation_power).pow(5.0f) as Float) * Companion.default_rotate
                setAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        }

        init {
            point_paint.isAntiAlias = true
            point_paint.color = pointColors[0]
            oval = RectF()
            last_end_degree = 0f
            next_end_degree = 0f
        }

        fun setPower(power: Float) {
            animation_power = power
            if (rotateAnimation == null) {
                next_end_degree += (1 - (1 - animation_power).pow(5.0f) as Float) * Companion.default_rotate
                setAnimation()
            }
            invalidate()
        }

        fun setAnimation() {
            rotateAnimation = RotateAnimation(
                last_end_degree,
                next_end_degree,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            rotateAnimation!!.setAnimationListener(listener)
            rotateAnimation!!.repeatCount = 1
            rotateAnimation!!.duration = 10
            this.startAnimation(rotateAnimation)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            if (toDraw) {
                val oval_long = Companion.default_radius * (1 + animation_power * 0.3f)
                val oval_short = Companion.default_radius * (1 - animation_power * 0.3f)

                oval.left = center_x - oval_long / 2
                oval.top = center_y - oval_short / 2
                oval.right = center_x + oval_long / 2
                oval.bottom = center_y + oval_short / 2

                canvas.drawOval(oval, point_paint)
            }
        }

        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            super.onLayout(changed, left, top, right, bottom)
            center_x = (right - left) / 2.0f
            center_y = (bottom - top) / 2.0f
        }


    }
}
