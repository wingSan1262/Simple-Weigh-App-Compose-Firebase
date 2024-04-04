package com.example.sawitprotest.base.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.sawitprotest.base.base_entity.Event
import com.example.sawitprotest.base.base_entity.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull


fun <K> StateFlow<Event<ResourceState<K>>>.collectEventState(): Flow<ResourceState<K>> {
    return this.mapNotNull{
        it.contentIfNotHandled?.let {
            return@mapNotNull it
        } ?: run{
            return@mapNotNull null
        }
    }
}

fun <K> StateFlow<Event<ResourceState<K>>>.collectResource(): Flow<ResourceState<K>> {
    return this.mapNotNull{
        it.mContent?.let {
            return@mapNotNull it
        } ?: run{
            return@mapNotNull null
        }
    }
}

@Composable
fun <K> Flow<ResourceState<K>>.asState(): ResourceState<K> {
    val data by this.collectAsState(initial = ResourceState(false, false, false))
    return data
}
