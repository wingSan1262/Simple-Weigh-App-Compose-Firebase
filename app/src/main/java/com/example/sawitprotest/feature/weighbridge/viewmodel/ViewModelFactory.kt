package com.example.sawitprotest.feature.weighbridge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sawitprotest.data.local.LocalWeightBridgeApi
import com.example.sawitprotest.data.local.LocalWeightBridgeApiImpl
import com.example.sawitprotest.data.remote.api.firebase.WeighBridgeApi
import com.example.sawitprotest.data.remote.api.firebase.WeighBridgeApiImpl
import com.example.sawitprotest.domain.usecase.CreateWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.DeleteWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.GetWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.UpdateWeightBridgeUseCase

class ViewModelFactory(
    private val weightBridgeApi: WeighBridgeApi = WeighBridgeApiImpl(),
    private val localWeightBridgeApi: LocalWeightBridgeApi = LocalWeightBridgeApiImpl(),

    var getWeightBridgeUseCase : GetWeightBridgeUseCase = GetWeightBridgeUseCase(
        weightBridgeApi, localWeightBridgeApi
    ),
    var updateWeightBridgeUseCase : UpdateWeightBridgeUseCase = UpdateWeightBridgeUseCase(weightBridgeApi),
    var createWeightBridgeUseCase : CreateWeightBridgeUseCase = CreateWeightBridgeUseCase(weightBridgeApi),
    var deleteWeightBridgeUseCase : DeleteWeightBridgeUseCase = DeleteWeightBridgeUseCase(weightBridgeApi)
): ViewModelProvider.Factory {



    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeighBridgeViewModel::class.java)) {
            return WeighBridgeViewModel(
                getWeightBridgeUseCase,
                updateWeightBridgeUseCase,
                createWeightBridgeUseCase, deleteWeightBridgeUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}