package com.example.sawitprotest.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sawitprotest.data.local.LocalWeightBridgeApi
import com.example.sawitprotest.data.remote.api.firebase.WeighBridgeApi
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.example.sawitprotest.data.remote.model.SearchModel
import com.example.sawitprotest.domain.usecase.CreateWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.DeleteWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.GetWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.UpdateWeightBridgeUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DomainTest {
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
    fun `test CreateWeightBridgeUseCase run with error`() = runTest {
        // Given
        val mockWeighBridgeApi = mockk<WeighBridgeApi>()
        val createWeightBridgeUseCase = CreateWeightBridgeUseCase(mockWeighBridgeApi)
        val entryItem = BridgeWeightEntryItem("testEntryId")

        coEvery { mockWeighBridgeApi.createEntry(entryItem) } throws Exception("Create entry failed")

        // When
        try {
            createWeightBridgeUseCase.run(entryItem)
        } catch (e: Exception) {
            // Then
            coVerify { mockWeighBridgeApi.createEntry(entryItem) }
            assertEquals(createWeightBridgeUseCase.currentData.value.mContent?.isSuccess, false)
            assertEquals(createWeightBridgeUseCase.currentData.value.mContent?.isLoading, false)
            assertEquals(createWeightBridgeUseCase.currentData.value.mContent?.isError, true)
        }
    }

    @Test
    fun `test CreateWeightBridgeUseCase run`() = runTest {
        // Given
        val mockWeighBridgeApi = mockk<WeighBridgeApi>()
        val createWeightBridgeUseCase = CreateWeightBridgeUseCase(mockWeighBridgeApi)
        val entryItem = BridgeWeightEntryItem("testEntryId")

        coEvery { mockWeighBridgeApi.createEntry(entryItem) } returns true

        // When
        createWeightBridgeUseCase.run(entryItem)

        // Then
        coVerify { mockWeighBridgeApi.createEntry(entryItem) }
        assertEquals(createWeightBridgeUseCase.currentData.value.mContent?.isSuccess, true)
        assertEquals(createWeightBridgeUseCase.currentData.value.mContent?.isLoading, false)
        assertEquals(createWeightBridgeUseCase.currentData.value.mContent?.isError, false)
    }

    @Test
    fun `test UpdateWeightBridgeUseCase run with error`() = runTest {
        // Given
        val mockWeighBridgeApi = mockk<WeighBridgeApi>()
        val updateWeightBridgeUseCase = UpdateWeightBridgeUseCase(mockWeighBridgeApi)
        val entryItem = BridgeWeightEntryItem("testEntryId")

        coEvery { mockWeighBridgeApi.updateEntry(entryItem) } throws Exception("Update entry failed")

        updateWeightBridgeUseCase.run(entryItem)
        // When
        coVerify { mockWeighBridgeApi.updateEntry(entryItem) }
        assertEquals(updateWeightBridgeUseCase.currentData.value.mContent?.isSuccess, false)
        assertEquals(updateWeightBridgeUseCase.currentData.value.mContent?.isLoading, false)
        assertEquals(updateWeightBridgeUseCase.currentData.value.mContent?.isError, true)
    }

    @Test
    fun `test UpdateWeightBridgeUseCase run`() = runTest {
        // Given
        val mockWeighBridgeApi = mockk<WeighBridgeApi>()
        val updateWeightBridgeUseCase = UpdateWeightBridgeUseCase(mockWeighBridgeApi)
        val entryItem = BridgeWeightEntryItem("testEntryId")

        coEvery { mockWeighBridgeApi.updateEntry(entryItem) } returns true

        // When
        updateWeightBridgeUseCase.run(entryItem)

        // Then
        coVerify { mockWeighBridgeApi.updateEntry(entryItem) }
        assertEquals(updateWeightBridgeUseCase.currentData.value.mContent?.isSuccess, true)
        assertEquals(updateWeightBridgeUseCase.currentData.value.mContent?.isLoading, false)
        assertEquals(updateWeightBridgeUseCase.currentData.value.mContent?.isError, false)
    }

    @Test
    fun `test DeleteWeightBridgeUseCase run with error`() = runTest {
        // Given
        val mockWeighBridgeApi = mockk<WeighBridgeApi>()
        val deleteWeightBridgeUseCase = DeleteWeightBridgeUseCase(mockWeighBridgeApi)
        val entryId = "testEntryId"

        coEvery { mockWeighBridgeApi.deleteEntry(entryId) } throws Exception("Delete entry failed")

        // When
        deleteWeightBridgeUseCase.run(entryId)

        coVerify { mockWeighBridgeApi.deleteEntry(entryId) }
        assertEquals(deleteWeightBridgeUseCase.currentData.value.mContent?.isSuccess, false)
        assertEquals(deleteWeightBridgeUseCase.currentData.value.mContent?.isLoading, false)
        assertEquals(deleteWeightBridgeUseCase.currentData.value.mContent?.isError, true)
    }

    @Test
    fun `test DeleteWeightBridgeUseCase run`() = runTest {
        // Given
        val mockWeighBridgeApi = mockk<WeighBridgeApi>()
        val deleteWeightBridgeUseCase = DeleteWeightBridgeUseCase(mockWeighBridgeApi)
        val entryId = "testEntryId"

        coEvery { mockWeighBridgeApi.deleteEntry(entryId) } returns true

        // When
        deleteWeightBridgeUseCase.run(entryId)

        // Then
        coVerify { mockWeighBridgeApi.deleteEntry(entryId) }
        assertEquals(deleteWeightBridgeUseCase.currentData.value.mContent?.isSuccess, true)
        assertEquals(deleteWeightBridgeUseCase.currentData.value.mContent?.isLoading, false)
        assertEquals(deleteWeightBridgeUseCase.currentData.value.mContent?.isError, false)
    }

    @Test
    fun `test GetWeightBridgeUseCase runOffline`() = runTest {
        // Given
        val mockWeighBridgeApi = mockk<WeighBridgeApi>()
        val mockLocalWeightBridgeApi = mockk<LocalWeightBridgeApi>()
        val getWeightBridgeUseCase = GetWeightBridgeUseCase(mockWeighBridgeApi, mockLocalWeightBridgeApi)
        val searchModel = SearchModel()

        val entries = listOf<BridgeWeightEntryItem>()
        coEvery { mockWeighBridgeApi.readEntries(searchModel) }.throws(Throwable())
        coEvery { mockLocalWeightBridgeApi.getEntryList() } returns entries

        // When
        getWeightBridgeUseCase.run(searchModel)

        // Then
        coVerify { mockWeighBridgeApi.readEntries(searchModel) }
        coVerify { mockLocalWeightBridgeApi.getEntryList() }

        assertEquals(getWeightBridgeUseCase.currentData.value.mContent?.data,entries)
        assertEquals(getWeightBridgeUseCase.currentData.value.mContent?.isSuccess,false)
        assertEquals(getWeightBridgeUseCase.currentData.value.mContent?.isLoading,false)
        assertEquals(getWeightBridgeUseCase.currentData.value.mContent?.isError,true)
    }

    @Test
    fun `test GetWeightBridgeUseCase runOnline`() = runTest {
        // Given

        val searchModel = SearchModel()

        val entries = listOf<BridgeWeightEntryItem>()
        val mockWeighBridgeApi = mockk<WeighBridgeApi>(){
            coEvery { readEntries(searchModel) } returns entries
        }
        val mockLocalWeightBridgeApi = mockk<LocalWeightBridgeApi>(){
            coEvery { getEntryList() } returns entries
            every { setEntryList(any()) } returns Unit
        }

        val getWeightBridgeUseCase = GetWeightBridgeUseCase(mockWeighBridgeApi, mockLocalWeightBridgeApi)

        // When
        getWeightBridgeUseCase.run(searchModel)

        // Then
        coVerify(atLeast =  1, atMost = 1) { mockWeighBridgeApi.readEntries(searchModel) }
        coVerify(atLeast =  1, atMost = 1) { mockLocalWeightBridgeApi.setEntryList(entries) }
        coVerify(atLeast =  0, atMost = 0) { mockLocalWeightBridgeApi.getEntryList() }

        assertEquals(getWeightBridgeUseCase.currentData.value.mContent?.data,entries)
        assertEquals(getWeightBridgeUseCase.currentData.value.mContent?.isSuccess,true)
        assertEquals(getWeightBridgeUseCase.currentData.value.mContent?.isLoading,false)
        assertEquals(getWeightBridgeUseCase.currentData.value.mContent?.isError,false)
    }
}