package com.example.sawitprotest.base

import com.example.sawitprotest.base.base_entity.Event
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EventTest {

    @Test
    fun `test Event class`() {
        // Given
        val content = "Test Content"
        val event = Event(content)

        // When
        val firstAccess = event.contentIfNotHandled
        val secondAccess = event.contentIfNotHandled

        // Then
        assertEquals(content, firstAccess)
        assertTrue(event.hasBeenHandled())
        assertEquals(null, secondAccess)
    }
}