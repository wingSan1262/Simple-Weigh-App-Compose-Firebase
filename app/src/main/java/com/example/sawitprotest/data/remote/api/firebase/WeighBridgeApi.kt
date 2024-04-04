package com.example.sawitprotest.data.remote.api.firebase

import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.example.sawitprotest.data.remote.model.SearchModel
import com.example.sawitprotest.feature.weighbridge.model.SortType
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.coroutines.tasks.await

interface WeighBridgeApi {
    suspend fun createEntry(entry: BridgeWeightEntryItem): Boolean
    suspend fun readEntries(searchModel: SearchModel): List<BridgeWeightEntryItem>
    suspend fun updateEntry(entry: BridgeWeightEntryItem): Boolean
    suspend fun deleteEntry(id: String): Boolean
}

class WeighBridgeApiImpl(
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(),
    private val weighBridgeReference: DatabaseReference = database.getReference("weigh_bridge")
) : WeighBridgeApi {

    var  taskWait :  suspend (Task<Void>) -> Any? = { task -> task.await()}
    override suspend fun createEntry(entry: BridgeWeightEntryItem): Boolean {
        val id = weighBridgeReference.push().key
        id?.let {
            taskWait(weighBridgeReference.child(it).setValue(entry.copy(id = it)))
            return true
        } ?: kotlin.run {
            return false
        }
    }

    var processAwait : suspend (Query) -> DataSnapshot = { query -> query.get().await()}

    override suspend fun readEntries(
        searchModel: SearchModel
    ): List<BridgeWeightEntryItem> {
        var query : Query = weighBridgeReference

        fun checkFilter(item: BridgeWeightEntryItem) : BridgeWeightEntryItem?{
            if (searchModel.filter.driverNameContain.isNotEmpty() && !item.driverName.contains(searchModel.filter.driverNameContain)) {
                return null
            }
            if (searchModel.filter.licenseNumberContain.isNotEmpty() && !item.licenseNumber.contains(searchModel.filter.licenseNumberContain)) {
                return null
            }


            if (searchModel.filter.minDate > 0 && item.dateTime < searchModel.filter.minDate) {
                return null
            }
            if (searchModel.filter.maxDate != Long.MAX_VALUE && item.dateTime > searchModel.filter.maxDate) {
                return null
            }

            if(searchModel.keyworkd.isNotEmpty()){
                if(!item.driverName.contains(searchModel.keyworkd, ignoreCase = true)
                        && !item.licenseNumber.contains(searchModel.keyworkd, ignoreCase = true)){
                    return null
                }
            }

            return item
        }

        fun sortList(entries: List<BridgeWeightEntryItem>) : List<BridgeWeightEntryItem>{
            val result = when(searchModel.sort.sortType){
                SortType.NONE -> entries
                SortType.OLDEST_DATE -> entries.sortedBy { it.dateTime }
                SortType.NEWEST_DATE -> entries.sortedByDescending { it.dateTime }
                SortType.DRIVER_NAME_ASC -> entries.sortedBy { it.driverName }
                SortType.DRIVER_NAME_DESC -> entries.sortedByDescending { it.driverName }
                SortType.LICENSE_NUMBER_ASC -> entries.sortedBy { it.licenseNumber }
                SortType.LICENSE_NUMBER_DESC -> entries.sortedByDescending { it.licenseNumber }
            }
            return result
        }
        val dataSnapshot = processAwait(query)
        val entries = dataSnapshot.children.mapNotNull {
            it.getValue(BridgeWeightEntryItem::class.java)?.let {
                val checkFilter = checkFilter(it) ?: return@mapNotNull null
                return@mapNotNull checkFilter
            }
            null
        }
        return sortList(entries = entries)
    }

    override suspend fun updateEntry(entry: BridgeWeightEntryItem): Boolean {
        entry.id.let {
            taskWait(weighBridgeReference.child(it).setValue(entry))
            return true
        }
    }

    override suspend fun deleteEntry(id: String): Boolean {
        taskWait(weighBridgeReference.child(id).removeValue())
        return true
    }
}