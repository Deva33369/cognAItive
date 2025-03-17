package visual.camp.sample.app.customUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import visual.camp.sample.app.R

@Composable
fun NormalBlueButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .wrapContentSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_blank),
            contentDescription = "Button"
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 32.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun EmptyProfileBlueButton(
    text: String,
    isEditing: Boolean,
    onTextChange: (String) -> Unit,
    onClick: () -> Unit,
    focusRequester: FocusRequester
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_background),
            contentDescription = "Button"
        )
        if (isEditing) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus() // Auto-focus when editing starts
            }

            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                modifier = Modifier
                    .size(100.dp, 50.dp) // Adjust for button size
                    .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(5.dp))
                    .focusRequester(focusRequester)
                    .padding(top = 7.dp)
            )
        }
    }
}

@Composable
fun ProfileBlueButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = { onClick() })
            .wrapContentSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_blank),
            contentDescription = "Button"
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 32.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}