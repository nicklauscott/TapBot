package com.example.tapbot.ui.screens.tasks.taskdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@Composable
fun ScreenCoordinates(
    modifier: Modifier = Modifier, task: ClickTask,
    range: List<Int> = (0..2000).step(20).toList(), onChangeX: (Int) -> Unit, onChangeY: (Int) -> Unit, assist: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Click Coordinates", style = MaterialTheme.typography.bodyMedium,
            maxLines = 2, fontSize = 13.sp,
            color = MaterialTheme.colorScheme.inverseOnSurface,
        )

        Row(
            modifier = Modifier
                .border(width = 0.3.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                ),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {
            CoordinateCell(
                modifier = Modifier, symbol = "X", selectedItem = task.x.toString(), range = range, assist = assist) { onChangeX(it) }

            Spacer(modifier = Modifier.width(2.percentOfScreenWidth()))

            CoordinateCell(modifier = Modifier, symbol = "Y", selectedItem = task.y.toString(), range = range, assist = assist) { onChangeY(it) }
        }
    }
}

@Composable
fun CoordinateCell(
    modifier: Modifier = Modifier,
    symbol: String, selectedItem: String, range: List<Int>, assist: Boolean = false, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 1.percentOfScreenWidth()),
        horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = symbol, style = MaterialTheme.typography.titleLarge,
            maxLines = 2, fontSize = 15.sp,
            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.8f),
        )

        Spacer(modifier = Modifier.width(1.percentOfScreenWidth()))

        CustomSpinner(selectedItem = selectedItem, items = range, assist = assist) {
            onClick(it)
        }
    }
}



@Composable
fun ScreenCoordinatesLandscape(
    modifier: Modifier = Modifier, task: ClickTask,
    range: List<Int> = (0..2000).step(20).toList(), onChangeX: (Int) -> Unit, onChangeY: (Int) -> Unit, assist: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Click Coordinates", style = MaterialTheme.typography.bodyMedium,
            maxLines = 2, fontSize = 13.sp,
            color = MaterialTheme.colorScheme.inverseOnSurface,
        )

        Row(
            modifier = Modifier
                .border(width = 0.3.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                ),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {
            CoordinateCellLandscape(
                modifier = modifier, symbol = "X", selectedItem = task.x.toString(), range = range, assist = assist) { onChangeX(it) }

            Spacer(modifier = Modifier.width(2.percentOfScreenWidth()))

            CoordinateCellLandscape(modifier = modifier, symbol = "Y", selectedItem = task.y.toString(), range = range, assist = assist) { onChangeY(it) }
        }
    }
}


@Composable
fun CoordinateCellLandscape(
    modifier: Modifier = Modifier,
    symbol: String, selectedItem: String, range: List<Int>, assist: Boolean = false, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 1.percentOfScreenWidth()),
        horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = symbol, style = MaterialTheme.typography.titleLarge,
            maxLines = 2, fontSize = 15.sp,
            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.8f),
        )

        Spacer(modifier = Modifier.width(1.percentOfScreenWidth()))

        CustomSpinnerLandscape(modifier = modifier, selectedItem = selectedItem, items = range, assist = assist) {
            onClick(it)
        }
    }
}


@Composable
fun CustomSpinnerLandscape(
    modifier: Modifier = Modifier,
    selectedItem: String, items: List<Int>,
    assist: Boolean = false,
    onClick: (Int) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }


    Row(modifier = modifier.width(10.percentOfScreenWidth())
        .height(5.percentOfScreenHeight())
        .padding(vertical = 1.percentOfScreenHeight())
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
        .clickable { expanded.value = true },
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier
            .weight(0.7f)
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = selectedItem.take(if (selectedItem.length == 4) 2 else 3),
                fontSize = 18.sp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.background)
        }
        Column(modifier = Modifier
            .weight(0.3f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Show more", tint = MaterialTheme.colorScheme.background)
        }
    }


    DropdownMenu(expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = {
                    Text(text = if (assist && item == 0) "Use Assist" else item.toString(),
                        style = MaterialTheme.typography.headlineSmall) },
                onClick = {
                    expanded.value = false
                    onClick(item)
                })
        }

    }
}


