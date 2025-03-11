package visual.camp.sample.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi

class EyeBlinkView : LinearLayout {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?, attrs: AttributeSet?,
        defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private val uiHandler = Handler(Looper.getMainLooper())

    private var imgRightEye: ImageView? = null
    private var imgLeftEye: ImageView? = null
    private var imgEye: ImageView? = null
    private var drawableOneEyeOpen: Drawable? = null
    private var drawableOneEyeClosed: Drawable? = null
    private var drawableEyeOpen: Drawable? = null
    private var drawableEyeClosed: Drawable? = null
    private fun init() {
        inflate(context, R.layout.view_eye_blink, this)
        imgRightEye = findViewById(R.id.img_right_eye)
        imgLeftEye = findViewById(R.id.img_left_eye)
        imgEye = findViewById(R.id.img_eye)

        drawableOneEyeOpen = context.getDrawable(R.drawable.baseline_visibility_black_48)
        drawableOneEyeClosed = context.getDrawable(R.drawable.baseline_visibility_off_black_48)
        drawableEyeOpen = context.getDrawable(R.drawable.twotone_visibility_black_48)
        drawableEyeClosed = context.getDrawable(R.drawable.twotone_visibility_off_black_48)
    }

    fun setLeftEyeBlink(isBlink: Boolean) {
        uiHandler.post {
            if (isBlink) {
                imgLeftEye!!.setImageDrawable(drawableOneEyeClosed)
            } else {
                imgLeftEye!!.setImageDrawable(drawableOneEyeOpen)
            }
        }
    }

    fun setRightEyeBlink(isBlink: Boolean) {
        uiHandler.post {
            if (isBlink) {
                imgRightEye!!.setImageDrawable(drawableOneEyeClosed)
            } else {
                imgRightEye!!.setImageDrawable(drawableOneEyeOpen)
            }
        }
    }

    fun setEyeBlink(isBlink: Boolean) {
        uiHandler.post {
            if (isBlink) {
                imgEye!!.setImageDrawable(drawableEyeClosed)
            } else {
                imgEye!!.setImageDrawable(drawableEyeOpen)
            }
        }
    }
}
