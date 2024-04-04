package com.example.sawitprotest.base

import android.content.Context
import android.widget.Toast
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider
import com.example.sawitprotest.base.extensions.*
import com.example.sawitprotest.feature.weighbridge.viewmodel.WeighBridgeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import java.util.*

@RunWith(RobolectricTestRunner::class)
class ExtensionsTest {

    val dispatcher = TestCoroutineDispatcher()
    var setupCalled = false

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `test toFormattedDateString`() {
        val timestamp = 1646140800000 // corresponds to "Tuesday, 01 Mar 2022 00:00"
        val expected = "Tuesday, 01 Mar 2022 20:20"
        val actual = timestamp.toFormattedDateString()
        assertEquals(expected, actual)
    }

    @Test
    fun `test showToast`() {
        // Given
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = "Test Message"

        // When
        context.showToast(message)

    }

    @Test
    fun `test runScoped`() {
        val viewModel = WeighBridgeViewModel(mock(), mock(), mock(), mock())
        var blockCalled = false
        val block: suspend () -> Unit = { blockCalled = true }
        viewModel.runScoped(block)
        runBlocking { block() }
        assert(blockCalled)
    }

}