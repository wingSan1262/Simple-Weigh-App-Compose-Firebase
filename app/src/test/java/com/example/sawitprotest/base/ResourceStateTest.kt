package com.example.sawitprotest.base

import com.example.sawitprotest.base.base_entity.ResourceState
import org.junit.Assert.assertEquals
import org.junit.Test

class ResourceStateTest {

    @Test
    fun `test ResourceState class`() {
        // Given
        val data = "Test Data"
        val resourceState = ResourceState(true, false, false, data)

        // Then
        assertEquals(false, resourceState.isLoading)
        assertEquals(false, resourceState.isError)
        assertEquals(true, resourceState.isSuccess)
        assertEquals(data, resourceState.data)
    }
}