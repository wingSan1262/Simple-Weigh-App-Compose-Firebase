package com.example.sawitprotest.domain.usecase

import com.example.sawitprotest.base.base_classes.BaseUseCase
import com.example.sawitprotest.data.local.LocalWeightBridgeApi
import com.example.sawitprotest.data.remote.api.firebase.WeighBridgeApi
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.example.sawitprotest.data.remote.model.SearchModel

class GetWeightBridgeUseCase(
    private val weightBridgeApi: WeighBridgeApi,
    private val localWeightBridgeApi: LocalWeightBridgeApi
): BaseUseCase<SearchModel, List<BridgeWeightEntryItem>>() {
    override suspend fun run(params: SearchModel){
        super.run(params)
        execute({
            val entries = weightBridgeApi.readEntries(params)
            localWeightBridgeApi.setEntryList(entries)
            entries
        }){
            localWeightBridgeApi.getEntryList()
        }
    }
}

class DeleteWeightBridgeUseCase(
    private val weightBridgeApi: WeighBridgeApi,
): BaseUseCase<String, Unit>() {
    override suspend fun run(params: String){
        super.run(params)
        execute({
            weightBridgeApi.deleteEntry(params)
        }){
            null
        }
    }
}

class UpdateWeightBridgeUseCase(
    private val weightBridgeApi: WeighBridgeApi
): BaseUseCase<BridgeWeightEntryItem, Unit>() {
    override suspend fun run(params: BridgeWeightEntryItem){
        super.run(params)
        execute({
            weightBridgeApi.updateEntry(params)
        }){
            null
        }
    }
}

class CreateWeightBridgeUseCase(
    private val weightBridgeApi: WeighBridgeApi
): BaseUseCase<BridgeWeightEntryItem, Unit>() {
    override suspend fun run(params: BridgeWeightEntryItem){
        super.run(params)
        execute({
            weightBridgeApi.createEntry(params)
        }){
            null
        }
    }
}