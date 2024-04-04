package com.example.sawitprotest.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sawitprotest.base.base_classes.BaseUseCase
import com.example.sawitprotest.base.base_entity.Event
import com.example.sawitprotest.base.base_entity.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class BaseUseCaseTest {

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
    fun `test execute`() = runTest {
        // Given
        val expectedData = "Expected Data"
        val runProcess: suspend () -> String =  {
            delay(100)
            expectedData
        }
        val offlineAlternate: suspend () -> String? =  {
            delay(100)
            null
        }
        val useCase = BaseUseCase<Unit, String>()

        // When
        useCase.execute(runProcess, offlineAlternate)

        // Then
        val expected = Event(ResourceState(true, false, false, expectedData))
        val actual = useCase.currentData.value
        assertEquals(expected.mContent, actual.mContent)
    }

    @Test
    fun `test execute when runProcess returns null`() = runTest {
        // Given
        val runProcess: suspend () -> String =  {
            throw IOException("An error occurred")
        }
        val offlineAlternate: suspend () -> String? =  {
            "Offline Data"
        }
        val useCase = BaseUseCase<Unit, String>()

        // When
        useCase.execute(runProcess, offlineAlternate)

        // Then
        val expected = Event(ResourceState(false, true, false, "Offline Data", 500, Throwable("Please Check Your Internet Connection")))
        val actual = useCase.currentData.value
        assertEquals(expected.mContent?.data, actual.mContent?.data)
        assertEquals(expected.mContent?.error?.message, actual.mContent?.error?.message)
        assertEquals(expected.mContent?.code, actual.mContent?.code)
        assertEquals(expected.mContent?.isError, actual.mContent?.isError)
        assertEquals(expected.mContent?.isSuccess, actual.mContent?.isSuccess)
    }

    @Test
    fun `test execute when both runProcess and offlineAlternate throw exceptions`() = runTest {
        // Given
        val runProcess: suspend () -> String =  {
            throw IOException("An error occurred")
        }
        val offlineAlternate: suspend () -> String? =  {
            throw IOException("An error occurred")
        }
        val useCase = BaseUseCase<Unit, String>()

        // When
        useCase.execute(runProcess, offlineAlternate)

        // Then
        val expected = Event(ResourceState(false, true, false, null, 500, Throwable("Please Check Your Internet Connection")))
        val actual = useCase.currentData.value
        assertEquals(expected.mContent?.data, actual.mContent?.data)
        assertEquals(expected.mContent?.error?.message, actual.mContent?.error?.message)
        assertEquals(expected.mContent?.code, actual.mContent?.code)
        assertEquals(expected.mContent?.isError, actual.mContent?.isError)
        assertEquals(expected.mContent?.isSuccess, actual.mContent?.isSuccess)
    }
}