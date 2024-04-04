package com.example.sawitprotest.data.remote.model

import com.example.sawitprotest.feature.weighbridge.model.SortType


// i'm not creating domain model for this project
// normally i will create domain model and map it to this model
// but for this project i will use this model directly
// because the app is simple and the data is not complex
data class BridgeWeightEntryItem(
    var id: String = "",
    var dateTime: Long = 0L,
    var licenseNumber: String = "",
    var driverName: String = "",
    var inboundWeight: Double = 0.0,
    var outboundWeight: Double = 0.0,
) {
    val netWeight: Double
        get() = outboundWeight - inboundWeight // Calculate net weight
}

data class FilterWeighSearch(
    var maxDate : Long = Long.MAX_VALUE,
    var minDate : Long = 0,
    var driverNameContain: String = "",
    var licenseNumberContain: String = "",
)

data class SortWeighSearch(
    var sortType: SortType = SortType.NONE,
){
    fun clearSort() {
        this.sortType = SortType.NONE
    }
}

data class SearchModel(
    var keyworkd : String = "",
    var filter: FilterWeighSearch = FilterWeighSearch(),
    var sort: SortWeighSearch = SortWeighSearch()
) {
    fun clearFilter(){
        filter.maxDate = Long.MAX_VALUE
        filter.minDate = 0
        filter.driverNameContain = ""
        filter.licenseNumberContain = ""
    }
    fun clearSort(){
        sort.clearSort()
    }
    fun clearSearch(){
        clearFilter()
        clearSort()
        keyworkd = ""
    }
}

