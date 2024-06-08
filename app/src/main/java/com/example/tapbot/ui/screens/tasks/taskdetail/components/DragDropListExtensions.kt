package com.example.tapbot.ui.screens.tasks.taskdetail.components

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.getVisibleItemInfoFor(absolute: Int): LazyListItemInfo? {
    return layoutInfo.visibleItemsInfo.getOrNull(absolute - layoutInfo.visibleItemsInfo.first().index)
}

val LazyListItemInfo.offsetEnd: Int
    get() = offset + size

fun <T> MutableList<T>.move(from: Int, to: Int) {
    if (from == to) return

    val element = removeAt(from) ?: return
    add(to, element)
}