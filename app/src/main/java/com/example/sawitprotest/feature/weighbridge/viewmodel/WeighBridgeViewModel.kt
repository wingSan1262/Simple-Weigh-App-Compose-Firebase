package com.example.sawitprotest.feature.weighbridge.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sawitprotest.base.extensions.runScoped
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.example.sawitprotest.data.remote.model.SearchModel
import com.example.sawitprotest.domain.usecase.CreateWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.DeleteWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.GetWeightBridgeUseCase
import com.example.sawitprotest.domain.usecase.UpdateWeightBridgeUseCase

class WeighBridgeViewModel(
    val getWeightBridgeUseCase: GetWeightBridgeUseCase,
    val updateWeightBridgeUseCase: UpdateWeightBridgeUseCase,
    val createWeightBridgeUseCase: CreateWeightBridgeUseCase,
    val deleteWeightBridgeUseCase: DeleteWeightBridgeUseCase
) : ViewModel() {

    val getWeightListData = getWeightBridgeUseCase.currentData
    var queryReqWeightBridge = SearchModel()
    fun getWeightList(){
        runScoped {
            getWeightBridgeUseCase.run(queryReqWeightBridge)
        }
    }


    val updateWeightBridgeData = updateWeightBridgeUseCase.currentData
    fun updateWeightBridge(
        item: BridgeWeightEntryItem
    ){
        runScoped {
            updateWeightBridgeUseCase.run(item)
        }
    }

    val createWeightBridgeData = createWeightBridgeUseCase.currentData
    fun createWeightBridge(item: BridgeWeightEntryItem){
        runScoped {
            createWeightBridgeUseCase.run(item)
        }
    }

    var selectedItem : BridgeWeightEntryItem = BridgeWeightEntryItem()
    var isEdit = false

    val deleteWeigthBridgeData = deleteWeightBridgeUseCase.currentData
    fun deleteWeightBridge(id: String){
        runScoped {
            deleteWeightBridgeUseCase.run(id)
        }
    }
}