package com.example.sawitprotest.base.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun ShowFadeInSlideBottom(
    delay: Long,
    duration: Int = 500,
    content: @Composable ()->Unit,
){

    var isShow by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true){
        delay(delay)
        isShow = true
    }

    AnimatedVisibility(
        visible = isShow,
        enter = fadeIn(tween(duration)) + slideInVertically(
            tween(1500),
            initialOffsetY = {-it/15}
        ),
    ) {
        content()
    }
}