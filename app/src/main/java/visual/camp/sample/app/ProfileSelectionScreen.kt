package visual.camp.sample.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import visual.camp.sample.app.theme.CognAItiveTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileSelectionScreen(
    navController: NavController = rememberNavController()
) {
    val profiles = remember { mutableStateListOf<String?>(null, null, null) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedSlot by remember { mutableStateOf(-1) }
    var newProfileName by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Profile Selection",
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(32.dp))
            profiles.forEachIndexed { index, profile ->
                Button(onClick = {
                    if(!profile.isNullOrEmpty()) {
                        navController.navigate(Screen.ProfileLandingScreen.route)
                    } else {
                        selectedSlot = index
                        showDialog = true
                    }
                }) {
                    Text(profile ?: "Empty Slot")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Enter Profile Name") },
            text = {
                TextField(
                    value = newProfileName,
                    onValueChange = { newProfileName = it },
                    placeholder = { Text("Profile Name") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (newProfileName.isNotBlank()) {
                        profiles[selectedSlot] = newProfileName
                        showDialog = false
                        newProfileName = ""
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}