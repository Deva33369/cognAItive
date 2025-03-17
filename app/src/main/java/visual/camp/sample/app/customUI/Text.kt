package visual.camp.sample.app.customUI

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import visual.camp.sample.app.theme.blue_white
import visual.camp.sample.app.theme.blue_white_shadow

@Composable
fun CustomTextHeader(
    text: String,
    fontSize: TextUnit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "scale"
    )
    val offset = Offset(0.0f, 10.0f)

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text,
            color = blue_white,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                fontSize = fontSize,
                shadow = Shadow(
                    color = blue_white_shadow,
                    offset = offset,
                    blurRadius = 3f
                ),
            ),
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                }
                .align(Alignment.Center),
        )
    }
}

@Composable
fun CustomTextHeaderPlain(
    text: String,
    fontSize: TextUnit
) {
    val offset = Offset(0.0f, 10.0f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = text,
            color = blue_white,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                fontSize = fontSize,
                shadow = Shadow(
                    color = blue_white_shadow,
                    offset = offset,
                    blurRadius = 3f
                ),
            ),
        )
    }
}