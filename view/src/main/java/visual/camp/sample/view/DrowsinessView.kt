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

class DrowsinessView : LinearLayout {
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

    private var imgAttention: ImageView? = null
    private var drawableDrowsinessOn: Drawable? = null
    private var drawableDrowsinessOff: Drawable? = null
    private fun init() {
        inflate(context, R.layout.view_drowsiness, this)
        imgAttention = findViewById(R.id.img_drowsiness)

        drawableDrowsinessOn = context.getDrawable(R.drawable.drowsiness_on_48)
        drawableDrowsinessOff = context.getDrawable(R.drawable.drowsiness_off_48)
    }

    fun setDrowsiness(drowsiness: Boolean) {
        uiHandler.post {
            if (drowsiness) {
                imgAttention!!.setImageDrawable(drawableDrowsinessOn)
            } else {
                imgAttention!!.setImageDrawable(drawableDrowsinessOff)
            }
        }
    }
}
