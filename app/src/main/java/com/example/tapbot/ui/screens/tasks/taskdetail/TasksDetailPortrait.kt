package com.example.tapbot.ui.screens.tasks.taskdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tapbot.R
import com.example.tapbot.data.sevices.ForegroundService
import com.example.tapbot.data.sevices.TapBotAccessibilityService
import com.example.tapbot.domain.model.Actions
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.ui.screens.tasks.taskdetail.components.ClickActionCell
import com.example.tapbot.ui.screens.tasks.taskdetail.components.DelayActionCell
import com.example.tapbot.ui.screens.tasks.taskdetail.components.StartLoopActionCell
import com.example.tapbot.ui.screens.tasks.taskdetail.components.StopLoopActionCell
import com.example.tapbot.ui.screens.tasks.taskdetail.state_and_events.TaskDetailScreenUiEvent
import com.example.tapbot.ui.screens.tasks.taskdetail.components.ActionCell
import com.example.tapbot.ui.screens.tasks.taskdetail.widgets.AccessibilityDialog
import com.example.tapbot.ui.screens.tasks.taskdetail.widgets.CompleteDetailDialog
import com.example.tapbot.ui.screens.tasks.taskdetail.widgets.DeleteDialog
import com.example.tapbot.ui.screens.tasks.taskdetail.widgets.Fab
import com.example.tapbot.ui.screens.tasks.taskdetail.widgets.StopTaskDialog
import com.example.tapbot.ui.screens.tasks.taskdetail.widgets.TasksDetailTopBar
import com.example.tapbot.ui.screens.util.cornerRadius
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import com.example.tapbot.ui.screens.util.rectangularModifier
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksDetailPortrait(
    navController: NavController, viewModel: TaskDetailViewModel, snackBarHostState: SnackbarHostState
) {
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }

    val errorMessage = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(true) }

    val tasks by viewModel.state

    val showCompleteDialog = remember { mutableStateOf(false) }
    val showStopTaskDialog = remember { mutableStateOf(false) }
    val showEnableAccessibilityDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }

    if (showCompleteDialog.value) {
        CompleteDetailDialog(onDismissRequest = {
            showCompleteDialog.value = false
        }) { name, description ->
            showCompleteDialog.value = false
            viewModel.onEvent(TaskDetailScreenUiEvent.CompleteTask(name = name, description = description))
        }
    }

    if (showDeleteDialog.value) {
        DeleteDialog( onDismissRequest = { showDeleteDialog.value = false }) {
            showDeleteDialog.value = false
            viewModel.onEvent(TaskDetailScreenUiEvent.DeleteTask)
        }
    }

    if (showStopTaskDialog.value) {
        StopTaskDialog(onDismissRequest = { showStopTaskDialog.value = false }) {
            showStopTaskDialog.value = false
            viewModel.onEvent(TaskDetailScreenUiEvent.StopOldAndStartNewTask)
        }
    }

    if (showEnableAccessibilityDialog.value) {
        AccessibilityDialog(onDismissRequest = { showEnableAccessibilityDialog.value = false }) {
            showEnableAccessibilityDialog.value = false
            ForegroundService.startService<TapBotAccessibilityService>(context)
        }
    }


    LaunchedEffect(viewModel) {
        viewModel.channel.collectLatest {
            when (it) {
                is TaskDetailViewModel.TaskDetailUiChannel.TaskMangerError -> {
                    errorMessage.value = it.message
                    isError.value = true
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar("")
                }

                is TaskDetailViewModel.TaskDetailUiChannel.TaskMangerWarning -> {
                    errorMessage.value = it.message
                    isError.value = false
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar("")
                }
                TaskDetailViewModel.TaskDetailUiChannel.CompleteTaskDetail -> showCompleteDialog.value = true
                TaskDetailViewModel.TaskDetailUiChannel.DeletedTask -> navController.popBackStack()
                TaskDetailViewModel.TaskDetailUiChannel.CancelRunningTask -> showStopTaskDialog.value = true
                TaskDetailViewModel.TaskDetailUiChannel.EnableAccessibilityService -> showEnableAccessibilityDialog.value = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TasksDetailTopBar(
                title = tasks.taskGroup?.name ?: "Loading...",
                canRun = tasks.taskGroup != null && tasks.taskList.isNotEmpty(),
                running = viewModel.serviceState.value.running,
                isThisRunningTask = viewModel.serviceState.value.runningTaskId == tasks.taskGroup?.taskGroupId,
                canSave = tasks.canSave(), favorite = tasks.taskGroup?.favorite ?: false,
                onToggleFavorite = { viewModel.onEvent(TaskDetailScreenUiEvent.ToggleFavorite) },
                onClickSave = { viewModel.onEvent(TaskDetailScreenUiEvent.SaveTask) },
                onClickPlay = { viewModel.onEvent(TaskDetailScreenUiEvent.PlayTask) },
                onClickDelete = { showDeleteDialog.value = true },
            ) {
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
            SnackbarHost(hostState = snackBarHostState) {
                Row(
                    modifier = rectangularModifier(
                        background = if (isError.value) MaterialTheme.colorScheme.error
                            else Color.Yellow.copy(alpha = 0.3f),
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
    ) { padding ->
        padding.calculateTopPadding()

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

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment =
                Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Choose an action", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    )
                }

                Spacer(modifier = Modifier.height(1.percentOfScreenHeight()))

                Actions.values().forEach { action ->
                    ActionCell(
                        resourceId = when (action) {
                        Actions.CLICK -> R.drawable.click_action
                        Actions.DELAY -> R.drawable.delay_action
                        Actions.STOP_LOOP -> R.drawable.stop_action
                        Actions.LOOP -> R.drawable.loop_action
                    },
                        action = action.value) {
                        scope.launch {
                            sheetState.hide()
                            showBottomSheet.value = false
                            viewModel.onEvent(TaskDetailScreenUiEvent.AddAction(action))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(1.percentOfScreenHeight()))

            }
        }

        when  {
            tasks.loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            else -> {
                Box {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 2.percentOfScreenWidth())
                    ) {

                        item {
                            Spacer(modifier = Modifier.height(9.percentOfScreenHeight()))
                        }

                        itemsIndexed(items = tasks.taskList) { index, task ->
                            if (task is ClickTask) {
                                ClickActionCell(task = task, onclickDelete = {
                                    viewModel.onEvent(TaskDetailScreenUiEvent.DeleteAction(index))
                                }) { newTask ->
                                    viewModel.onEvent(TaskDetailScreenUiEvent.EditAction(index, newTask))
                                }
                            }

                            if (task is DelayTask) {
                                DelayActionCell(task = task, onEditTask = { newTask ->
                                    viewModel.onEvent(TaskDetailScreenUiEvent.EditAction(index, newTask))
                                }) {
                                    viewModel.onEvent(TaskDetailScreenUiEvent.DeleteAction(index))
                                }
                            }

                            if (task is StartLoop) {
                                StartLoopActionCell(task = task, onEditTask = { newTask ->
                                    viewModel.onEvent(TaskDetailScreenUiEvent.EditAction(index, newTask))
                                }) {
                                    viewModel.onEvent(TaskDetailScreenUiEvent.DeleteAction(index))
                                }
                            }

                            if (task is StopLoop) {
                                val parentLoop = tasks.taskList.filterIsInstance<StartLoop>().find { it.id == task.prentLoopId }
                                StopLoopActionCell(task = task, startLoop = parentLoop, onEditTask = { newTask ->
                                    viewModel.onEvent(TaskDetailScreenUiEvent.EditAction(index, newTask))
                                }) {
                                    viewModel.onEvent(TaskDetailScreenUiEvent.DeleteAction(index))
                                }
                            }
                        }
                    }

                    if (tasks.saving) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }
    }
}