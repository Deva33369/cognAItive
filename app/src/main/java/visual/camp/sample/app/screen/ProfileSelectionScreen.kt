package visual.camp.sample.app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import visual.camp.sample.app.Screen
import visual.camp.sample.app.customUI.CustomTextHeaderPlain
import visual.camp.sample.app.customUI.EmptyProfileBlueButton
import visual.camp.sample.app.customUI.ProfileBlueButton
import visual.camp.sample.app.theme.sky_blue

@Composable
fun ProfileSelectionScreen(
    navController: NavController = rememberNavController()
) {
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var profileNames by remember { mutableStateOf(mutableListOf("Yes", null, null)) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val interactionSource = remember { MutableInteractionSource() }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .background(sky_blue)
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null // Removes click animation
                ) {
                    if (editingIndex != null) {
                        showConfirmationDialog = true
                        focusManager.clearFocus() // Hide keyboard
                    }
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(0.4f)
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                CustomTextHeaderPlain(
                    text = "Profiles",
                    fontSize = 64.sp
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxSize()
            ) {
                profileNames.forEachIndexed { index, name ->
                    if (name == null) {
                        EmptyProfileBlueButton(
                            text = tempName,
                            isEditing = editingIndex == index,
                            onTextChange = { newText ->
                                if (newText.length <= 5) tempName = newText
                            },
                            onClick = {
                                if (editingIndex != null) {
                                    tempName = ""
                                }
                                editingIndex = index
                            },
                            focusRequester = focusRequester
                        )
                    } else {
                        ProfileBlueButton(
                            text = name,
                            onClick = {
                                navController.navigate(Screen.ProfileLandingScreen.route)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirm Name") },
            text = { Text("Are you sure you want to set the name to \"$tempName\"?") },
            confirmButton = {
                TextButton(onClick = {
                    // Confirm and save changes
                    profileNames = profileNames.toMutableList().also { it[editingIndex ?: 0] = tempName }
                    tempName = ""
                    editingIndex = null // Exit editing mode
                    showConfirmationDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showConfirmationDialog = false
                    focusRequester.requestFocus()
                }) {
                    Text("Continue Editing")
                }
            }
        )
    }
}