@Composable
fun CustomSpinner(
    selectedItem: String, items: List<Int>,
    assist: Boolean = false,
    onClick: (Int) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }


    Row(modifier = Modifier.width(10.percentOfScreenWidth())
        .height(5.percentOfScreenHeight())
        .padding(vertical = 1.percentOfScreenHeight())
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
        .clickable { expanded.value = true },
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier
            .weight(0.7f)
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = selectedItem.take(if (selectedItem.length == 4) 2 else 3),
                fontSize = 18.sp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.background)
        }
        Column(modifier = Modifier
            .weight(0.3f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Show more", tint = MaterialTheme.colorScheme.background)
        }
    }


    DropdownMenu(expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = {
                    Text(text = if (assist && item == 0) "Use Assist" else item.toString(),
                        style = MaterialTheme.typography.headlineSmall) },
                onClick = {
                    expanded.value = false
                    onClick(item)
                })
        }

    }
}



@Composable
fun CustomSpinner(
    modifier: Modifier = Modifier,
    selectedItem: String,
    items: List<Int>,
    assist: Boolean = false,
    onClick: (Int) -> Unit) {
    val expanded = remember {
        mutableStateOf(false)
    }


    Row(modifier = modifier.width(10.percentOfScreenWidth())
        .height(5.percentOfScreenHeight())
        .padding(vertical = 1.percentOfScreenHeight())
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
        .clickable { expanded.value = true },
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier
            .weight(0.7f)
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = selectedItem,
                fontSize = 18.sp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.background)
        }
        Column(modifier = Modifier
            .weight(0.3f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Show more", tint = MaterialTheme.colorScheme.background)
        }
    }


    DropdownMenu(expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = {
                    Text(text = if (assist && item == 0) "Use Assist" else item.toString(),
                        style = MaterialTheme.typography.headlineSmall) },
                onClick = {
                    expanded.value = false
                    onClick(item)
                })
        }

    }
}



@Composable
fun CustomSpinner(
    modifier: Modifier = Modifier,
    selectedItem: String,
    values: List<Any>,
    onClick: (Any) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }


    Row(modifier = modifier.width(10.percentOfScreenWidth())
        .height(5.percentOfScreenHeight())
        .padding(vertical = 1.percentOfScreenHeight())
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
        .clickable { expanded.value = true },
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier
            .weight(0.7f)
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = selectedItem.take(if (selectedItem.length == 4) 2 else 3),
                fontSize = 18.sp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.background)
        }
        Column(modifier = Modifier
            .weight(0.3f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Show more", tint = MaterialTheme.colorScheme.background)
        }
    }


    DropdownMenu(expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier
    ) {
        values.forEach { item ->
            DropdownMenuItem(
                text = {
                    Text(text = item.toString(),
                        style = MaterialTheme.typography.headlineSmall) },
                onClick = {
                    expanded.value = false
                    onClick(item)
                })
        }

    }
}

@Composable
fun CustomYesNoSpinner(
    modifier: Modifier = Modifier,
    selectedItem: Int,
    onClick: (Int) -> Unit) {

    val expanded = remember {
        mutableStateOf(false)
    }

    Row(modifier = modifier.width(10.percentOfScreenWidth())
        .height(5.percentOfScreenHeight())
        .padding(vertical = 1.percentOfScreenHeight())
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
        .clickable { expanded.value = true },
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier
            .weight(0.7f)
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = if (selectedItem == 1) "Yes" else "No",
                fontSize = 18.sp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.background)
        }
        Column(modifier = Modifier
            .weight(0.3f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Show more", tint = MaterialTheme.colorScheme.background)
        }
    }


    DropdownMenu(expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier
    ) {
        listOf("Yes", "No").forEach { item ->
            DropdownMenuItem(
                text = {
                    Text(text = item,
                        style = MaterialTheme.typography.headlineSmall) },
                onClick = {
                    expanded.value = false
                    onClick(if (item == "Yes") 1 else 0)
                })
        }

    }
}