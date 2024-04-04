package com.example.sawitprotest.feature.weighbrighe

import com.example.sawitprotest.data.local.LocalWeightBridgeApi
import com.example.sawitprotest.data.remote.api.firebase.WeighBridgeApi
import com.example.sawitprotest.domain.usecase.CreateWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.DeleteWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.GetWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.UpdateWeightBridgeUseCase
import com.example.sawitprotest.feature.weighbridge.viewmodel.ViewModelFactory
import com.example.sawitprotest.feature.weighbridge.viewmodel.WeighBridgeViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class ViewModelFactoryTest {

    @Test
    fun `test ViewModelFactory create`() {
        // Given
        val mockWeighBridgeApi = mockk<WeighBridgeApi>()
        val mockLocalWeightBridgeApi = mockk<LocalWeightBridgeApi>()
        val getWeightBridgeUseCase = mockk<GetWeightBridgeUseCase>(){
            every { currentData } returns mockk()
        }
        val updateWeightBridgeUseCase = mockk<UpdateWeightBridgeUseCase>(){
            every { currentData } returns mockk()
        }
        val createWeightBridgeUseCase = mockk<CreateWeightBridgeUseCase>(){
            every { currentData } returns mockk()
        }
        val deleteWeightBridgeUseCase = mockk<DeleteWeightBridgeUseCase>(){
            every { currentData } returns mockk()
        }

        val viewModelFactory = ViewModelFactory(
            weightBridgeApi = mockWeighBridgeApi,
            localWeightBridgeApi = mockLocalWeightBridgeApi,
            getWeightBridgeUseCase = getWeightBridgeUseCase,
            updateWeightBridgeUseCase = updateWeightBridgeUseCase,
            createWeightBridgeUseCase = createWeightBridgeUseCase,
            deleteWeightBridgeUseCase = deleteWeightBridgeUseCase
        )

        // When
        val viewModel = viewModelFactory.create(WeighBridgeViewModel::class.java)

        // Then
        assertEquals(viewModel.createWeightBridgeUseCase, createWeightBridgeUseCase)
        assertEquals(viewModel.deleteWeightBridgeUseCase, deleteWeightBridgeUseCase)
        assertEquals(viewModel.getWeightBridgeUseCase, getWeightBridgeUseCase)
        assertEquals(viewModel.updateWeightBridgeUseCase, updateWeightBridgeUseCase)
    }
}