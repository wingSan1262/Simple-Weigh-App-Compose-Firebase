package com.example.sawitprotest.feature.weighbridge.navigator

import androidx.navigation.NavHostController

class TaskScreenNavigator(
    val nav : NavHostController
){
    fun navigateToDetail() {
        nav.navigate(
            route = WEIGHT_BRIDGE_DETAIL_SCREEN
        )
    }

    fun navigateToSort() {
        nav.navigate(
            route = FILTER_SORT_SCREEN
        )
    }
    fun navigateToMain (isPop : Boolean = false) {
        nav.navigate(
            route = WEIGHT_BRIDGE_LIST_SCREEN
        ){ if(isPop) popUpTo(WEIGHT_BRIDGE_LIST_SCREEN) }
    }
//
//    fun pop(){
//        nav.popBackStack()
//    }
//    fun navigateToWebviewNews(){
//        nav.navigate(WEBVIEW_NEWS)
//    }
}