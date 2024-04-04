package com.example.sawitprotest.feature.weighbrighe

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.example.sawitprotest.domain.usecase.CreateWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.DeleteWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.GetWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.UpdateWeightBridgeUseCase
import com.example.sawitprotest.feature.weighbridge.viewmodel.WeighBridgeViewModel
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ViewModel {

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
    fun `test getWeightList action`() = runTest {
        // Given
        val mockGetWeightBridgeUseCase = mockk<GetWeightBridgeUseCase>(relaxed = true)
        val viewModel = WeighBridgeViewModel(
            getWeightBridgeUseCase = mockGetWeightBridgeUseCase,
            updateWeightBridgeUseCase = mockk(relaxed = true),
            createWeightBridgeUseCase = mockk(relaxed = true),
            deleteWeightBridgeUseCase = mockk(relaxed = true)
        )

        // When
        viewModel.getWeightList()

        // Then
        coVerify { mockGetWeightBridgeUseCase.run(viewModel.queryReqWeightBridge) }
    }

    @Test
    fun `test updateWeightBridge action`() = runTest {
        // Given
        val mockUpdateWeightBridgeUseCase = mockk<UpdateWeightBridgeUseCase>(relaxed = true)
        val viewModel = WeighBridgeViewModel(
            getWeightBridgeUseCase = mockk(relaxed = true),
            updateWeightBridgeUseCase = mockUpdateWeightBridgeUseCase,
            createWeightBridgeUseCase = mockk(relaxed = true),
            deleteWeightBridgeUseCase = mockk(relaxed = true)
        )
        val item = BridgeWeightEntryItem("testEntryId")

        // When
        viewModel.updateWeightBridge(item)

        // Then
        coVerify { mockUpdateWeightBridgeUseCase.run(item) }
    }

    @Test
    fun `test createWeightBridge action`() = runTest {
        // Given
        val mockCreateWeightBridgeUseCase = mockk<CreateWeightBridgeUseCase>(relaxed = true)
        val viewModel = WeighBridgeViewModel(
            getWeightBridgeUseCase = mockk(relaxed = true),
            updateWeightBridgeUseCase = mockk(relaxed = true),
            createWeightBridgeUseCase = mockCreateWeightBridgeUseCase,
            deleteWeightBridgeUseCase = mockk(relaxed = true)
        )
        val item = BridgeWeightEntryItem("testEntryId")

        // When
        viewModel.createWeightBridge(item)

        // Then
        coVerify { mockCreateWeightBridgeUseCase.run(item) }
    }

    @Test
    fun `test deleteWeightBridge action`() = runTest {
        // Given
        val mockDeleteWeightBridgeUseCase = mockk<DeleteWeightBridgeUseCase>(relaxed = true)
        val viewModel = WeighBridgeViewModel(
            getWeightBridgeUseCase = mockk(relaxed = true),
            updateWeightBridgeUseCase = mockk(relaxed = true),
            createWeightBridgeUseCase = mockk(relaxed = true),
            deleteWeightBridgeUseCase = mockDeleteWeightBridgeUseCase
        )
        val id = "testEntryId"

        // When
        viewModel.deleteWeightBridge(id)

        // Then
        coVerify { mockDeleteWeightBridgeUseCase.run(id) }
    }
}