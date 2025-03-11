package visual.camp.sample.view

import android.animation.FloatEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class GazePathView : View {
    private val DEFAULT_COLOR = Color.argb(0x74, 0x34, 0x34, 0xff)

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


    private var pointPaint: Paint? = null
    private var linePaint: Paint? = null

    private fun init() {
        fixationHistory = ArrayList()

        pointPaint = Paint()
        pointPaint!!.color = DEFAULT_COLOR

        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint!!.color = DEFAULT_COLOR
        linePaint!!.strokeWidth = SACCADE_LINE_WIDTH

        curPointSize = MIN_POINT_RADIUS

        evaluator = FloatEvaluator()
    }

    private var offsetX = 0f
    private var offsetY = 0f

    private var fixationHistory: MutableList<PointF>? = null
    private var fixationDrawPoint = PointF()
    private var fixationAnchorPoint: PointF? = PointF()
    private var saccadeRootPoint = PointF()
    private var saccadeTarget: PointF? = null

    private var wasFixation = false
    private var curPointSize = 0f
    private var firstFixationTime: Long = 0
    private var lastSaccadeUpdateTime: Long = 0

    private var evaluator: FloatEvaluator? = null

    fun setOffset(x: Int, y: Int) {
        offsetX = x.toFloat()
        offsetY = y.toFloat()
    }

    fun onGaze(x: Float, y: Float, is_fixation: Boolean) {
        val curTime = System.currentTimeMillis()
        val curPoint = PointF(x - offsetX, y - offsetY)
        if (!wasFixation || !is_fixation) {
            processSaccade(curTime, curPoint)
        } else {
            processFixation(curTime, curPoint)
        }
        wasFixation = is_fixation
        invalidate()
    }

    private fun processFixation(timestamp: Long, curPoint: PointF) {
        clearSaccade()
        updateAnchorPointIfNeeded(timestamp, curPoint)
        fixationHistory!!.add(curPoint)
        fixationDrawPoint = getWeightedAverage(fixationHistory!!)
        curPointSize = calculatePointSize(timestamp)

        if (didFixationMoved(
                fixationAnchorPoint!!,
                curPoint,
                calculateFixationThreshold(curPointSize)
            )
        ) {
            processSaccade(timestamp, curPoint)
        }
    }

    private fun processSaccade(timeStamp: Long, curPoint: PointF) {
        clearFixation()
        if (needUpdateSaccade(timeStamp)) {
            updateSaccadeRoot(timeStamp)
        }
        saccadeTarget = curPoint
        curPointSize = calculatePointSize(timeStamp)
    }

    private fun updateAnchorPointIfNeeded(curTime: Long, curPoint: PointF) {
        if (fixationAnchorPoint == null) {
            firstFixationTime = curTime
            fixationAnchorPoint = curPoint
        }
    }

    private fun getWeightedAverage(points: List<PointF>): PointF {
        val center = PointF(0f, 0f)
        var count = 0f
        for (i in points.indices) {
            center.x += points[i].x * (points.size - i)
            center.y += points[i].y * (points.size - i)
            count += (points.size - i).toFloat()
        }
        center.x /= count
        center.y /= count
        return center
    }

    private fun calculateFixationThreshold(pointSize: Float): Float {
        return max(
            (pointSize * 1.5f).toDouble(),
            MIN_FIXATION_POSITION_THRESHOLD.toDouble()
        ).toFloat()
    }

    private fun didFixationMoved(
        anchorPoint: PointF,
        curPoint: PointF,
        fixationThreshold: Float
    ): Boolean {
        val diff_squre: Float =
            (anchorPoint.x - curPoint.x).pow(2.0f) + (anchorPoint.y - curPoint.y).pow(2.0f)
        return diff_squre > fixationThreshold * fixationThreshold
    }

    private fun calculatePointSize(timeStamp: Long): Float {
        if (firstFixationTime == 0L) {
            return MIN_POINT_RADIUS
        } else {
            val timeDiff = timeStamp - firstFixationTime
            val size = evaluator!!.evaluate(
                timeDiff.toFloat() / MAX_FIXATION_SIZE_TIME,
                MIN_POINT_RADIUS,
                MAX_POINT_RADIUS
            )
            return min(
                size.toDouble(),
                MAX_POINT_RADIUS.toDouble()
            ).toFloat()
        }
    }

    private fun clearSaccade() {
        lastSaccadeUpdateTime = 0
        saccadeTarget = null
    }

    private fun clearFixation() {
        fixationHistory!!.clear()
        fixationAnchorPoint = null
        firstFixationTime = 0
    }

    private fun needUpdateSaccade(timeStamp: Long): Boolean {
        return timeStamp - lastSaccadeUpdateTime > SACCADE_POINT_REFRESH_TIME_MILLIS
    }

    private fun updateSaccadeRoot(timeStamp: Long) {
        saccadeRootPoint = saccadeTarget ?: fixationDrawPoint
        lastSaccadeUpdateTime = timeStamp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (saccadeTarget != null) {
            drawSaccade(canvas)
        } else {
            drawFixation(canvas)
        }
    }

    private fun drawSaccade(canvas: Canvas) {
        canvas.drawCircle(saccadeRootPoint.x, saccadeRootPoint.y, curPointSize, pointPaint!!)
        canvas.drawCircle(saccadeTarget!!.x, saccadeTarget!!.y, curPointSize, pointPaint!!)
        canvas.drawLine(
            saccadeRootPoint.x, saccadeRootPoint.y, saccadeTarget!!.x, saccadeTarget!!.y,
            linePaint!!
        )
    }

    private fun drawFixation(canvas: Canvas) {
        canvas.drawCircle(fixationDrawPoint.x, fixationDrawPoint.y, curPointSize, pointPaint!!)
    }

    companion object {
        private const val MIN_POINT_RADIUS = 10f
        private const val MAX_POINT_RADIUS = 80f
        private const val MAX_FIXATION_SIZE_TIME: Long = 1500
        private const val SACCADE_LINE_WIDTH = 2f
        private const val SACCADE_POINT_REFRESH_TIME_MILLIS: Long = 350
        private const val MIN_FIXATION_POSITION_THRESHOLD = 80f
    }
}
