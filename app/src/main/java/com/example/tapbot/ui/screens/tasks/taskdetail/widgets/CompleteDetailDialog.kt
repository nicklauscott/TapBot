package com.example.tapbot.ui.screens.tasks.taskdetail.widgets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompleteDetailDialog(modifier: Modifier = Modifier, isPortrait: Boolean = true,
                         onDismissRequest: () -> Unit, confirmButton: (String, String) -> Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()

    val isKeyboardVisible = remember { mutableStateOf(false) }

    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    if (WindowInsets.isImeVisible) isKeyboardVisible.value = true else isKeyboardVisible.value = false

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }


    Dialog(onDismissRequest = onDismissRequest ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 2.percentOfScreenHeight(), horizontal = 3.percentOfScreenWidth())
            .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Task Detail", style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))

            Spacer(modifier = Modifier.height(4.percentOfScreenHeight()))

            TextField(
                value = name.value, onValueChange = {
                    if (it.length <= 15) name.value = it },
                textStyle = TextStyle(
                    fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 15.sp
                ),
                label = { Text(text = "Name", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)) },
                modifier = modifier
                    .fillMaxWidth()
                    .height(6.percentOfScreenHeight())
                    .focusRequester(focusRequester),
                singleLine = true,
                shape = RoundedCornerShape(4.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(1.5.percentOfScreenHeight()))

            TextField(
                value = description.value, onValueChange = { description.value = it },
                textStyle = TextStyle(
                    fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 15.sp
                ),
                label = { Text(text = "Description", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)) },
                modifier = modifier
                    .fillMaxWidth()
                    .height(6.percentOfScreenHeight()),
                singleLine = true,
                shape = RoundedCornerShape(4.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(4.percentOfScreenHeight()))

            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .height(6.percentOfScreenHeight()),
                shape = RoundedCornerShape(4.dp),
                enabled = name.value.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.95f),
                    contentColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                ),
                onClick = {
                    keyboardController?.hide()
                    confirmButton(name.value, description.value)
                }) {
                Text(text = "Save", style = MaterialTheme.typography.bodySmall)
            }

           if (!isPortrait && isKeyboardVisible.value) {
               Spacer(modifier = Modifier.height(70.percentOfScreenHeight()))
           }
        }
    }
}


