@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.sawitprotest.feature.weighbridge.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.SwipeToDismissBoxValue.*
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.sawitprotest.base.extensions.asState
import com.example.sawitprotest.base.extensions.collectEventState
import com.example.sawitprotest.base.extensions.showToast
import com.example.sawitprotest.base.ui.compose.ShowFadeInSlideBottom
import com.example.sawitprotest.base.ui.compose.lazyColumnControl
import com.example.sawitprotest.base.ui.theme.primaryColor
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.example.sawitprotest.feature.weighbridge.component.BridgeWeightEntryCard
import com.example.sawitprotest.feature.weighbridge.component.EmptyIllustration
import com.example.sawitprotest.feature.weighbridge.component.MyAlertDialog
import com.example.sawitprotest.feature.weighbridge.navigator.WEIGHT_BRIDGE_DETAIL_SCREEN
import com.example.sawitprotest.feature.weighbridge.navigator.WEIGHT_BRIDGE_LIST_SCREEN
import com.example.sawitprotest.feature.weighbridge.viewmodel.WeighBridgeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun NavGraphBuilder.BrigeWeightListScreen(
    viewModel: WeighBridgeViewModel,
    onOpenDetail: () -> Unit,
    onCreate: () -> Unit,
    onSortFilter: () -> Unit,
    onBack: () -> Unit,
) {
    composable(route = WEIGHT_BRIDGE_LIST_SCREEN) {

        val (_, isError, isLoading, data, _, error) =
            viewModel.getWeightListData.collectEventState().asState()


        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        var deleteDialog by remember { mutableStateOf(false) }

        val (appendData, releasePagingBlock, scrollState, listItem,
            reset, refreshState) =
            lazyColumnControl(
                listOf<BridgeWeightEntryItem>(), {
                    //no pagination at moment
                }, {
                    viewModel.getWeightList()
                }
            )

        LaunchedEffect(key1 = true) {
            refreshState.startRefresh()
        }

        val (isSuccesDelete, isErrorDelete, _, _, _, errorDelete) =
            viewModel.deleteWeigthBridgeData.collectEventState().asState()
        LaunchedEffect(key1 = isErrorDelete) {
            errorDelete?.message?.let { context.showToast(it) }
        }
        LaunchedEffect(key1 = isSuccesDelete) {
            if (isSuccesDelete) {
                refreshState.startRefresh()
            }
        }

        LaunchedEffect(key1 = isLoading) {
            launch {
                delay(300)
                if (!isLoading)
                    refreshState.endRefresh()
            }
        }

        LaunchedEffect(key1 = isError) {
            error?.message?.let {
                context.showToast(it)
            }
        }

        LaunchedEffect(key1 = data) {
            if (data.isNullOrEmpty() && listItem.isEmpty()) {
                if (isError) releasePagingBlock();
                return@LaunchedEffect
            }
            appendData(data ?: listOf())
            releasePagingBlock()
        }


        val scaleFraction = if (refreshState.isRefreshing) 1f else
            LinearOutSlowInEasing.transform(refreshState.progress).coerceIn(0f, 1f)
        Box(Modifier.nestedScroll(refreshState.nestedScrollConnection)) {
            LazyColumn(Modifier.fillMaxSize(), scrollState) {
                item {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Weigh Bridge Entry List",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (listItem.isEmpty()) {
                    item {
                        EmptyIllustration()
                    }
                } else {
                    itemsIndexed(items = listItem, itemContent = { index, it ->
                        Box(
                            Modifier
                                .fillMaxSize()
                                .height(300.dp)
                        ){
                            ShowFadeInSlideBottom(delay = 25) {
                                BridgeWeightEntryCard(it, {
                                    viewModel.selectedItem = it
                                    viewModel.isEdit = true
                                    onOpenDetail()
                                }) {
                                    viewModel.selectedItem = it
                                    deleteDialog = true
                                }
                            }
                        }
                    })
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
            PullToRefreshContainer(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction),
                state = refreshState,
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                FloatingActionButton(
                    onClick = { onCreate() },
                    containerColor = primaryColor,
                    ) {
                    Icon(Icons.Filled.Add, tint = Color.White, contentDescription = "Add")
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = { onSortFilter() },
                    containerColor = primaryColor,
                    ) {
                    Icon(Icons.Filled.Search, tint = Color.White, contentDescription = "Search")
                }
            }

        }
        if (deleteDialog) {
            MyAlertDialog(title = "Delete", message = "Are you sure want to delete this item?",
                onClose = {
                    deleteDialog = false
                },
                onAccept = {
                    viewModel.deleteWeightBridge(viewModel.selectedItem.id)
                }
            )
        }
    }
}

