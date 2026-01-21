package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun MoaWheelPicker(
    modifier: Modifier = Modifier,
    items: ImmutableList<Int>,
    initialSelectedIndex: Int,
    visibleItemCount: Int = 5,
    itemHeight: Dp = 34.dp,
    itemSpacing: Dp = 10.dp,
    onItemSelected: (Int) -> Unit,
    itemToString: (Int) -> String = { it.toString().padStart(2, '0') },
) {
    val itemCount = items.size
    val infiniteMultiplier = 10000
    val infiniteItemCount = itemCount * infiniteMultiplier

    val halfVisibleCount = visibleItemCount / 2
    val middlePosition = (infiniteMultiplier / 2) * itemCount + initialSelectedIndex

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = middlePosition - halfVisibleCount
    )

    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val density = LocalDensity.current
    val totalItemHeightPx = with(density) { (itemHeight + itemSpacing).toPx() }

    val centerInfiniteIndex by remember {
        derivedStateOf {
            val firstVisibleIndex = listState.firstVisibleItemIndex
            val firstVisibleOffset = listState.firstVisibleItemScrollOffset

            if (firstVisibleOffset > totalItemHeightPx / 2) {
                firstVisibleIndex + halfVisibleCount + 1
            } else {
                firstVisibleIndex + halfVisibleCount
            }
        }
    }

    val selectedIndex by remember {
        derivedStateOf { centerInfiniteIndex % itemCount }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { selectedIndex }
            .distinctUntilChanged()
            .collect { index -> onItemSelected(items[index]) }
    }

    LazyColumn(
        modifier = modifier
            .height(itemHeight * visibleItemCount + itemSpacing * (visibleItemCount - 1)),
        state = listState,
        flingBehavior = snapFlingBehavior,
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(infiniteItemCount) { infiniteIndex ->
            val actualIndex = infiniteIndex % itemCount
            val item = items[actualIndex]

            val isSelected by remember {
                derivedStateOf { infiniteIndex == centerInfiniteIndex }
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(itemHeight),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = itemToString(item),
                    style = if (isSelected) {
                        MoaTheme.typography.t2_500
                    } else {
                        MoaTheme.typography.t3_500
                    },
                    color = if (isSelected) {
                        MoaTheme.colors.textHighEmphasis
                    } else {
                        MoaTheme.colors.textLowEmphasis
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MoaWheelPickerPreview() {
    MoaTheme {
        MoaWheelPicker(
            modifier = Modifier
                .width(300.dp),
            items = (0..23).toImmutableList(),
            initialSelectedIndex = 9,
            onItemSelected = {},
            itemToString = { it.toString().padStart(2, '0') }
        )
    }
}

