package com.example.sawitprotest.feature.weighbridge.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.example.sawitprotest.feature.weighbridge.screens.BridfeWeightItemScreen
import com.example.sawitprotest.feature.weighbridge.screens.BrigeWeightListScreen
import com.example.sawitprotest.feature.weighbridge.screens.FilterAndSort
import com.example.sawitprotest.feature.weighbridge.screens.FilterSortScreen
import com.example.sawitprotest.feature.weighbridge.viewmodel.WeighBridgeViewModel

@Composable
fun WeightBridgeNavigationHost(
    vm: WeighBridgeViewModel,
    nav: NavHostController,
) {
    val navigator = remember(nav) {
        TaskScreenNavigator(nav)
    }

    NavHost(nav, startDestination = WEIGHT_BRIDGE_LIST_SCREEN) {
        BrigeWeightListScreen(vm, {
            navigator.navigateToDetail()

        }, {
            vm.selectedItem = BridgeWeightEntryItem()
            vm.isEdit = false
            navigator.navigateToDetail()
        }, {
            navigator.navigateToSort()
        }) {
        }

        BridfeWeightItemScreen(vm) {
            navigator.navigateToMain(true)
        }
        FilterSortScreen(vm){
            navigator.navigateToMain(true)
        }
    }
}

val WEIGHT_BRIDGE_LIST_SCREEN by lazy { "WEIGHT_BRIDGE_LIST_SCREEN" }
val WEIGHT_BRIDGE_DETAIL_SCREEN by lazy { "WEIGHT_BRIDGE_DETAIL_SCREEN" }
val FILTER_SORT_SCREEN by lazy { "FILTER_SORT_SCREEN" }