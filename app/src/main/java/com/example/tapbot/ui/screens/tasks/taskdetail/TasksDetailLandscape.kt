package com.example.tapbot.ui.screens.tasks.taskdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tapbot.R
import com.example.tapbot.domain.model.Actions
import com.example.tapbot.ui.screens.tasks.taskdetail.widgets.ActionCell
import com.example.tapbot.ui.screens.tasks.taskdetail.widgets.Fab
import com.example.tapbot.ui.screens.tasks.taskdetail.widgets.TasksDetailTopBar
import com.example.tapbot.ui.screens.util.cornerRadius
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import com.example.tapbot.ui.screens.util.rectangularModifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksDetailLandscape(navController: NavController, viewModel: TaskDetailViewModel,
                         snackBarHostState: SnackbarHostState) {

    val errorMessage = remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }

    val task = viewModel.state.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TasksDetailTopBar(modifier = Modifier.height(14.percentOfScreenHeight()),
                title = task.taskGroup?.name ?: "Loading...") {
                navController.popBackStack()
            }
        },
        floatingActionButton = {
            Fab {
                showBottomSheet.value = true
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
            {
                Row(
                    modifier = rectangularModifier(
                        background = MaterialTheme.colorScheme.error,
                        height = 40.dp,
                        width = 100.percentOfScreenWidth(),
                        padding = 2.percentOfScreenWidth()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "Warning Icon")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = errorMessage.value, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    ) {
        it.calculateTopPadding()


        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet.value = false },
                modifier = Modifier.fillMaxWidth(),
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = cornerRadius(), topEnd = cornerRadius()),
                containerColor = MaterialTheme.colorScheme.background,
                dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)) },
                scrimColor = Color.Black.copy(alpha = 0.9f)
            ) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.percentOfScreenWidth()), verticalAlignment =
                Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = "Choose an action", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    )
                }

                Spacer(modifier = Modifier.height(1.percentOfScreenHeight()))


                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        Actions.values().take(Actions.values().size / 2).forEach { action ->
                            ActionCell(
                                modifier = Modifier
                                    .height(15.percentOfScreenHeight())
                                    .padding(
                                    vertical = 1.percentOfScreenHeight(),
                                    horizontal = 1.percentOfScreenWidth()
                                )
                                .clip(RoundedCornerShape(cornerRadius())),
                                resourceId = when (action) {
                                    Actions.CLICK -> R.drawable.click_action
                                    Actions.DELAY -> R.drawable.delay_action
                                    Actions.STOP_LOOP -> R.drawable.stop_action
                                    Actions.LOOP -> R.drawable.loop_action
                                },
                                action = action.value
                            ) {
                                scope.launch {
                                    sheetState.hide()
                                    showBottomSheet.value = false
                                    // use the action
                                }
                            }
                        }

                    }


                    Column(modifier = Modifier.weight(1f)) {
                        Actions.values().takeLast(Actions.values().size / 2).forEach { action ->
                            ActionCell(
                                modifier = Modifier
                                    .height(15.percentOfScreenHeight())
                                    .padding(
                                        vertical = 1.percentOfScreenHeight(),
                                        horizontal = 1.percentOfScreenWidth()
                                    )
                                    .clip(RoundedCornerShape(cornerRadius())),
                                resourceId = when (action) {
                                    Actions.CLICK -> R.drawable.click_action
                                    Actions.DELAY -> R.drawable.delay_action
                                    Actions.STOP_LOOP -> R.drawable.stop_action
                                    Actions.LOOP -> R.drawable.loop_action
                                },
                                action = action.value
                            ) {
                                scope.launch {
                                    sheetState.hide()
                                    showBottomSheet.value = false
                                    // use the action
                                }
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(2.percentOfScreenHeight()))

            }
        }

    }

}
