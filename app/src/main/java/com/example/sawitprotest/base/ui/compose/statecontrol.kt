package com.example.sawitprotest.base.ui.compose

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

data class LazyListControlData<Data> @OptIn(ExperimentalMaterial3Api::class) constructor(
    val appendData: KFunction1<List<Data>, Unit>,
    val releasePagingBlock: () -> Unit,
    val scrollState: LazyListState,
    val lazyList: SnapshotStateList<Data>,
    val reset: () -> Unit,
    val refreshState: PullToRefreshState,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <Data>lazyColumnControl(
    data : List<Data>,
    reachBottom : (Int)->Unit,
    onRefresh : (Int)->Unit,
) : LazyListControlData<Data> {



    val coroutineScope = rememberCoroutineScope()

    var currentPage by remember { mutableStateOf(1) }
    var isAvailableForFetch by remember { mutableStateOf(true) }

    var lazyList = remember {
        mutableStateListOf<Data>().apply {
            data.forEach {
                coroutineScope.launch {
                    delay(50)
                    this@apply.add(it)
                }
            }
        }
    }

    fun reset(){
        currentPage = 1;
        lazyList.clear();
        isAvailableForFetch = true;
    }

    val refreshState = rememberPullToRefreshState()
    LaunchedEffect(key1 = refreshState.isRefreshing) {
        if(!refreshState.isRefreshing) return@LaunchedEffect
        reset()
        onRefresh(currentPage)
    }

    val scrollState = rememberLazyListState()
    fun LazyListState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
    val endOfListReached by remember {
        derivedStateOf {
            scrollState.isScrolledToEnd()
        }
    }

    LaunchedEffect(key1 = endOfListReached){
        if(!lazyList.isEmpty() && isAvailableForFetch && endOfListReached){
            isAvailableForFetch = false;
            currentPage++;
            reachBottom(currentPage);
        }
    }

    fun appendData(data : List<Data>){
        lazyList.addAll(data)
    }
    fun releasePagingBlock(){
        isAvailableForFetch = true;
    }

    return LazyListControlData(
        ::appendData, ::releasePagingBlock, scrollState, lazyList,
        ::reset, refreshState
    )
}