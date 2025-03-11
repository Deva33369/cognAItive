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

class AttentionView : LinearLayout {
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
    private val threshold = 0.75f
    private var imgAttention: ImageView? = null
    private var drawableAttentionOn: Drawable? = null
    private var drawableAttentionOff: Drawable? = null
    private fun init() {
        inflate(context, R.layout.view_attention, this)
        imgAttention = findViewById(R.id.img_attention)

        drawableAttentionOn = context.getDrawable(R.drawable.attention_on_48)
        drawableAttentionOff = context.getDrawable(R.drawable.attention_off_48)
    }

    fun setAttention(attention: Float) {
        uiHandler.post {
            if (attention >= threshold) {
                imgAttention!!.setImageDrawable(drawableAttentionOn)
            } else {
                imgAttention!!.setImageDrawable(drawableAttentionOff)
            }
        }
    }
}
